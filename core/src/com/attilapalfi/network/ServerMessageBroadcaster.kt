package com.attilapalfi.network

import com.attilapalfi.common.messages.DISCOVERY_BROADCAST
import com.attilapalfi.common.messages.ServerMessage
import java.net.*

/**
 * Created by palfi on 2016-01-11.
 */
class ServerMessageBroadcaster(private val port: Int, private val maxPlayers: Int) :
        MessageBroadcaster {

    private val socket: DatagramSocket by lazy { DatagramSocket().apply { broadcast = true } }
    private val broadcastMessage: ByteArray

    private val broadcastAddresses: List<InetAddress> = filterBroadcastAddresses(collectValidNetworkInterfaceAddresses())

    @Volatile
    private var connectedClients = 0;
    @Volatile
    private var started = false;

    init {
        if (maxPlayers < 1) {
            throw IllegalStateException("maxPlayers must be at least 1.")
        }
        this.broadcastMessage = Converter.messageToByteArray(ServerMessage(DISCOVERY_BROADCAST))
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
                socket.send(DatagramPacket(broadcastMessage, broadcastMessage.size, it, port))
            }
        }
    }

    private fun sleep() {
        when (connectedClients) {
            0 -> Thread.sleep(100)
            else -> Thread.sleep(1000)
        }
    }

    @Synchronized
    override fun clientConnected() {
        if (connectedClients < maxPlayers) {
            connectedClients++
        }
    }

    @Synchronized
    override fun clientDisconnected() {
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