package com.attilapalfi

import com.attilapalfi.logic.World
import com.attilapalfi.screens.IntroScreen
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx

class CassiopeiaeGame : Game() {

    private val world = World()

    override fun create() {
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
