package com.attilapalfi.game.entities

import com.attilapalfi.game.Renderable
import com.attilapalfi.game.Steppable
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Created by 212461305 on 2016.03.18..
 */
class Attila(var posX: Float, var posy: Float) : Renderable, Steppable {

    companion object {
        private val sprite = SpriteBatch()
        private val attila = Texture("tom.jpg")
    }

    override fun render() {
        sprite.begin()
        sprite.draw(attila, posX, posy)
        sprite.end()
    }

    override fun step(cameraPos: Float, deltaT: Long) {
        throw UnsupportedOperationException()
    }
}