package com.attilapalfi.network

import com.attilapalfi.commons.BUFFER_SIZE
import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.commons.UdpPacketProcessor
import com.attilapalfi.commons.UdpPacketReceiver
import com.badlogic.gdx.Gdx
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-11.
 */
class SensorDataReceiver(private val packetProcessor: UdpPacketProcessor) : UdpPacketReceiver {

    @Volatile
    private var started = false
    private val socket = DatagramSocket(UDP_PORT)
    private val localHost = InetAddress.getLocalHost()
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

    private fun receive(socket: DatagramSocket, packetProcessor: UdpPacketProcessor) {
        val buffer: ByteArray = ByteArray(BUFFER_SIZE)
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)
        if (localHost != packet.address) {
            packetProcessor.process(packet)
        }
    }

    override fun stopReceiving() {
        socket.close()
    }

    override fun started(): Boolean = started
}