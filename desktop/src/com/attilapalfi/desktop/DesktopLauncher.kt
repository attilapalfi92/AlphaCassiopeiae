package com.attilapalfi.desktop

import com.attilapalfi.CassiopeiaeGame
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "Alpha Cassiopeiae"
        config.width = 1280
        config.height = 720
        LwjglApplication(CassiopeiaeGame(), config)
    }
}
