package com.attilapalfi.logic

import com.attilapalfi.game.GameMap
import com.attilapalfi.game.WorldRenderer
import com.attilapalfi.network.Client
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import java.util.stream.Stream

/**
 * Created by palfi on 2016-01-11.
 */
class World(private val lock: ReentrantLock) {

    @Volatile
    var gameState: GameState = GameState.WAITING_FOR_PLAYER

    val renderer: WorldRenderer = WorldRenderer(this, lock)

    @Volatile
    private var threadIsRunning = true
    private var lastStepTime: Long = 0L

    private val players: ConcurrentHashMap<InetAddress, Client> = ConcurrentHashMap(11);
    private val map = GameMap(lock)

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
            lock.lock()
            val deltaTime = System.currentTimeMillis() - lastStepTime

            for(player in players) {
                player.value.player.step(renderer.camera.position.x, deltaTime)
            }

            map.step(players, renderer.camera.position.x, deltaTime)
            lock.unlock()
        }
        lastStepTime = System.currentTimeMillis()
    }

    fun render() {

    }

    fun playerCount(): Int = players.size
}