package com.attilapalfi.game

import com.attilapalfi.network.ServerBroadcaster
import java.net.DatagramPacket

/**
 * Created by palfi on 2016-01-11.
 */
class MessageProcessor(private val world: World,
                       private val serverBroadcaster: ServerBroadcaster) {

    fun process(message: DatagramPacket) {

    }
}