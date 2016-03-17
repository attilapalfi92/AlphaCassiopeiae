package com.attilapalfi.screens

import com.attilapalfi.logic.GameManager
import com.badlogic.gdx.Game

/**
 * Created by 212461305 on 2016.03.17..
 */
class LobbyScreen(game: Game, private val gameManager: GameManager) : CassiopeiaeScreen(game) {

    override fun render(delta: Float) {
        if ( gameManager.gameStartIsReceived() ) {
            game.screen = GameScreen(game, gameManager)
        }
    }

}