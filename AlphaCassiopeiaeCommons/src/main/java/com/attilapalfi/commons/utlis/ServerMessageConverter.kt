package com.attilapalfi.commons.utlis

import com.attilapalfi.commons.messages.TcpClientMessage
import com.attilapalfi.commons.messages.TcpServerMessage
import com.attilapalfi.commons.messages.UdpDiscoveryBroadcast
import com.attilapalfi.commons.messages.UdpSensorData
import org.apache.commons.lang3.SerializationUtils

/**
 * Created by palfi on 2016-01-15.
 */
object ServerMessageConverter {

    fun tcpMessageToByteArray(message: TcpServerMessage): ByteArray
            = SerializationUtils.serialize(message)

    fun byteArrayToTcpMessage(payload: ByteArray): TcpClientMessage
            = SerializationUtils.deserialize(payload)

    fun udpDiscoveryToByteArray(message: UdpDiscoveryBroadcast): ByteArray
            = SerializationUtils.serialize(message)

    fun byteArrayToSensorData(payload: ByteArray): UdpSensorData
            = SerializationUtils.deserialize(payload)
}