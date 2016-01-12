package com.attilapalfi.network

import com.attilapalfi.game.MessageProcessor
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by palfi on 2016-01-11.
 */
class MessageReceiver(private val messageProcessor: MessageProcessor,
                      private val port: Int) {

    @Volatile
    public var started = false
    @Volatile
    private var stopped = false

    @Synchronized
    fun startReceiving() {
        if (!started) {
            started = true
            Thread({
                val socket: DatagramSocket = DatagramSocket(port)
                while (!stopped) {
                    val buffer: ByteArray = ByteArray(100)
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)
                    messageProcessor.process(packet)
                }
            }).start()
        }
    }

    @Synchronized
    fun stopReceiving() {
        stopped = true
    }
}