package com.attilapalfi.network

/**
 * Created by 212461305 on 2016.03.16..
 */
interface UdpMessageBroadcaster {
    fun startBroadcasting()
    fun clientConnected(serverPort: Int)
    fun clientDisconnected(serverPort: Int)
    fun addNewAvailablePort(serverPort: Int)
    fun clientsCleared()
}