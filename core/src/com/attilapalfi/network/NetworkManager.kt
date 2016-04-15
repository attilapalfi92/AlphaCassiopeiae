package com.attilapalfi.network

import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.controller.ControllerEventHandler

/**
 * Created by palfi on 2016-04-12.
 */
class NetworkManager(controllerEventHandler: ControllerEventHandler,
                     controllerConnectionListener: ControllerConnectionListener,
                     sensorDataListener: SensorDataListener) {

    private val sensorDataReceiver: UdpPacketReceiver
            = SensorDataReceiver(SensorDataProcessor(sensorDataListener))

    private val tcpConnectionPool: TcpConnectionPool
            = TcpConnectionPool(controllerEventHandler, controllerConnectionListener)

    fun startNetworking() {
        sensorDataReceiver.startReceiving()
        tcpConnectionPool.startBroadcasting()
    }
}
