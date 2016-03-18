package com.attilapalfi.screens

import com.attilapalfi.CURRENT_WINDOW_HEIGHT
import com.attilapalfi.CURRENT_WINDOW_WIDTH
import com.attilapalfi.commons.DEFAULT_MAX_USERS
import com.attilapalfi.logic.GameManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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

    private val communicationManager = GameManager(DEFAULT_MAX_USERS)
            .apply { startUdpCommunication() }

    private val sprite = SpriteBatch()
    private val tomCruize = Texture("tom.jpg")
    private var time: Float = 0f

    override fun render(delta: Float) {
        time += delta

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawTomCruize()
    }

    private fun drawTomCruize() {
        sprite.begin()
        sprite.transformMatrix = transformMatrix()
        sprite.draw(tomCruize, 0f, 0f)

        if (time > 3) {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
                game.screen = LobbyScreen(game, communicationManager)
            }
        }

        sprite.end()
    }

    private fun transformMatrix(): Matrix4? {
        val amplitude = CURRENT_WINDOW_HEIGHT / 3f
        val deltaX: Float = amplitude * Math.cos(time.toDouble() * 1.5).toFloat()
        val deltaY: Float = amplitude * Math.sin(time.toDouble() * 1.5).toFloat()

        val transformMatrix = Matrix4()
                .translate((CURRENT_WINDOW_WIDTH / 2f) + deltaX, (CURRENT_WINDOW_HEIGHT / 2f) + deltaY, 0f)
                .scale(0.4f, 0.4f, 0.4f)
                .rotate(Vector3(0f, 0f, 1f), time * 100)
        return transformMatrix
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}