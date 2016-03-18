package com.attilapalfi.utlis

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3

/**
 * Created by 212461305 on 2016.03.18..
 */
fun OrthographicCamera.adjustCameraPos(cameraSpeed: Float, deltaT: Float) {
    synchronized(this) {
        val newPosition = position.add(cameraSpeed * deltaT)
        position.lerp(newPosition, deltaT)
    }
}

fun OrthographicCamera.getCameraPos(): Vector3 = synchronized(this) { position }