package com.attilapalfi.network

import com.attilapalfi.commons.messages.UdpDiscoveryBroadcast
import com.attilapalfi.commons.utlis.ServerMessageConverter
import java.net.*
import java.util.*

/**
 * Created by palfi on 2016-01-11.
 */
class DiscoveryBroadcaster(private val port: Int, private val maxPlayers: Int) :
        UdpMessageBroadcaster {

    private val availableTcpPorts = Collections.synchronizedList(ArrayList<Int>());
    private val socket: DatagramSocket by lazy { DatagramSocket().apply { broadcast = true } }
    private val broadcastAddresses: List<InetAddress>
            = filterBroadcastAddresses(collectValidNetworkInterfaceAddresses())

    @Volatile
    private var connectedClients = 0
    @Volatile
    private var started = false

    private val messageConverter = ServerMessageConverter()

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
                        if (connectedClients < maxPlayers) {
                            sendDiscoveryBroadcast()
                        }
                        sleep()
                    }
                }
            }).start()
        }
    }

    private fun sendDiscoveryBroadcast() {
        if (connectedClients < maxPlayers) {
            broadcastAddresses.forEach {
                val broadcastMessage = messageConverter
                        .messageToByteArray(UdpDiscoveryBroadcast(availableTcpPorts))
                socket.send(DatagramPacket(broadcastMessage, broadcastMessage.size, it, port))
            }
        }
    }

    private fun sleep() {
        when (connectedClients) {
            0 -> Thread.sleep(100)
            else -> Thread.sleep(300)
        }
    }

    override fun clientConnected(serverPort: Int) {
        if (connectedClients < maxPlayers) {
            connectedClients++
        }
        availableTcpPorts.remove(serverPort)
    }

    override fun clientDisconnected(serverPort: Int) {
        if (connectedClients > 0) {
            connectedClients--;
        }
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