package com.attilapalfi.network

import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.controller.ControllerEventHandler
import com.attilapalfi.core.World
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-04-10.
 */
class TcpConnectionPool(private val controllerEventHandler: ControllerEventHandler,
                        private val controllerConnectionListener: ControllerConnectionListener) :
        TcpConnectionEventListener {

    private val maxTcpClients: Int = 4
    private val tcpConnections: ConcurrentHashMap<TcpConnection, Int> = initTcpConnections()

    private var discoveryBroadcaster: UdpMessageBroadcaster = DiscoveryBroadcaster(
            Collections.synchronizedList(tcpConnections.map { it.key.serverPort }),
            UDP_PORT, maxTcpClients)

    private fun initTcpConnections(): ConcurrentHashMap<TcpConnection, Int> {
        return ConcurrentHashMap<TcpConnection, Int>().apply {
            for (i in 1..maxTcpClients) {
                put(TcpConnection(controllerEventHandler, this@TcpConnectionPool).apply { start() }, 1)
            }
        }
    }

    fun startBroadcasting() {
        discoveryBroadcaster.startBroadcasting()
    }

    override fun onConnect(tcpConnection: TcpConnection) {
        controllerConnectionListener.controllerConnected(tcpConnection.controller)
        discoveryBroadcaster.clientConnected(tcpConnection.serverPort)
    }

    override fun onDisconnect(tcpConnection: TcpConnection) {
        removeConnection(tcpConnection)
        createNewConnection()
    }

    private fun removeConnection(tcpConnection: TcpConnection) {
        tcpConnections.remove(tcpConnection)
        discoveryBroadcaster.clientDisconnected(tcpConnection.serverPort)
    }

    private fun createNewConnection() {
        val newConnection = TcpConnection(controllerEventHandler, this).apply { start() }
        tcpConnections.put(newConnection, 1)
        discoveryBroadcaster.addNewAvailablePort(newConnection.serverPort)
    }
}