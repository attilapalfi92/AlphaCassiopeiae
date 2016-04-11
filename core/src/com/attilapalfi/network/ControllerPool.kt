package com.attilapalfi.network

import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.controller.Controller
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by palfi on 2016-04-10.
 */
class ControllerPool(private val maxTcpClients: Int) : TcpConnectionEventListener {

    private val controllers: ConcurrentHashMap<Controller, Int> = ConcurrentHashMap()
    private val tcpConnections: ConcurrentHashMap<TcpConnection2, Int> = initTcpConnections()

    private val tcpSendingExecutor: ExecutorService = Executors.newFixedThreadPool(maxTcpClients)

    private var discoveryBroadcaster: UdpMessageBroadcaster = DiscoveryBroadcaster(
            Collections.synchronizedList(tcpConnections.map { it.key.serverPort }),
            UDP_PORT, maxTcpClients)

    private fun initTcpConnections(): ConcurrentHashMap<TcpConnection2, Int> {
        return ConcurrentHashMap<TcpConnection2, Int>().apply {
            for (i in 1..maxTcpClients) {
                put(TcpConnection2(this@ControllerPool).apply { start() }, 1)
            }
        }
    }

    override fun onConnect(tcpConnection2: TcpConnection2) {
        controllers.put(tcpConnection2.controller, 1)
        discoveryBroadcaster.clientConnected(tcpConnection2.serverPort)
    }

    override fun onDisconnect(tcpConnection2: TcpConnection2) {
        removeConnection(tcpConnection2)
        createNewConnection()
    }

    private fun removeConnection(tcpConnection: TcpConnection2) {
        tcpConnections.remove(tcpConnection)
        discoveryBroadcaster.clientDisconnected(tcpConnection.serverPort)
    }

    private fun createNewConnection() {
        val newConnection = TcpConnection2(this).apply { start() }
        tcpConnections.put(newConnection, 1)
        discoveryBroadcaster.addNewAvailablePort(newConnection.serverPort)
    }
}