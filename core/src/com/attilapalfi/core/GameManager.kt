package com.attilapalfi.core

import com.attilapalfi.controller.Controller
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.game.entities.Player
import com.attilapalfi.network.NetworkManager
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by 212461305 on 2016.02.10..
 */
class GameManager : ControllerConnectionListener {

    var world: World = World().apply { start() }
    var worldRenderer = world.renderer
    private val controllers: ConcurrentHashMap<Controller, Int> = ConcurrentHashMap()
    private val networkManager = NetworkManager(world, this, world)

    init {
        networkManager.startNetworking()
    }

    @Synchronized
    fun gameStartIsReceived(): Boolean = world.gameState == GameState.RUNNING

    fun startNewGame() {
        world.startNewGame()
    }

    fun players() = world.getPlayers()

    override fun controllerConnected(controller: Controller) {
        controllers.put(controller, 1)
        world.addPlayer(controller)
    }

    override fun controllerDisconnected(controller: Controller) {
        controllers.remove(controller)
        world.removePlayer(controller)
    }
}