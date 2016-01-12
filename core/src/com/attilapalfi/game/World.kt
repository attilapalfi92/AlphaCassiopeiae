package com.attilapalfi.game

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
    private val players: MutableMap<String, Player> = ConcurrentHashMap()

    fun addPlayer(player: Player) {
        players.put(player.androidId, player)
    }

    fun setPlayerSpeed(androidId: String, speedX: Int, speedY: Int) {
        players[androidId]?.let {
            it.speedX = speedX
            it.speedY = speedY
        }
    }

    fun start() {
        Thread({
            while (running) {
                when (gameState) {
                    GameState.WAITING_FOR_PLAYER -> {
                        Thread.sleep(100)
                    }
                    GameState.WAITING_FOR_START -> {
                        Thread.sleep(100)
                    }
                    GameState.STARTED -> {
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

            players.forEach {
                // draw
            }

        }
        lastStepTime = System.currentTimeMillis()
    }

}