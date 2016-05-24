package com.attilapalfi.network

import com.attilapalfi.commons.CommonUdpPacketReceiver
import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.controller.ControllerInputHandler
import com.attilapalfi.logger.logError

/**
 * Created by palfi on 2016-04-12.
 */
class NetworkManager(controllerInputHandler: ControllerInputHandler,
                     controllerConnectionListener: ControllerConnectionListener,
                     sensorDataListener: SensorDataListener) {

    private val maxTcpClients = 4;

    private val sensorDataReceiver: UdpPacketReceiver
            = CommonUdpPacketReceiver(SensorDataProcessor(sensorDataListener),
            { e -> logError("UdpReceiver", e.message ?: "null") });

    private val discoveryBroadcaster = DiscoveryBroadcaster(UDP_PORT, maxTcpClients);

    private val tcpConnectionPool: TcpConnectionPool = TcpConnectionPool(maxTcpClients,
            discoveryBroadcaster, controllerInputHandler, controllerConnectionListener);

    fun startNetworking() {
        sensorDataReceiver.startReceiving();
        discoveryBroadcaster.startBroadcasting();
        tcpConnectionPool.initPool();
    }
}
