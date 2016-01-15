package com.attilapalfi.game

import com.attilapalfi.common.MessageProcessor
import java.net.DatagramPacket
import java.util.*

/**
 * Created by palfi on 2016-01-13.
 */
class TestMessageProcessorImpl(expectedMessageCount: Int) : MessageProcessor {
    public var processCallCount: Int = 0
        private set

    public val messageHistory: ArrayList<DatagramPacket> = ArrayList(expectedMessageCount)

    override fun process(packet: DatagramPacket) {
        processCallCount++
        messageHistory.add(packet)
    }
}