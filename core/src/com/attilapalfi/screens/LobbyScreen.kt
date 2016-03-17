package com.attilapalfi.screens

import com.attilapalfi.network.CommunicationManager
import com.badlogic.gdx.Game

/**
 * Created by 212461305 on 2016.03.17..
 */
class LobbyScreen(game: Game, private val communicationManager: CommunicationManager) : CassiopeiaeScreen(game) {

    override fun render(delta: Float) {
        if ( communicationManager.gameStartIsReceived() ) {
            game.screen = GameScreen(game, communicationManager)
        }
    }

}