package com.attilapalfi.common

import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-13.
 */
interface PacketProcessor {
    fun process(packet: DatagramPacket)
}