package com.attilapalfi.screens

import com.attilapalfi.core.GameManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.*

/**
 * Created by 212461305 on 2016.03.17..
 */
class LobbyScreen(game: Game, private val gameManager: GameManager) : CassiopeiaeScreen(game) {

    private val joinedPlayers = ArrayList(gameManager.players())
    private val spriteBatch = SpriteBatch()

    init {
        gameManager.startNewGame()
    }

    override fun show() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        spriteBatch.begin()
        bitmapFont.draw(spriteBatch, "Joined players: $joinedPlayers",
                Gdx.graphics.width / 2f, Gdx.graphics.height / 2f)
        spriteBatch.end()

        if (gameManager.gameStartIsReceived()) {
            game.screen = GameScreen(game, gameManager)
        }
    }

    override fun hide() {

    }

}