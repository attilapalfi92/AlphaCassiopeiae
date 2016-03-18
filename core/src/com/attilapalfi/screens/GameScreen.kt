package com.attilapalfi.screens

import com.attilapalfi.game.WorldRenderer
import com.attilapalfi.logic.GameManager
import com.badlogic.gdx.Game

/**
 * Created by 212461305 on 2016.03.17..
 */
class GameScreen(game: Game, private val gameManager: GameManager) : CassiopeiaeScreen(game) {

    private val renderer: WorldRenderer = gameManager.worldRenderer

    init {
        gameManager.startActualNewGame()
    }

    override fun render(delta: Float) {
        renderer.render(delta)
    }
}