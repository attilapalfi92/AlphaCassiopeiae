package com.attilapalfi.network

import com.attilapalfi.common.PacketProcessor
import com.attilapalfi.common.messages.SENSOR_DATA
import com.attilapalfi.common.messages.SHITBOMB
import com.attilapalfi.common.messages.SHOOT
import com.attilapalfi.common.messages.UdpSensorData
import com.attilapalfi.game.World
import com.attilapalfi.network.utlis.Converter
import org.apache.commons.lang3.SerializationException
import java.net.DatagramPacket
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-11.
 */
class UdpPacketProcessor(private val world: World) : PacketProcessor {

    override fun process(packet: DatagramPacket) {
        try {
            val sensorData = Converter.byteArrayToSensorData(packet.data)
            when (sensorData.type) {
                SENSOR_DATA -> {
                    world.setPlayerSpeed(packet.address, sensorData.x, sensorData.y)
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