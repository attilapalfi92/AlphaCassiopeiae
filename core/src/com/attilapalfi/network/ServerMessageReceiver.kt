package com.attilapalfi.network

import com.attilapalfi.common.BUFFER_SIZE
import com.attilapalfi.common.MessageReceiver
import com.attilapalfi.common.PORT
import com.attilapalfi.common.PacketProcessor
import com.badlogic.gdx.Gdx
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket

/**
 * Created by palfi on 2016-01-11.
 */
class ServerMessageReceiver(private val packetProcessor: PacketProcessor) : MessageReceiver {

    @Volatile
    private var started = false
    private val socket = DatagramSocket(PORT)

    private val thread: Thread by lazy { newThread() }

    private fun newThread(): Thread {
        return Thread({
            socket.use {
                while (!socket.isClosed) {
                    try {
                        receive(it, packetProcessor)
                    } catch (e: IOException) {
                        Gdx.app.log("SerMesRec", "socket closed or error", e)
                    }
                }
            }
        })
    }

    @Synchronized
    override fun startReceiving() {
        if (!started) {
            started = true
            thread.start()
        }
    }

    private fun receive(socket: DatagramSocket, packetProcessor: PacketProcessor) {
        val buffer: ByteArray = ByteArray(BUFFER_SIZE)
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)
        packetProcessor.process(packet)
    }

    override fun stopReceiving() {
        socket.disconnect()
    }

    override fun started(): Boolean = started
}