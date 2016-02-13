package com.attilapalfi.commons

/**
 * Created by palfi on 2016-01-13.
 */
interface UdpPacketReceiver {
    fun started(): Boolean
    fun startReceiving()
    fun stopReceiving()
}