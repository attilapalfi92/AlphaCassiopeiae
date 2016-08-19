package com.attilapalfi.core

import com.attilapalfi.CAMERA_HEIGHT
import com.attilapalfi.CAMERA_WIDTH
import com.attilapalfi.commons.messages.UdpSensorData
import com.attilapalfi.controller.Controller
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.controller.ControllerInputHandler
import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.Renderer
import com.attilapalfi.game.entities.Player
import com.attilapalfi.game.levels.Level1Map
import com.attilapalfi.network.NetworkManager
import com.attilapalfi.network.SensorDataListener
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

/**
 * Created by 212461305 on 2016.02.10..
 */
class GameManager : ControllerConnectionListener, ControllerInputHandler, SensorDataListener {

    var renderer = Renderer();
    private val controllers: ConcurrentHashMap<Controller, Int> = ConcurrentHashMap();
    private val networkManager = NetworkManager(this, this, this);

    @Volatile
    var gameState: GameState = GameState.WAITING_FOR_PLAYER;
        private set;

    private var lastStepTime: Long = 0L;
    private val players: ConcurrentHashMap<InetAddress, Player> = ConcurrentHashMap(11);
    private var map: Level1Map? = null;

    private val logicExecutor = Executors.newSingleThreadExecutor();

    init {
        networkManager.startNetworking()
    }

    @Synchronized
    fun gameStartIsReceived(): Boolean = gameState == GameState.RUNNING

    fun startNewGame() {
        players.values.forEach {
            it.controller.startSensorDataStream();
        };
        map = Level1Map(players).apply {
            renderer.startNewGame(this);
        };
        startThread();
    }

    private fun startThread() {
        logicExecutor.submit {
            while (!Thread.currentThread().isInterrupted) {
                when (gameState) {
                    GameState.WAITING_FOR_PLAYER -> {
                        Thread.sleep(100);
                    }
                    GameState.WAITING_FOR_START -> {
                        Thread.sleep(100);
                    }
                    GameState.RUNNING -> {
                        Thread.sleep(10);
                        step();
                    }
                    GameState.PAUSED -> {
                        Thread.sleep(10);
                    }
                    GameState.OVER -> {
                        Thread.currentThread().interrupt();
                    }
                    GameState.EXITED -> {
                        System.exit(0);
                    }
                    else -> {
                    }
                }
            }
        }
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

    fun players() = players;

    override fun controllerConnected(controller: Controller) {
        controllers.put(controller, 1);
        if (gameState == GameState.WAITING_FOR_PLAYER) {
            gameState = GameState.WAITING_FOR_START;
        }
        controller.address?.let {
            players.put(it, Player(controller, 0));
        };
    }

    override fun controllerDisconnected(controller: Controller) {
        controllers.remove(controller);
        players.remove(controller.address);
        if (players.isEmpty()) {
            when (gameState) {
                GameState.WAITING_FOR_START -> {
                    gameState = GameState.WAITING_FOR_PLAYER;
                }
                GameState.RUNNING -> {
                    gameState = GameState.PAUSED;
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
    }

    override fun onBpressed(controller: Controller) {
        when (gameState) {
            GameState.WAITING_FOR_START -> {
                gameState = GameState.STARTED
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
                // TODO: FIRE WITH WEAPON 2
            }
            else -> {
            }
        }
    }

    override fun updatePlayerData(address: InetAddress, playerData: UdpSensorData) {
        throw UnsupportedOperationException()
    }
}