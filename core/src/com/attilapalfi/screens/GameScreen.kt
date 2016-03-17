package com.attilapalfi.screens

import com.attilapalfi.CassiopeiaeRenderer
import com.attilapalfi.logic.GameManager
import com.badlogic.gdx.Game

/**
 * Created by 212461305 on 2016.03.17..
 */
class GameScreen(game: Game, private val gameManager: GameManager) : CassiopeiaeScreen(game) {

    init {
        gameManager.startActualNewGame()
    }

    private val renderer = CassiopeiaeRenderer()

    override fun render(delta: Float) {

    }
}