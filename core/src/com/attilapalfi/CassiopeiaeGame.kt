package com.attilapalfi

import com.attilapalfi.commons.DEFAULT_MAX_USERS
import com.attilapalfi.game.World
import com.attilapalfi.network.CommunicationManager
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class CassiopeiaeGame : ApplicationListener {

    private val batch = SpriteBatch()
    private val img = Texture("badlogic.jpg")
    private val world = World()
    private val communicationManager = CommunicationManager(world, DEFAULT_MAX_USERS)

    override fun create() {
        communicationManager.startUdpCommunication()
        playMusic()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        world.render()
        //        batch.begin()
        //        batch.draw(img, 0f, 0f)
        //        batch.end()
    }

    override fun pause() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun resume() {

    }

    override fun dispose() {

    }

    private fun playMusic() {
        val music = Gdx.audio.newMusic(Gdx.files.getFileHandle("music/magneto.mp3", FileType.Local))
        music.volume = 1.0f
        music.play()
        music.isLooping = true
    }
}
