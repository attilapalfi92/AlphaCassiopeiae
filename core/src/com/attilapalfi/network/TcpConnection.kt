package com.attilapalfi.network

import com.attilapalfi.commons.IntelligentTcpMessageBuffer
import com.attilapalfi.commons.TCP_BUFFER_SIZE
import com.attilapalfi.commons.TcpMessageBuffer
import com.attilapalfi.commons.messages.ServerTcpMessage
import com.attilapalfi.commons.messages.ServerTcpMessageType.*
import com.attilapalfi.commons.utlis.ServerMessageConverter
import com.attilapalfi.controller.AndroidController
import com.attilapalfi.controller.Controller
import com.attilapalfi.controller.ControllerEventHandler
import com.attilapalfi.controller.ControllerNotifier
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
class TcpConnection(private val controllerEventHandler: ControllerEventHandler,
                    private val tcpConnectionEventListener: TcpConnectionEventListener) :
        ControllerNotifier {

    companion object {
        private val tcpSendingExecutor: ExecutorService = Executors.newFixedThreadPool(4)
    }

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

    val controller: Controller = AndroidController(controllerEventHandler, this)

    private val tcpMessageBuffer: TcpMessageBuffer by lazy {
        IntelligentTcpMessageBuffer(ServerTcpSignalProcessor(controller))
    }

    private val messageConverter = ServerMessageConverter()

    init {
        serverSocket.bind(null)
        serverPort = serverSocket.localPort
    }

    @Synchronized
    override fun sendVibration(milliseconds: Int) {
        tcpSendingExecutor.submit {
            val message = messageConverter.messageToByteArray(ServerTcpMessage(VIBRATE, milliseconds))
            connection?.outputStream?.use { it.write(message) }
        }
    }

    @Synchronized
    override fun sendStartSensorDataStream() {
        val message = messageConverter.messageToByteArray(ServerTcpMessage(START_SENSOR_STREAM))
        connection?.outputStream?.use { it.write(message) }
    }

    @Synchronized
    override fun sendStopSensorDataStream() {
        val message = messageConverter.messageToByteArray(ServerTcpMessage(STOP_SENSOR_STREAM))
        connection?.outputStream?.use { it.write(message) }
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
                    "Server port: $serverPort, client IP: ${clientIp ?: "null"} " +
                    "client port: ${clientPort ?: "null"}")
        } catch (e: ConnectionException) {
            //TODO: ??? connectionEventHandler.onTcpConnectionDeath(this)
        } finally {
            tcpConnectionEventListener.onDisconnect(this)
        }
    }

    private fun connectAndSetProperties() {
        connection = serverSocket.accept() // blocks until connection
        connection?.keepAlive = true
        clientIp = connection?.inetAddress
        clientPort = connection?.port
        controller.address = clientIp
        tcpConnectionEventListener.onConnect(this)
    }

    private fun readFromSocket() {
        val array = ByteArray(TCP_BUFFER_SIZE)
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