package com.attilapalfi.game.entities

import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.Renderable
import com.attilapalfi.game.Steppable
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4

/**
 * Created by 212461305 on 2016.03.18..
 */
class Attila(@Volatile var posX: Float,
             @Volatile var posy: Float) : HashedEntity(), Renderable, Steppable {

    private val sprite = SpriteBatch()
    private val attila = Texture("images/Attila.jpg")

    override fun render(projAndViewMatrix: Matrix4) {
        sprite.projectionMatrix = projAndViewMatrix
        sprite.begin()
        sprite.draw(attila, posX, posy, 2f, 2f)
        sprite.end()
    }

    override fun step(cameraViewport: CameraViewport, deltaT: Long) {
        // 1 unit / sec speed
    }

    override fun dispose() {
        sprite.dispose()
    }
}