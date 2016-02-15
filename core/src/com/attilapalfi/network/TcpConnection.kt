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
import com.attilapalfi.exception.ConnectionException
import com.attilapalfi.game.World
import com.attilapalfi.logger.logInfo
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException


/**
 * Created by palfi on 2016-02-06.
 */
class TcpConnection(world: World, messageBroadcaster: UdpMessageBroadcaster,
                    ackSender: AckSender,
                    private val connectionManager: TcpConnectionManager) {

    val serverPort: Int
    private val serverSocket: ServerSocket = ServerSocket()
    private var connection: Socket? = null
    var clientIp: InetAddress? = null
        private set
    var clientPort: Int? = null
        private set

    @Volatile
    var stopped: Boolean = false
        private set

    @Volatile
    var started: Boolean = false
        private set

    private val tcpMessageBuffer: TcpMessageBuffer =
            IntelligentTcpMessageBuffer(ServerTcpSignalProcessor(world, this, messageBroadcaster, ackSender))

    init {
        serverSocket.bind(null)
        serverPort = serverSocket.localPort
    }

    @Synchronized
    fun start() {
        if (!started) {
            started = true
            Thread({ run() }).start()
        }
    }

    private fun run() {
        try {
            connectAndSetProperties()
            while (!stopped) {
                readFromSocket()
            }
        } catch (se: SocketException) {
            logInfo("TcpServer", "TCP Server is closed, thread run finishes. " +
                    "Server port: $serverPort, client IP: ${clientIp ?: "null"} client port: ${clientPort ?: "null"}")
        } catch (e: ConnectionException) {
            connectionManager.onTcpConnectionDeath(this)
        }
    }

    private fun connectAndSetProperties() {
        connection = serverSocket.accept() // blocks until connection
        connection?.keepAlive = true
        clientIp = connection?.inetAddress
        clientPort = connection?.port
    }

    private fun readFromSocket() {
        val array = ByteArray(BUFFER_SIZE)
        var readBytes: Int = 0
        connection?.inputStream?.use {
            while (readBytes != -1) {
                readBytes = it.read(array)
                if (readBytes != -1) {
                    tcpMessageBuffer.tryToProcess(array, readBytes)
                }
            }
        }
    }

    @Synchronized
    fun sendRegAck() {
        val message = ServerMessageConverter.tcpMessageToByteArray(TcpServerMessage(REG_ACK)) + MESSAGE_END
        connection?.outputStream?.use { it.write(message) }
    }

    @Synchronized
    fun sendStartAck() {
        val message = ServerMessageConverter.tcpMessageToByteArray(TcpServerMessage(START_ACK)) + MESSAGE_END
        connection?.outputStream?.use { it.write(message) }
    }

    fun isConnected(): Boolean {
        connection?.let {
            return !it.isClosed && it.isConnected
        }
        return false
    }

    fun disconnect() {
        stopped = true
        serverSocket.close()
        connection?.close()
    }
}