package com.attilapalfi.game

import com.attilapalfi.CAMERA_HEIGHT
import com.attilapalfi.CAMERA_WIDTH
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3

/**
 * Created by 212461305 on 2016.03.16..
 */
class WorldRenderer(private val map: GameMap) {

    val camera: OrthographicCamera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT).apply {
        position.set(CAMERA_WIDTH / 6, CAMERA_HEIGHT / 2, 0f)
    }

    private val cameraSpeed: Float = 0.2f

    init {
        camera.update()
    }

    fun render(deltaT: Float) {
        adjustCameraPos(deltaT)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        map.render(camera.combined)
    }

    fun adjustCameraPos(deltaT: Float) {
        //        camera.translate(cameraSpeed * deltaT, 0f)
        val newPosition = camera.position.add(cameraSpeed * deltaT, 0f, 0f)
        camera.position.lerp(newPosition, deltaT)
        camera.update()
    }

    fun getCameraPos(): Vector3 {
        return camera.position
    }
}
