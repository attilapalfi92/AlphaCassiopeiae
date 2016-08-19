package com.attilapalfi.game.levels

import com.attilapalfi.CAMERA_HEIGHT
import com.attilapalfi.CAMERA_WIDTH
import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.entities.Attila
import com.attilapalfi.game.entities.Player
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Disposable
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue

class Level1Map(players: ConcurrentHashMap<InetAddress, Player>) : AbstractMap(players) {

    var currentPosition: Float = 0f
    val preCalculationSize: Float = CAMERA_WIDTH / 2

    var attilas: ConcurrentHashMap<Attila, Int> = ConcurrentHashMap()
    var disposedEntities = LinkedBlockingQueue<Disposable>()

    init {
        val initialAttilaCount = 20
        for (i in 0..initialAttilaCount - 1) {
            val x: Float = (Math.random().toFloat() * CAMERA_WIDTH) //+ preCalculationSize
            val y: Float = (Math.random().toFloat() * CAMERA_HEIGHT * 3 / 4) + (CAMERA_HEIGHT / 4) - 2
            attilas.put(Attila(x, y), 0)
        }
    }

    override fun step(cameraViewport: CameraViewport, deltaT: Long) {

        players.forEach { it.value.step(cameraViewport, deltaT) }
        attilas.forEach {
            if (it.key.posX < cameraViewport.left) {
                disposedEntities.offer(it.key)
                attilas.remove(it.key)
            }
        }

        attilas.forEach { it.key.step(cameraViewport, deltaT) }
    }

    override fun render(projAndViewMatrix: Matrix4) {
        var toBeDisposed = disposedEntities.poll()
        while (toBeDisposed != null) {
            toBeDisposed.dispose()
            toBeDisposed = disposedEntities.poll()
        }

        players.forEach { it.value.render(projAndViewMatrix) }
        attilas.forEach { it.key.render(projAndViewMatrix) }
    }

    override fun dispose() {
        players.forEach { it.value.dispose() }
        attilas.forEach { it.key.dispose() }
    }
}