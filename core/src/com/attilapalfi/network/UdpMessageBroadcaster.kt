package com.attilapalfi.network

/**
 * Created by 212461305 on 2016.03.16..
 */
interface UdpMessageBroadcaster {
    fun startBroadcasting()
    fun clientConnected(clientPort: Int)
    fun clientDisconnected(clientPort: Int)
    fun addNewAvailablePort(clientPort: Int)
    fun clientsCleared()
}