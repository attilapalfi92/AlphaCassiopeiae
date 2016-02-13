package com.attilapalfi.network

import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.commons.UdpMessageBroadcaster
import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.commons.exceptions.NetworkException
import com.attilapalfi.game.GameState
import com.attilapalfi.game.World
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by 212461305 on 2016.02.10..
 */
class CommunicationManager(private val world: World, private val maxTcpClients: Int) {

    private var tcpConnectionHandlers: List<TcpConnectionHandler> = initTcpConnectionHandlers()

    private val tcpSendingExecutor: ExecutorService
            = Executors.newFixedThreadPool(maxTcpClients)

    private val discoveryBroadCaster: UdpMessageBroadcaster
            = DiscoveryBroadcaster(UDP_PORT, maxTcpClients)

    private val sensorDataReceiver: UdpPacketReceiver
            = SensorDataReceiver(ServerUdpPacketProcessor(world))

    fun startUdpCommunication() {
        startReceivingUdpPackets()
        startSendingDiscoveryBroadcast()
    }

    private fun startReceivingUdpPackets() {
        sensorDataReceiver.startReceiving()
    }

    private fun startSendingDiscoveryBroadcast() {
        discoveryBroadCaster.startBroadcasting()
    }

    @Synchronized
    fun gameStateChanged(gameState: GameState) {
        when (gameState) {
            GameState.WAITING_FOR_PLAYER -> {
                restartBroadcasting()
                restartTcpServers()
            }
            GameState.WAITING_FOR_START -> {

            }
        }
    }

    @Synchronized
    fun sendStartAcksToClients() {
        val ackFutures: List<Future<out Any?>>
                = tcpConnectionHandlers.filter { it.isConnected() }.map { tcpSendingExecutor.submit { it.sendStartAck() } }

        waitForFutures(ackFutures)
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
        Gdx.app.logLevel = Application.LOG_ERROR
        val thrown = NetworkException("Error happened during sending START_ACK to clients.", e)
        Gdx.app.log("SignalProcessor", thrown.toString())
        throw thrown
    }

    private fun restartBroadcasting() {
        discoveryBroadCaster.clientsCleared()
        discoveryBroadCaster.startBroadcasting()
    }

    private fun restartTcpServers() {
        tcpConnectionHandlers.forEach { it.interrupt() }
        tcpConnectionHandlers = initTcpConnectionHandlers()
    }

    private fun initTcpConnectionHandlers(): List<TcpConnectionHandler> {
        return ArrayList<TcpConnectionHandler>().apply {
            for (i in 1..maxTcpClients) {
                add(TcpConnectionHandler(world, discoveryBroadCaster, this@CommunicationManager).apply { start() })
            }
        }
    }
}