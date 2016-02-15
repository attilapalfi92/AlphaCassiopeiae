package com.attilapalfi.network

import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.commons.UdpMessageBroadcaster
import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.commons.exceptions.NetworkException
import com.attilapalfi.game.World
import com.attilapalfi.logger.logError
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by 212461305 on 2016.02.10..
 */
class CommunicationManager(private val world: World, private val maxTcpClients: Int) : AckSender, TcpConnectionManager {

    private val discoveryBroadCaster: UdpMessageBroadcaster
            = DiscoveryBroadcaster(UDP_PORT, maxTcpClients)

    private var tcpConnectionHandlers: ConcurrentHashMap<TcpConnection, Int> = initTcpConnectionHandlers()

    private val tcpSendingExecutor: ExecutorService
            = Executors.newFixedThreadPool(maxTcpClients)

    private val sensorDataReceiver: UdpPacketReceiver
            = SensorDataReceiver(ServerUdpPacketProcessor(world))

    fun startUdpCommunication() {
        startReceivingUdpPackets()
        startSendingDiscoveryBroadcast()
    }

    override fun onTcpConnectionDeath(tcpConnection: TcpConnection) {
        tcpConnectionHandlers.remove(tcpConnection)
        // it's a very unlikely scenario, no further propagation of the error
    }

    @Synchronized
    override fun sendStartAcksToClients() {
        val ackFutures: List<Future<out Any?>>
                = tcpConnectionHandlers.filter { it.key.isConnected() }.map { tcpSendingExecutor.submit { it.key.sendStartAck() } }
        waitForFutures(ackFutures)
    }

    @Synchronized
    override fun sendPauseAcksToClients() {
        throw UnsupportedOperationException()
    }

    @Synchronized
    override fun sendResumeAcksToClients() {
        throw UnsupportedOperationException()
    }

    private fun waitForFutures(ackFutures: List<Future<out Any?>>) {
        try {
            ackFutures.forEach {
                it.get()
            }
        } catch (e: Exception) {
            handleError(e) // TODO: handle failure better
        }
    }

    private fun handleError(e: Exception) {
        val thrown = NetworkException("Error happened during sending START_ACK to clients.", e)
        logError("SignalProcessor", thrown.toString())
        throw thrown
    }

    private fun startReceivingUdpPackets() {
        sensorDataReceiver.startReceiving()
    }

    private fun startSendingDiscoveryBroadcast() {
        discoveryBroadCaster.startBroadcasting()
    }

    private fun restartBroadcasting() {
        discoveryBroadCaster.clientsCleared()
        discoveryBroadCaster.startBroadcasting()
    }

    private fun restartTcpServers() {
        tcpConnectionHandlers.forEach { it.key.disconnect() }
        tcpConnectionHandlers = initTcpConnectionHandlers()
    }

    private fun initTcpConnectionHandlers(): ConcurrentHashMap<TcpConnection, Int> {
        return ConcurrentHashMap<TcpConnection, Int>().apply {
            for (i in 1..maxTcpClients) {
                put(TcpConnection(world, discoveryBroadCaster,
                        this@CommunicationManager, this@CommunicationManager).apply { start() }, 1)
            }
        }
    }
}