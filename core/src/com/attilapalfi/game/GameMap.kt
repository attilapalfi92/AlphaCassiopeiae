package com.attilapalfi.game

import com.attilapalfi.WORLD_HEIGHT
import com.attilapalfi.WORLD_WIDTH
import com.attilapalfi.game.entities.Attila
import com.attilapalfi.network.Client
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by 212461305 on 2016.03.17..
 */
class GameMap(private val players: ConcurrentHashMap<InetAddress, Client>) : Steppable, Renderable {

    var currentPosition: Float = 0f
    val preCalculationSize: Float = WORLD_WIDTH / 2

    var attilas: ConcurrentHashMap<Attila, Int> = ConcurrentHashMap()

    init {
        val initialAttilaCount = 20
        for (i in 0..initialAttilaCount - 1) {
            val x: Float = (Math.random().toFloat() * WORLD_WIDTH) + preCalculationSize
            val y: Float = (Math.random().toFloat() * WORLD_HEIGHT * 3 / 4) + (WORLD_HEIGHT / 4)
            attilas.put(Attila(x, y), 0)
        }
    }

    override fun step(cameraViewport: CameraViewport, deltaT: Long) {

        players.forEach { it.value.player.step(cameraViewport, deltaT) }
        attilas.forEach {
            if (it.key.posX < cameraViewport.left) {
                attilas.remove(it.key)
            }
        }

        attilas.forEach { it.key.step(cameraViewport, deltaT) }
    }

    override fun render() {
        players.forEach { it.value.player.render() }
        attilas.forEach { it.key.render() }
    }
}