package com.attilapalfi.network

import com.attilapalfi.commons.CommonUdpPacketReceiver
import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.controller.ControllerEventHandler
import com.attilapalfi.logger.logError

/**
 * Created by palfi on 2016-04-12.
 */
class NetworkManager(controllerEventHandler: ControllerEventHandler,
                     controllerConnectionListener: ControllerConnectionListener,
                     sensorDataListener: SensorDataListener) {

    private val sensorDataReceiver: UdpPacketReceiver
            = CommonUdpPacketReceiver(SensorDataProcessor(sensorDataListener),
            { e -> logError("UdpReceiver", e.message ?: "null") })

    private val tcpConnectionPool: TcpConnectionPool
            = TcpConnectionPool(controllerEventHandler, controllerConnectionListener)

    fun startNetworking() {
        sensorDataReceiver.startReceiving()
        tcpConnectionPool.startBroadcasting()
    }
}
