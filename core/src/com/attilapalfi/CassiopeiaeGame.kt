package com.attilapalfi

import com.attilapalfi.common.*
import com.attilapalfi.game.ServerMessageProcessor
import com.attilapalfi.game.World
import com.attilapalfi.network.*
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class CassiopeiaeGame : ApplicationListener {

    lateinit var batch: SpriteBatch
    lateinit var img: Texture

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        val broadcaster: MessageBroadcaster = ServerMessageBroadcaster(PORT, 3, "Cassiopeiae server discovery.")
        val world: World = World()
        val messageProcessor: MessageProcessor = ServerMessageProcessor(world, broadcaster)
        val messageReceiver: MessageReceiver = ServerMessageReceiver(messageProcessor, PORT, BUFFER_SIZE)

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
