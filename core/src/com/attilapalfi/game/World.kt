package com.attilapalfi.game

import com.attilapalfi.network.Client
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-11.
 */
class World {
    @Volatile
    public var gameState: GameState = GameState.WAITING_FOR_PLAYER
        @Synchronized
        set
        @Synchronized
        get

    @Volatile
    private var running = true
    private var lastStepTime: Long = 0L
    private var players: Map<Client, Player> = ConcurrentHashMap(11);

    fun setPlayerSpeed(client: Client, speedX: Int, speedY: Int) {
        players?.let {
            it[client]?.let {
                it.speedX = speedX
                it.speedY = speedY
            }
        }
    }

    fun start(players: Map<Client, Player>) {
        this.players = players
        Thread({
            while (running) {
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
                        running = false
                    }
                }
            }
        }).start()
    }

    private fun step() {
        if (lastStepTime != 0L) {
            val deltaTime = System.currentTimeMillis() - lastStepTime

            players?.forEach {
                // step
            }

        }
        lastStepTime = System.currentTimeMillis()
    }

    fun render() {

    }

    fun playerCount(): Int = players?.size ?: 0
}