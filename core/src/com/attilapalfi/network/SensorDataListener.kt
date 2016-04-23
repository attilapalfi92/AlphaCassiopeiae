package com.attilapalfi.network

import com.attilapalfi.commons.messages.UdpSensorData
import java.net.InetAddress

/**
 * Created by palfi on 2016-04-12.
 */
interface SensorDataListener {
    fun updatePlayerData(address: InetAddress, playerData: UdpSensorData)
}