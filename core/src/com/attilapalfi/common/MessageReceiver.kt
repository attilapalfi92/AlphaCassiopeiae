package com.attilapalfi.common

/**
 * Created by palfi on 2016-01-13.
 */
interface MessageReceiver {
    fun started(): Boolean
    fun startReceiving()
    fun stopReceiving()
}