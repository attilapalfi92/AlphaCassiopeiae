package com.attilapalfi.network

import com.attilapalfi.commons.IntelligentTcpMessageBuffer
import com.attilapalfi.commons.TCP_BUFFER_SIZE
import com.attilapalfi.commons.TcpMessageBuffer
import com.attilapalfi.commons.messages.ServerTcpMessage
import com.attilapalfi.commons.messages.ServerTcpMessageType.*
import com.attilapalfi.commons.utlis.ServerMessageConverter
import com.attilapalfi.controller.AndroidController
import com.attilapalfi.controller.Controller
import com.attilapalfi.controller.ControllerInputHandler
import com.attilapalfi.controller.Connection
import com.attilapalfi.exception.ConnectionException
import com.attilapalfi.logger.logInfo
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by palfi on 2016-04-10.
 */
class TcpConnection(private val controllerInputHandler: ControllerInputHandler,
                    private val tcpConnectionEventListener: TcpConnectionEventListener) :
        Connection {

    private val tcpSendingExecutor: ExecutorService = Executors.newSingleThreadExecutor();

    val serverPort: Int
    private val serverSocket: ServerSocket = ServerSocket()
    private var remoteSocket: Socket? = null
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

    val controller: Controller = AndroidController(controllerInputHandler, this)

    private val tcpMessageBuffer: TcpMessageBuffer by lazy {
        IntelligentTcpMessageBuffer(ServerTcpSignalProcessor(controller))
    }

    private val messageConverter = ServerMessageConverter()

    init {
        serverSocket.bind(null)
        serverPort = serverSocket.localPort
    }

    override fun sendVibration(milliseconds: Int) {
        tcpSendingExecutor.submit {
            val message = messageConverter.messageToByteArray(ServerTcpMessage(VIBRATE, milliseconds))
            remoteSocket?.outputStream?.use { it.write(message) }
        }
    }

    override fun sendGlow(milliseconds: Int) {
        throw UnsupportedOperationException()
    }

    override fun sendStartSensorDataStream() {
        val message = messageConverter.messageToByteArray(ServerTcpMessage(START_SENSOR_STREAM))
        remoteSocket?.outputStream?.use { it.write(message) }
    }

    override fun sendStopSensorDataStream() {
        val message = messageConverter.messageToByteArray(ServerTcpMessage(STOP_SENSOR_STREAM))
        remoteSocket?.outputStream?.use { it.write(message) }
    }

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
                    "Server port: $serverPort, client IP: ${clientIp ?: "null"} " +
                    "client port: ${clientPort ?: "null"}")
        } catch (e: ConnectionException) {
            //TODO: ??? connectionEventHandler.onTcpConnectionDeath(this)
        } finally {
            disconnect();
            tcpConnectionEventListener.onTcpDisconnect(this)
        }
    }

    private fun connectAndSetProperties() {
        remoteSocket = serverSocket.accept() // blocks until connection
        remoteSocket?.keepAlive = true
        clientIp = remoteSocket?.inetAddress
        clientPort = remoteSocket?.port
        controller.address = clientIp
        tcpConnectionEventListener.onTcpConnected(this)
    }

    private fun readFromSocket() {
        val array = ByteArray(TCP_BUFFER_SIZE)
        var readBytes: Int = 0
        remoteSocket?.inputStream?.use {
            while (readBytes != -1) {
                readBytes = it.read(array)
                if (readBytes != -1) {
                    tcpMessageBuffer.tryToProcess(array, readBytes)
                }
            }
        }
    }

    fun isConnected(): Boolean {
        remoteSocket?.let {
            return !it.isClosed && it.isConnected
        }
        return false
    }

    fun disconnect() {
        stopped = true
        serverSocket.close()
        remoteSocket?.close()
    }

}