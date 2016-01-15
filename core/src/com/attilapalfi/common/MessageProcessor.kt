package com.attilapalfi.common

import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-13.
 */
interface MessageProcessor {
    fun process(packet: DatagramPacket)
}