package com.attilapalfi.commons.utlis

import com.attilapalfi.commons.messages.TcpClientMessage
import com.attilapalfi.commons.messages.UdpSensorData
import org.apache.commons.lang3.SerializationUtils

/**
 * Created by palfi on 2016-02-13.
 */
object ClientMessageConverter {

    fun udpSensorDataToByteArray(message: UdpSensorData): ByteArray
            = SerializationUtils.serialize(message)

    fun byteArrayToUdpSensorData(payload: ByteArray): UdpSensorData
            = SerializationUtils.deserialize(payload)

    fun tcpClientMessageToByteArray(message: TcpClientMessage): ByteArray
            = SerializationUtils.serialize(message)

    fun byteArrayToTcpClientMessage(payload: ByteArray): TcpClientMessage
            = SerializationUtils.deserialize(payload)
}
