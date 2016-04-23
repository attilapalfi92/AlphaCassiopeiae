package com.attilapalfi.core

import com.attilapalfi.CAMERA_HEIGHT
import com.attilapalfi.CAMERA_WIDTH
import com.attilapalfi.commons.messages.PressedButton
import com.attilapalfi.commons.messages.PressedButton.*
import com.attilapalfi.commons.messages.UdpSensorData
import com.attilapalfi.controller.Controller
import com.attilapalfi.controller.ControllerEventHandler
import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.GameMap
import com.attilapalfi.game.WorldRenderer
import com.attilapalfi.game.entities.Player
import com.attilapalfi.network.SensorDataListener
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-11.
 */
class World : ControllerEventHandler, SensorDataListener {

    @Volatile
    var gameState: GameState = GameState.WAITING_FOR_PLAYER
        private set

    @Volatile
    private var threadIsRunning = true
    private var lastStepTime: Long = 0L

    private val players: ConcurrentHashMap<InetAddress, Player> = ConcurrentHashMap(11);

    private var map: GameMap? = null
    val renderer: WorldRenderer = WorldRenderer()

    fun startNewGame() {
        players.values.forEach {
            it.controller.startSensorDataStream()
        }
        map = GameMap(players).apply {
            renderer.startNewGame(this)
        }
    }

    fun getPlayers() = players.map { it.value }

    override fun updatePlayerData(address: InetAddress, playerData: UdpSensorData) {
        val time = System.currentTimeMillis()
        players[address]?.let {
            val player = it
            player.speedX = playerData.x
            player.speedY = playerData.y
            playerData.pressedButtons.forEach {
                when (it) {
                    A -> {
                        onApressed(player.controller)
                    }
                    X -> {
                        onXpressed(player.controller)
                    }
                    Y -> {
                        onYpressed(player.controller)
                    }
                    else -> {}
                }
                player.buttonsToLastPressTimes.put(it, time)
            }
        }
    }

    fun addPlayer(controller: Controller) {
        if (gameState == GameState.WAITING_FOR_PLAYER) {
            gameState = GameState.WAITING_FOR_START
        }
        controller.address?.let {
            players.put(it, Player(controller, 0))
        }
    }

    fun removePlayer(controller: Controller) {
        players.remove(controller.address)
        if (players.isEmpty()) {
            when (gameState) {
                GameState.WAITING_FOR_START -> {
                    gameState = GameState.WAITING_FOR_PLAYER
                }
                GameState.RUNNING -> {
                    gameState = GameState.PAUSED
                }
                else -> {
                }
            }
        }
    }

    override fun onApressed(controller: Controller) {
        when (gameState) {
            GameState.WAITING_FOR_START -> {
                gameState = GameState.EXITED
            }
            GameState.RUNNING -> {
                // TODO: DROP BOMB
            }
            GameState.PAUSED -> {
                gameState = GameState.EXITED
            }
            GameState.OVER -> {
                gameState = GameState.EXITED
            }
            else -> {
            }
        }
        throw UnsupportedOperationException()
    }

    override fun onBpressed(controller: Controller) {
        when (gameState) {
            GameState.WAITING_FOR_START -> {
                gameState = GameState.RUNNING
            }
            GameState.RUNNING -> {
                gameState = GameState.PAUSED
            }
            GameState.PAUSED -> {
                gameState = GameState.RUNNING
            }
            GameState.OVER -> {
                startNewGame()
                gameState = GameState.RUNNING
            }
            else -> {
            }
        }
    }

    override fun onXpressed(controller: Controller) {
        when (gameState) {
            GameState.RUNNING -> {
                // TODO: FIRE WITH WEAPON 1
            }
            else -> {
            }
        }
    }

    override fun onYpressed(controller: Controller) {
        when (gameState) {
            GameState.RUNNING -> {
                // TODO: FIRE WITH WEAPON 1
            }
            else -> {
            }
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
                    GameState.EXITED -> {
                        System.exit(0)
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
            map?.step(viewport, deltaTime)
        }
        lastStepTime = System.currentTimeMillis()
    }

    fun playerCount(): Int = players.size
}