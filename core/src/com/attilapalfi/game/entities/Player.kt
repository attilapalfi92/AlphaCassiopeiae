package com.attilapalfi.game.entities

import com.attilapalfi.commons.messages.PressedButton
import com.attilapalfi.commons.messages.PressedButton.*
import com.attilapalfi.controller.Controller
import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.Renderable
import com.attilapalfi.game.Steppable
import com.badlogic.gdx.math.Matrix4
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-11.
 */
class Player(val controller: Controller,
             @Volatile var points: Int,
             @Volatile var x: Int = 0,
             @Volatile var y: Int = 0) : HashedEntity(), Steppable, Renderable {

    @Volatile
    var speedX: Float = 0f
    @Volatile
    var speedY: Float = 0f
    @Volatile
    var health: Int = 100

    var weaponXFirePeriod = 50
    var weaponYFirePeriod = 50
    var bombFirePeriod = 2000

    val buttonsToLastPressTimes = ConcurrentHashMap<PressedButton, Long>().apply {
        put(A, 0)
        put(B, 0)
        put(X, 0)
        put(Y, 0)
        put(None, 0)
    }

    override fun step(cameraViewport: CameraViewport, deltaT: Long) {

    }

    override fun render(projAndViewMatrix: Matrix4) {

    }

    override fun dispose() {
        throw UnsupportedOperationException()
    }

    override fun toString() = "${controller.name} </_\\> $points"
}