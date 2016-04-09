package com.attilapalfi.game.entities

import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.Renderable
import com.attilapalfi.game.Steppable
import com.badlogic.gdx.math.Matrix4

/**
 * Created by palfi on 2016-01-11.
 */
class Player(@Volatile var x: Int = 0,
             @Volatile var y: Int = 0) : HashedEntity(), Steppable, Renderable {

    @Volatile
    var speedX: Float = 0f
    @Volatile
    var speedY: Float = 0f
    @Volatile
    var health: Int = 100

    override fun step(cameraViewport: CameraViewport, deltaT: Long) {

    }

    override fun render(projAndViewMatrix: Matrix4) {

    }

    override fun dispose() {
        throw UnsupportedOperationException()
    }
}