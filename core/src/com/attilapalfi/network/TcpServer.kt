package com.attilapalfi.network

import com.attilapalfi.common.BUFFER_SIZE
import com.attilapalfi.common.MessageBuffer
import com.attilapalfi.common.messages.MESSAGE_END
import com.attilapalfi.common.messages.REG_ACK
import com.attilapalfi.common.messages.START_ACK
import com.attilapalfi.common.messages.TcpServerMessage
import com.attilapalfi.game.World
import com.attilapalfi.network.utlis.Converter
import com.badlogic.gdx.Gdx
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket


/**
 * Created by palfi on 2016-02-06.
 */
class TcpServer(world: World, messageBroadcaster: MessageBroadcaster,
                communicationManager: CommunicationManager) : Thread() {

    val address: InetAddress
    val port: Int
    private val serverSocket: ServerSocket = ServerSocket()
    private val connection: Socket by lazy { serverSocket.accept() }
    val clientIp: InetAddress by lazy { connection.inetAddress }
    val clientPort: Int by lazy { connection.port }

    private val messageBuffer: MessageBuffer = TcpMessageBuffer(this,
            TcpSignalProcessor(world, this, messageBroadcaster, communicationManager))

    init {
        serverSocket.bind(null)
        address = serverSocket.inetAddress
        port = serverSocket.localPort
    }

    override fun run() {
        try {
            connection.keepAlive = true // blocks until connection, lazy init
            while (!Thread.interrupted()) {
                val array = ByteArray(BUFFER_SIZE)
                var readBytes: Int = 0
                connection.inputStream.use {
                    while (readBytes != -1) {
                        readBytes = it.read(array)
                        if (readBytes != -1) {
                            messageBuffer.tryToProcess(array, readBytes)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Gdx.app.log("TcpServer", "TCP Server is closed, thread run finishes.")
        }
    }

    @Synchronized
    fun sendRegAck() {
        val message = Converter.tcpMessageToByteArray(TcpServerMessage(REG_ACK)) + MESSAGE_END
        connection.outputStream.use { it.write(message) }
    }

    @Synchronized
    fun sendStartAck() {
        val message = Converter.tcpMessageToByteArray(TcpServerMessage(START_ACK)) + MESSAGE_END
        connection.outputStream.use { it.write(message) }
    }

    fun isConnected(): Boolean = !connection.isClosed && connection.isConnected

    override fun interrupt() {
        super.interrupt()
        serverSocket.close()
        connection.close()
    }
}