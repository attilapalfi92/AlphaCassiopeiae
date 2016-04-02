package com.attilapalfi.game.entities

import com.attilapalfi.game.CameraViewport
import com.attilapalfi.game.Renderable
import com.attilapalfi.game.Steppable

/**
 * Created by 212461305 on 2016.03.18..
 */
class Attila(@Volatile var posX: Float,
             @Volatile var posy: Float) : HashedEntity(), Renderable, Steppable {

    companion object {
        //        private val sprite = SpriteBatch()
        //        private val attila = Texture("Attila.jpg")
    }

    override fun render() {
        //        sprite.begin()
        //        sprite.draw(attila, posX, posy)
        //        sprite.end()
    }

    override fun step(cameraViewport: CameraViewport, deltaT: Long) {
        throw UnsupportedOperationException()
    }
}