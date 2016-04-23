package com.attilapalfi.network

import com.attilapalfi.commons.UdpPacketProcessor
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.logger.logError
import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-11.
 */
class SensorDataProcessor(private val dataListener: SensorDataListener) : UdpPacketProcessor {

    val messageConverter = ClientMessageConverter()

    override fun process(packet: DatagramPacket) {
        try {
            val sensorData = messageConverter.byteArrayToUdpSensorData(packet.data)
            dataListener.updatePlayerData(packet.address, sensorData)
        } catch (e: Exception) {
            logError("SensorDataProcessor", e.message ?: "null")
        }
    }
}