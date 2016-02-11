package com.attilapalfi.network.utlis

import com.attilapalfi.common.messages.TcpClientMessage
import com.attilapalfi.common.messages.TcpServerMessage
import com.attilapalfi.common.messages.UdpDiscoveryBroadcast
import com.attilapalfi.common.messages.UdpSensorData
import org.apache.commons.lang3.SerializationUtils

/**
 * Created by palfi on 2016-01-15.
 */
object Converter {
    fun byteArrayToTcpMessage(payload: ByteArray): TcpClientMessage
            = SerializationUtils.deserialize(payload)

    fun byteArrayToSensorData(payload: ByteArray): UdpSensorData
            = SerializationUtils.deserialize(payload)

    fun tcpMessageToByteArray(message: TcpServerMessage): ByteArray
            = SerializationUtils.serialize(message)

    fun udpDiscoveryToByteArray(message: UdpDiscoveryBroadcast): ByteArray
            = SerializationUtils.serialize(message)
}