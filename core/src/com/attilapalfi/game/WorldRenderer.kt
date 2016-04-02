package com.attilapalfi.game

import com.attilapalfi.WORLD_HEIGHT
import com.attilapalfi.WORLD_WIDTH
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3

/**
 * Created by 212461305 on 2016.03.16..
 */
class WorldRenderer(private val map: GameMap) {

    val camera: OrthographicCamera = OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT).apply {
        position.set(WORLD_WIDTH / 6, WORLD_HEIGHT / 2, 0f)
    }

    private val cameraSpeed: Float = 2f

    fun render(deltaT: Float) {
        adjustCameraPos(deltaT)
        map.render()
    }

    fun adjustCameraPos(deltaT: Float) {
        val newPosition = camera.position.add(cameraSpeed * deltaT)
        camera.position.lerp(newPosition, deltaT)
    }

    fun getCameraPos(): Vector3 {
        return camera.position
    }
}
