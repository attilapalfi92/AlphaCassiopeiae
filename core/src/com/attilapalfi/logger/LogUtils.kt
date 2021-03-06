package com.attilapalfi.logger

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx

/**
 * Created by palfi on 2016-02-14.
 */
fun logError(tag: String, message: String) {
    Gdx.app.logLevel = Application.LOG_ERROR
    Gdx.app.log(tag, message)
}

fun logInfo(tag: String, message: String) {
    Gdx.app.logLevel = Application.LOG_INFO
    Gdx.app.log(tag, message)
}