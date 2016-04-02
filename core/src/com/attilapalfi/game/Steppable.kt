package com.attilapalfi.game

/**
 * Created by palfi on 2016-04-02.
 */
interface Steppable {
    fun step(cameraViewport: CameraViewport, deltaT: Long)
}