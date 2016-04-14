package com.attilapalfi.network

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-12.
 */
interface SensorDataListener {
    fun onSetPlayerSpeed(address: InetAddress, speedX: Float, speedY: Float)
}