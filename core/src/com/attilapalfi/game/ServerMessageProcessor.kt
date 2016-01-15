package com.attilapalfi.game

import com.attilapalfi.common.MessageBroadcaster
import com.attilapalfi.common.MessageProcessor
import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-11.
 */
class ServerMessageProcessor(private val world: World,
                             private val messageBroadcaster: MessageBroadcaster) : MessageProcessor {

    override fun process(packet: DatagramPacket) {

    }
}