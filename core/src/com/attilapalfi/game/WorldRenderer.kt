package com.attilapalfi.game

import com.attilapalfi.WORLD_HEIGHT
import com.attilapalfi.WORLD_WIDTH
import com.attilapalfi.logic.World
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by 212461305 on 2016.03.16..
 */
class WorldRenderer(private val world: World, private val lock: ReentrantLock) {

    val camera: OrthographicCamera = OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT).apply {
        position.set(WORLD_WIDTH / 6, WORLD_HEIGHT / 2, 0f)
    }

    private val cameraSpeed: Float = 4f

    fun render(deltaT: Float) {
        adjustCameraPos(deltaT)

    }

    fun adjustCameraPos(deltaT: Float) {
        lock.lock()
        val newPosition = camera.position.add(cameraSpeed * deltaT)
        camera.position.lerp(newPosition, deltaT)
        lock.unlock()
    }

    fun getCameraPos(): Vector3 {
        try {
            lock.lock()
            return camera.position
        } finally {
            lock.unlock()
        }
    }
}
