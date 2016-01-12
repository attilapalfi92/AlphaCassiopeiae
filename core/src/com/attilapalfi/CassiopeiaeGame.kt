package com.attilapalfi

import com.attilapalfi.game.MessageProcessor
import com.attilapalfi.game.World
import com.attilapalfi.network.MessageReceiver
import com.attilapalfi.network.ServerBroadcaster
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class CassiopeiaeGame : ApplicationListener {

    companion object {
        val PORT = 23456
    }

    lateinit var batch: SpriteBatch
    lateinit var img: Texture

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        val broadcaster = ServerBroadcaster(PORT, "Cassiopeiae server discovery.".toByteArray())
        val world = World()
        val messageProcessor = MessageProcessor(world, broadcaster)
        val messageReceiver = MessageReceiver(messageProcessor, PORT)

        broadcaster.startBroadcasting()
        messageReceiver.startReceiving()
        world.start()

        playMusic()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
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
