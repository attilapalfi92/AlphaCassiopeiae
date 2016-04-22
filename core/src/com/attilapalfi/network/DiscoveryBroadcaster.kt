package com.attilapalfi.network

import com.attilapalfi.commons.messages.UdpDiscoveryBroadcast
import com.attilapalfi.commons.utlis.ServerMessageConverter
import java.net.*

/**
 * Created by palfi on 2016-01-11.
 */
class DiscoveryBroadcaster(private val availableTcpPorts: MutableList<Int>,
                           private val port: Int, private val maxPlayers: Int) :
        UdpMessageBroadcaster {

    private val socket: DatagramSocket by lazy { DatagramSocket().apply { broadcast = true } }
    private val broadcastAddresses: List<InetAddress>
            = filterBroadcastAddresses(collectValidNetworkInterfaceAddresses())

    @Volatile
    private var connectedClients = 0
    @Volatile
    private var started = false

    init {
        if (maxPlayers < 1) {
            throw IllegalStateException("maxPlayers must be at least 1.")
        }
    }

    @Synchronized
    override fun startBroadcasting() {
        if (!started) {
            started = true
            Thread({
                socket.use {
                    while (true) {
                        sendDiscoveryBroadcast()
                        sleep()
                    }
                }
            }).start()
        }
    }

    private fun sendDiscoveryBroadcast() {
        if (connectedClients < maxPlayers) {
            broadcastAddresses.forEach {
                val broadcastMessage = ServerMessageConverter
                        .udpDiscoveryToByteArray(UdpDiscoveryBroadcast(availableTcpPorts))
                socket.send(DatagramPacket(broadcastMessage, broadcastMessage.size, it, port))
            }
        }
    }

    private fun sleep() {
        when (connectedClients) {
            0 -> Thread.sleep(100)
            1 -> Thread.sleep(300)
            2 -> Thread.sleep(300)
            3 -> Thread.sleep(300)
            else -> Thread.sleep(1000)
        }
    }

    @Synchronized
    override fun clientConnected(serverPort: Int) {
        if (connectedClients < maxPlayers) {
            connectedClients++
        }
        availableTcpPorts.remove(serverPort)
    }

    @Synchronized
    override fun clientDisconnected(serverPort: Int) {
        if (connectedClients > 0) {
            connectedClients--;
        }
    }

    @Synchronized
    override fun clientsCleared() {
        connectedClients = 0
    }

    override fun addNewAvailablePort(serverPort: Int) {
        availableTcpPorts.add(serverPort)
    }

    private fun filterBroadcastAddresses(interfaceAddresses: List<InterfaceAddress>): List<InetAddress> {
        return interfaceAddresses
                .filter { address -> address.broadcast != null }
                .map { it.broadcast }
    }

    private fun collectValidNetworkInterfaceAddresses(): List<InterfaceAddress> {
        return NetworkInterface.getNetworkInterfaces().toList()
                .filter { validInterface(it) }
                .flatMap { nInterface -> nInterface.interfaceAddresses }
    }

    private fun validInterface(nInterface: NetworkInterface): Boolean {
        if (nInterface.isLoopback || !nInterface.isUp) {
            return false
        }
        return true
    }
}