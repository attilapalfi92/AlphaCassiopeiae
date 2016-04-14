package com.attilapalfi.screens

import com.attilapalfi.core.GameManager
import com.attilapalfi.game.WorldRenderer
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

/**
 * Created by 212461305 on 2016.03.17..
 */
class GameScreen(game: Game, private val gameManager: GameManager) : CassiopeiaeScreen(game) {

    private val renderer: WorldRenderer = gameManager.worldRenderer

    override fun show() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    }

    override fun render(delta: Float) {
        renderer.render(delta)
    }

    override fun hide() {

    }
}