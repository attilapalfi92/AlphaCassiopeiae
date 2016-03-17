package com.attilapalfi.network

import java.net.InetAddress

/**
 * Created by 212461305 on 2016.03.17..
 */
interface GameEventHandler {
    fun onPlayerJoined(ipAddress: InetAddress, client: Client)
    fun onGameStartReceived()
    fun onSetPlayerSpeed(address: InetAddress, x: Float, y: Float)
}