package com.attilapalfi.commons

/**
 * Created by palfi on 2016-01-13.
 */
interface UdpMessageBroadcaster {
    fun startBroadcasting()
    fun clientConnected()
    fun clientDisconnected()
    fun clientsCleared()
}