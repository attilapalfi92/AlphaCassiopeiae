package com.attilapalfi.network

import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.controller.ControllerInputHandler
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-04-10.
 */
class TcpConnectionPool(private val maxTcpClients: Int,
                        private val udpMessageBroadcaster: UdpMessageBroadcaster,
                        private val controllerInputHandler: ControllerInputHandler,
                        private val controllerConnectionListener: ControllerConnectionListener) :
        TcpConnectionEventListener {

    private val tcpConnections: ConcurrentHashMap<TcpConnection, Int> = ConcurrentHashMap();

    fun initPool() {
        for (i in 1..maxTcpClients) {
            val tcpConnection = TcpConnection(controllerInputHandler, this@TcpConnectionPool)
                    .apply { start(); };
            udpMessageBroadcaster.addNewAvailablePort(tcpConnection.serverPort);
            tcpConnections.put(tcpConnection, 1);
        }
    }

    override fun onTcpConnected(tcpConnection: TcpConnection) {
        controllerConnectionListener.controllerConnected(tcpConnection.controller);
        udpMessageBroadcaster.clientConnected(tcpConnection.serverPort);
    }

    override fun onTcpDisconnect(tcpConnection: TcpConnection) {
        removeConnection(tcpConnection);
        createNewConnection();
    }

    private fun removeConnection(tcpConnection: TcpConnection) {
        tcpConnections.remove(tcpConnection);
        udpMessageBroadcaster.clientDisconnected(tcpConnection.serverPort);
    }

    private fun createNewConnection() {
        val newConnection = TcpConnection(controllerInputHandler, this).apply { start(); };
        tcpConnections.put(newConnection, 1);
        udpMessageBroadcaster.addNewAvailablePort(newConnection.serverPort);
    }
}