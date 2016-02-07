package com.attilapalfi.network

import com.badlogic.gdx.Gdx
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket


/**
 * Created by palfi on 2016-02-06.
 */
class TcpServerThread : Thread() {

    val address: InetAddress
    val port: Int
    private val serverSocket: ServerSocket = ServerSocket()
    private val socket: Socket by lazy { serverSocket.accept() }
    private val messageBuffer: TcpMessageBuffer = TcpMessageBuffer()

    init {
        serverSocket.bind(null)
        address = serverSocket.inetAddress
        port = serverSocket.localPort
    }

    override fun run() {
        try {
            socket.keepAlive = true // blocks until connection, lazy init
            while (!Thread.interrupted()) {


                val array = ByteArray(300)
                val inputStream = socket.inputStream
                var readBytes: Int = 0

                while (readBytes != -1) {
                    readBytes = inputStream.read(array)
                    if (readBytes != -1) {
                        messageBuffer.tryToProcess(array, readBytes)
                    }
                }

            }
        } catch (e: IOException) {
            Gdx.app.log("TcpServer", "TCP Server is closed, thread run finishes.")
        }
    }

    override fun interrupt() {
        super.interrupt()
        serverSocket.close()
        socket.close()
    }

}