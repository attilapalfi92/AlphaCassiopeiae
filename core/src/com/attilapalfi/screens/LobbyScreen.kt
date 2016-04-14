package com.attilapalfi.screens

import com.attilapalfi.core.GameManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

/**
 * Created by 212461305 on 2016.03.17..
 */
class LobbyScreen(game: Game, private val gameManager: GameManager) : CassiopeiaeScreen(game) {

    init {
        gameManager.startNewGame()
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if ( gameManager.gameStartIsReceived() ) {
            game.screen = GameScreen(game, gameManager)
        }
    }

    override fun hide() {

    }

}