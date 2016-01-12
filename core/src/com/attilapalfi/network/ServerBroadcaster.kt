package com.attilapalfi.network

import java.net.*

/**
 * Created by palfi on 2016-01-11.
 */
class ServerBroadcaster(private val port: Int, private val broadcastMessage: ByteArray) {
    private val socket: DatagramSocket = DatagramSocket().apply { broadcast = true }
    private val broadcastAddresses: List<InetAddress> = filterBroadcastAddresses(collectValidNetworkInterfaceAddresses())
    @Volatile
    private var connectedClients = 0;
    @Volatile
    private var started = false;

    @Synchronized
    fun startBroadcasting() {
        if (!started) {
            started = true
            Thread({
                while (true) {
                    sendDiscoveryBroadcast()
                    sleep()
                }
            }).start()
        }
    }

    private fun sendDiscoveryBroadcast() {
        if (connectedClients < 2) {
            broadcastAddresses.forEach {
                socket.send(DatagramPacket(broadcastMessage, broadcastMessage.size, it, port))
            }
        }
    }

    private fun sleep() {
        when (connectedClients) {
            0 -> Thread.sleep(100)
            else -> Thread.sleep(500)
        }
    }

    @Synchronized
    fun clientConnected() {
        if (connectedClients < 2) {
            connectedClients++
        }
    }

    @Synchronized
    fun clientDisconnected() {
        if (connectedClients > 0) {
            connectedClients--;
        }
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