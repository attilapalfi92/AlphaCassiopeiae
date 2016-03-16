package com.attilapalfi

import com.attilapalfi.commons.DEFAULT_MAX_USERS
import com.attilapalfi.logic.World
import com.attilapalfi.network.CommunicationManager
import com.attilapalfi.screens.IntroScreen
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class CassiopeiaeGame : Game() {

    private val world = World()
    private val communicationManager = CommunicationManager(world, DEFAULT_MAX_USERS)

    override fun create() {
        communicationManager.startUdpCommunication()
        playMusic()
        setScreen(IntroScreen(this))
    }

    private fun playMusic() {
        val music = Gdx.audio.newMusic(Gdx.files.getFileHandle("music/magneto.mp3", FileType.Local))
        music.volume = 1.0f
        music.play()
        music.isLooping = true
    }
}
