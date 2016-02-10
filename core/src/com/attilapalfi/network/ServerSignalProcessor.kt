package com.attilapalfi.network

import com.attilapalfi.common.SignalProcessor
import com.attilapalfi.game.World

/**
 * Created by 212461305 on 2016.02.10..
 */
class ServerSignalProcessor(world: World, tcpServer: TcpServer) : SignalProcessor {

    override fun processMessage(messageBytes: ByteArray) {

    }
}