package com.attilapalfi.network

import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.controller.ControllerConnectionListener

/**
 * Created by palfi on 2016-04-12.
 */
class NetworkManager(controllerConnectionListener: ControllerConnectionListener,
                     sensorDataDistributor: SensorDataDistributor) {

    private val sensorDataReceiver: UdpPacketReceiver
            = SensorDataReceiver(SensorDataProcessor(sensorDataDistributor))

    private val tcpConnectionPool: TcpConnectionPool
            = TcpConnectionPool(controllerConnectionListener)

    init {
        sensorDataReceiver.startReceiving()
    }
}