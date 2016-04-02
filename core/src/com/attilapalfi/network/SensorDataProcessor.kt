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
class SensorDataProcessor(private val eventHandler: GameEventHandler) : UdpPacketProcessor {

    override fun process(packet: DatagramPacket) {
        try {
            val sensorData = ServerMessageConverter.byteArrayToSensorData(packet.data)
            when (sensorData.type) {
                SENSOR_DATA -> {
                    eventHandler.onSetPlayerSpeed(packet.address, sensorData.x, sensorData.y)
                }
                SHOOT -> {
                    handleShoot(packet.address, sensorData)
                }
                SHITBOMB -> {
                    handleShitBomb(packet.address, sensorData)
                }
            }
        } catch (e: SerializationException) {

        }
    }

    private fun handleShoot(packet: InetAddress, sensorData: UdpSensorData) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleShitBomb(address: InetAddress?, sensorData: UdpSensorData) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}