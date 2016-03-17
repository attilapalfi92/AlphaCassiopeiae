package com.attilapalfi.logic

import com.attilapalfi.network.Client
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-11.
 */
class World {

    @Volatile
    var gameState: GameState = GameState.WAITING_FOR_PLAYER
    @Volatile
    private var threadIsRunning = true
    private var lastStepTime: Long = 0L
    private var players: MutableMap<InetAddress, Client> = ConcurrentHashMap(11);

    fun setPlayerSpeed(address: InetAddress, speedX: Float, speedY: Float) {
        players[address]?.let {
            it.player.speedX = speedX
            it.player.speedY = speedY
        }
    }

    fun addPlayer(address: InetAddress, client: Client) {
        players.put(address, client)
    }

    fun init() {

    }

    fun start() {
        Thread({
            while (threadIsRunning) {
                when (gameState) {
                    GameState.WAITING_FOR_PLAYER -> {
                        Thread.sleep(100)
                    }
                    GameState.WAITING_FOR_START -> {
                        Thread.sleep(100)
                    }
                    GameState.RUNNING -> {
                        step()
                    }
                    GameState.PAUSED -> {
                        Thread.sleep(10)
                    }
                    GameState.OVER -> {
                        threadIsRunning = false
                    }
                }
            }
        }).start()
    }

    private fun step() {
        if (lastStepTime != 0L) {
            val deltaTime = System.currentTimeMillis() - lastStepTime

            players.forEach {
                // step
            }

        }
        lastStepTime = System.currentTimeMillis()
    }

    fun render() {

    }

    fun playerCount(): Int = players.size
}