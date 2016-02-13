package com.attilapalfi.network

import com.attilapalfi.commons.BUFFER_SIZE
import com.attilapalfi.commons.IntelligentTcpMessageBuffer
import com.attilapalfi.commons.TcpMessageBuffer
import com.attilapalfi.commons.UdpMessageBroadcaster
import com.attilapalfi.commons.messages.MESSAGE_END
import com.attilapalfi.commons.messages.REG_ACK
import com.attilapalfi.commons.messages.START_ACK
import com.attilapalfi.commons.messages.TcpServerMessage
import com.attilapalfi.commons.utlis.ServerMessageConverter
import com.attilapalfi.game.World
import com.badlogic.gdx.Gdx
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket


/**
 * Created by palfi on 2016-02-06.
 */
class TcpConnectionHandler(world: World, messageBroadcaster: UdpMessageBroadcaster,
                           communicationManager: CommunicationManager) : Thread() {

    val serverAddress: InetAddress
    val serverPort: Int
    private val serverSocket: ServerSocket = ServerSocket()
    private val connection: Socket by lazy { serverSocket.accept() }
    val clientIp: InetAddress by lazy { connection.inetAddress }
    val clientPort: Int by lazy { connection.port }

    private val tcpMessageBuffer: TcpMessageBuffer =
            IntelligentTcpMessageBuffer(ServerTcpSignalProcessor(world, this, messageBroadcaster, communicationManager))

    init {
        serverSocket.bind(null)
        serverAddress = serverSocket.inetAddress
        serverPort = serverSocket.localPort
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
                            tcpMessageBuffer.tryToProcess(array, readBytes)
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
        val message = ServerMessageConverter.tcpMessageToByteArray(TcpServerMessage(REG_ACK)) + MESSAGE_END
        connection.outputStream.use { it.write(message) }
    }

    @Synchronized
    fun sendStartAck() {
        val message = ServerMessageConverter.tcpMessageToByteArray(TcpServerMessage(START_ACK)) + MESSAGE_END
        connection.outputStream.use { it.write(message) }
    }

    fun isConnected(): Boolean = !connection.isClosed && connection.isConnected

    override fun interrupt() {
        super.interrupt()
        serverSocket.close()
        connection.close()
    }
}