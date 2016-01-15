package com.attilapalfi.network

import com.attilapalfi.common.PacketProcessor
import com.attilapalfi.game.World
import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-11.
 */
class ServerPacketProcessor(private val world: World,
                            private val messageBroadcaster: MessageBroadcaster) : PacketProcessor {

    override fun process(packet: DatagramPacket) {

    }
}