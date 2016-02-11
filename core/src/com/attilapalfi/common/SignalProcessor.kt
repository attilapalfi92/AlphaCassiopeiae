package com.attilapalfi.common

import java.net.InetAddress

/**
 * Created by 212461305 on 2016.02.10..
 */
interface SignalProcessor {
    fun processMessage(messageBytes: ByteArray, ipAddress: InetAddress, port: Int)
}