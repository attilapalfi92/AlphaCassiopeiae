package com.attilapalfi.core

import com.attilapalfi.commons.UDP_PORT
import com.attilapalfi.commons.UdpPacketReceiver
import com.attilapalfi.commons.exceptions.NetworkException
import com.attilapalfi.game.WorldRenderer
import com.attilapalfi.logger.logError
import com.attilapalfi.network.*
import java.net.InetAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by 212461305 on 2016.02.10..
 */
class GameManager(private val maxTcpClients: Int) : TcpConnectionEventHandler, GameEventHandler {

    private var tcpConnections: ConcurrentHashMap<TcpConnection, Int> = initTcpConnections()

    private var discoveryBroadcaster: UdpMessageBroadcaster = DiscoveryBroadcaster(
            Collections.synchronizedList(tcpConnections.map { it.key.serverPort }),
            UDP_PORT, maxTcpClients)

    private val sensorDataReceiver: UdpPacketReceiver = SensorDataReceiver(SensorDataProcessor(this))

    private val tcpSendingExecutor: ExecutorService = Executors.newFixedThreadPool(maxTcpClients)

    lateinit var world: World
    lateinit var worldRenderer: WorldRenderer

    @Synchronized
    fun gameStartIsReceived(): Boolean = world.gameState == GameState.RUNNING

    fun startActualNewGame() {
        world.start()
    }

    fun startUdpCommunication() {
        startReceivingUdpPackets()
        startSendingDiscoveryBroadcast()
    }

    @Synchronized
    override fun onSetPlayerSpeed(address: InetAddress, x: Float, y: Float) {
        world.setPlayerSpeed(address, x, y)
    }

    @Synchronized
    fun initNewGame() {
        world = World()
        worldRenderer = world.renderer
    }

    @Synchronized
    override fun onGameStartReceived() {
        world.startReceived()
        sendStartAcksToClients()
    }

    override fun onPlayerJoined(ipAddress: InetAddress, client: Client) {
        world.addPlayer(ipAddress, client)
    }

    // it's a very unlikely scenario, no further propagation of the error
    override fun onTcpConnectionDeath(tcpConnection: TcpConnection) {
        removeConnection(tcpConnection)
        createNewConnection()
    }

    private fun removeConnection(tcpConnection: TcpConnection) {
        tcpConnections.remove(tcpConnection)
        tcpConnection.clientPort?.let {
            discoveryBroadcaster.clientDisconnected(it)
        }
    }

    private fun createNewConnection() {
        val newConnection = TcpConnection(this, this).apply { start() }
        tcpConnections.put(newConnection, 1)
        discoveryBroadcaster.addNewAvailablePort(newConnection.serverPort)
    }

    override fun clientConnected(client: Client) {
        discoveryBroadcaster.clientConnected(client.port)
    }

    fun sendStartAcksToClients() {
        val ackFutures: List<Future<out Any?>>
                = tcpConnections.filter { it.key.isConnected() }
                .map { tcpSendingExecutor.submit { it.key.sendStartAck() } }
        waitForFutures(ackFutures)
    }

    fun sendPauseAcksToClients() {
        throw UnsupportedOperationException()
    }

    fun sendResumeAcksToClients() {
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
        discoveryBroadcaster.startBroadcasting()
    }

    private fun initTcpConnections(): ConcurrentHashMap<TcpConnection, Int> {
        return ConcurrentHashMap<TcpConnection, Int>().apply {
            for (i in 1..maxTcpClients) {
                put(TcpConnection(this@GameManager, this@GameManager).apply { start() }, 1)
            }
        }
    }
}