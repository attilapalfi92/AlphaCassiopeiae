package com.attilapalfi.network

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-12.
 */
interface SensorDataDistributor {
    fun onSetPlayerSpeed(address: InetAddress, x: Float, y: Float)
}