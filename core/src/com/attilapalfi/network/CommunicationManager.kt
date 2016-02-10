package com.attilapalfi.network

import com.attilapalfi.common.PORT
import com.attilapalfi.game.GameState
import java.util.*

/**
 * Created by 212461305 on 2016.02.10..
 */
class CommunicationManager(maxTcpClients: Int) {

    private val tcpServers: List<TcpServer> = ArrayList<TcpServer>().apply {
        for (i in 1..maxTcpClients) {
            add(TcpServer())
        }
    }

    private val discoveryBroadCaster: MessageBroadcaster = DiscoveryBroadcaster(PORT, maxTcpClients)

    fun gameStateChanged(gameState: GameState) {
        when (gameState) {
            GameState.WAITING_FOR_PLAYER -> {

            }
            GameState.WAITING_FOR_START -> {

            }
        }
    }
}