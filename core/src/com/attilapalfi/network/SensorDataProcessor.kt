package com.attilapalfi.network

import com.attilapalfi.commons.UdpPacketProcessor
import com.attilapalfi.commons.messages.SENSOR_DATA
import com.attilapalfi.commons.messages.SHITBOMB
import com.attilapalfi.commons.messages.SHOOT
import com.attilapalfi.commons.messages.UdpSensorData
import com.attilapalfi.commons.utlis.ServerMessageConverter
import com.attilapalfi.core.World
import org.apache.commons.lang3.SerializationException
import java.net.DatagramPacket
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-11.
 */
class SensorDataProcessor(private val dataListener: SensorDataListener) : UdpPacketProcessor {

    override fun process(packet: DatagramPacket) {
        try {
            val sensorData = ServerMessageConverter.byteArrayToSensorData(packet.data)
            when (sensorData.type) {
                SENSOR_DATA -> {
                    dataListener.onSetPlayerSpeed(packet.address, sensorData.x, sensorData.y)
                }
            }
        } catch (e: SerializationException) {

        }
    }
}