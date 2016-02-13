package com.attilapalfi.commons

import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-13.
 */
interface UdpPacketProcessor {
    fun process(packet: DatagramPacket)
}