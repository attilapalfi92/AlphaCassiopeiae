package com.attilapalfi.core

import com.attilapalfi.CAMERA_HEIGHT
import com.attilapalfi.CAMERA_WIDTH
import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.GameMap
import com.attilapalfi.game.WorldRenderer
import com.attilapalfi.network.Client
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-11.
 */
class World() {

    @Volatile
    public var gameState: GameState = GameState.WAITING_FOR_PLAYER
        private set

    @Volatile
    private var threadIsRunning = true
    private var lastStepTime: Long = 0L

    private val players: ConcurrentHashMap<InetAddress, Client> = ConcurrentHashMap(11);
    private val map = GameMap(players)

    val renderer: WorldRenderer = WorldRenderer(map)

    fun setPlayerSpeed(address: InetAddress, speedX: Float, speedY: Float) {
        players[address]?.let {
            it.player.speedX = speedX
            it.player.speedY = speedY
        }
    }

    fun addPlayer(address: InetAddress, client: Client) {
        if (gameState == GameState.WAITING_FOR_PLAYER) {
            gameState = GameState.WAITING_FOR_START
        }
        players.put(address, client)
    }

    fun startReceived() {
        if (gameState == GameState.WAITING_FOR_START) {
            gameState = GameState.RUNNING
        }
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
                        Thread.sleep(10)
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
            val viewport = CameraViewport(renderer.camera.position.x - (CAMERA_WIDTH / 2),
                    renderer.camera.position.y + (CAMERA_HEIGHT / 2),
                    renderer.camera.position.x + (CAMERA_WIDTH / 2),
                    renderer.camera.position.y - (CAMERA_HEIGHT / 2))
            map.step(viewport, deltaTime)
        }
        lastStepTime = System.currentTimeMillis()
    }

    fun playerCount(): Int = players.size
}