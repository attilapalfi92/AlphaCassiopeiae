package com.attilapalfi.network

import com.attilapalfi.common.Endpoint
import com.attilapalfi.game.Player
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-15.
 */
class Client(IP: InetAddress, tcpPort: Int, val deviceName: String, val player: Player) : Endpoint(IP, tcpPort) {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}