package com.attilapalfi.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

/**
 * Created by 212461305 on 2016.03.16..
 */
abstract class CassiopeiaeScreen(protected val game: Game) : Screen {

    companion object {
        val bitmapFont: BitmapFont

        init {
            val fontGenerator = FreeTypeFontGenerator(
                    Gdx.files.internal("fonts/open-sans/OpenSans-Regular.ttf")
            )
            bitmapFont = fontGenerator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                size = 48
            })
            fontGenerator.dispose()
        }
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun hide() {
    }

    override fun render(delta: Float) {
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}