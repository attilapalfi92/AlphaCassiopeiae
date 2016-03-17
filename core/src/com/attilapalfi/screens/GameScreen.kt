package com.attilapalfi.screens

import com.attilapalfi.CassiopeiaeRenderer
import com.attilapalfi.network.CommunicationManager
import com.badlogic.gdx.Game

/**
 * Created by 212461305 on 2016.03.17..
 */
class GameScreen(game: Game, private val communicationManager: CommunicationManager) : CassiopeiaeScreen(game) {

    init {
        communicationManager.startActualNewGame()
    }

    private val renderer = CassiopeiaeRenderer()

    override fun render(delta: Float) {

    }
}