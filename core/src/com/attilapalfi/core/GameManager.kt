package com.attilapalfi.core

import com.attilapalfi.controller.Controller
import com.attilapalfi.controller.ControllerConnectionListener
import com.attilapalfi.game.WorldRenderer
import com.attilapalfi.network.NetworkManager
import com.attilapalfi.network.SensorDataDistributor
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by 212461305 on 2016.02.10..
 */
class GameManager : ControllerConnectionListener, SensorDataDistributor {

    lateinit var world: World
    lateinit var worldRenderer: WorldRenderer
    private val controllers: ConcurrentHashMap<Controller, Int> = ConcurrentHashMap()
    private val networkManager = NetworkManager(this, this)

    @Synchronized
    fun gameStartIsReceived(): Boolean = world.gameState == GameState.RUNNING

    fun startActualNewGame() {
        world.start()
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

    override fun controllerConnected(controller: Controller) {
        controllers.put(controller, 1)
        world.addPlayer(controller)
    }

    override fun controllerDisconnected(controller: Controller) {
        controllers.remove(controller)
    }
}