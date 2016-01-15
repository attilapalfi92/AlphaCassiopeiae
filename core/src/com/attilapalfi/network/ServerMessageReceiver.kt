package com.attilapalfi.network

import com.attilapalfi.common.MessageProcessor
import com.attilapalfi.common.MessageReceiver
import java.net.DatagramPacket
import java.net.DatagramSocket

/**
 * Created by palfi on 2016-01-11.
 */
class ServerMessageReceiver(private val messageProcessor: MessageProcessor,
                            private val port: Int,
                            private val bufferSize: Int) : MessageReceiver {

    @Volatile
    private var started = false

    @Synchronized
    override fun startReceiving() {
        if (!started) {
            started = true
            Thread({
                DatagramSocket(port).use {
                    while (true) {
                        val buffer: ByteArray = ByteArray(bufferSize)
                        val packet = DatagramPacket(buffer, buffer.size)
                        it.receive(packet)
                        messageProcessor.process(packet)
                    }
                }
            }).start()
        }
    }

    override fun started(): Boolean = started
}