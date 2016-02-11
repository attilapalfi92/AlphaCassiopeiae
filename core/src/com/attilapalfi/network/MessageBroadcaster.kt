package com.attilapalfi.network

/**
 * Created by palfi on 2016-01-13.
 */
interface MessageBroadcaster {
    fun startBroadcasting()
    fun clientConnected()
    fun clientDisconnected()
    fun clientsCleared()
}