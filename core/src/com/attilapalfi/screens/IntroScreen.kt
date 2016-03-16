package com.attilapalfi.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

/**
 * Created by 212461305 on 2016.03.16..
 */
class IntroScreen(game: Game) : CassiopeiaeScreen(game) {

    private val batch = SpriteBatch()
    private val img = Texture("tom.jpg")
    private val tomCruize = TextureRegion(img)
    private var time: Float = 0f

    override fun show() {
    }

    override fun pause() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun hide() {
    }

    override fun render(delta: Float) {
        time += delta

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()

        val rotationMatrix = Matrix4().translate(500f, 300f, 0f).rotate(Vector3(0f, 0f, 1f), time * 100).scale(0.5f, 0.5f, 0.5f)
        //batch.transformMatrix = rotationMatrix
        batch.draw(tomCruize, 300f, 300f, 0.5f, 0.5f, 100f, 100f, 1f, 1f, 30 * time)
        //batch.draw(img, 0f, 0f)
        batch.end()
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}