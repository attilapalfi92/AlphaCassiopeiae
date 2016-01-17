package com.attilapalfi.game

import com.attilapalfi.common.PacketProcessor
import java.net.DatagramPacket
import java.util.*

/**
 * Created by palfi on 2016-01-13.
 */
class TestPacketProcessorImpl(expectedMessageCount: Int) : PacketProcessor {
    public var processCallCount: Int = 0
        private set

    public val messageHistory: ArrayList<DatagramPacket> = ArrayList(expectedMessageCount)

    override fun process(packet: DatagramPacket) {
        processCallCount++
        messageHistory.add(packet)
    }

    override fun playerCount(): Int = 0
}
