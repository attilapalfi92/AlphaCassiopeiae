package com.attilapalfi.network

import com.attilapalfi.common.messages.ServerMessage
import org.apache.commons.lang3.SerializationUtils
import java.net.DatagramPacket
import java.net.DatagramSocket

/**
 * Created by palfi on 2016-01-15.
 */
class ServerMessageSender : MessageSender {

    private val socket = DatagramSocket()

    override fun send(client: Client, message: ServerMessage) {
        val payload: ByteArray = messageToByteArray(message)
        socket.send(DatagramPacket(payload, payload.size, client.IP, client.port))
    }

    private fun messageToByteArray(message: ServerMessage): ByteArray
            = SerializationUtils.serialize(message)

    override fun close() {
        socket.close()
    }
}