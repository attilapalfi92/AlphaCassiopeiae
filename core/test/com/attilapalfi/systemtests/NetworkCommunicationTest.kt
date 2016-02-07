package com.attilapalfi.systemtests

import com.attilapalfi.common.MessageReceiver
import com.attilapalfi.common.PORT
import com.attilapalfi.common.PacketProcessor
import com.attilapalfi.game.GameState
import com.attilapalfi.game.World
import com.attilapalfi.network.MessageBroadcaster
import com.attilapalfi.network.ServerMessageBroadcaster
import com.attilapalfi.network.ServerMessageReceiver
import com.attilapalfi.network.ServerPacketProcessor
import org.junit.Assert
import org.junit.Test
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-16.
 */
class NetworkCommunicationTest {
    private val world = World()
    private val broadcaster: MessageBroadcaster = ServerMessageBroadcaster(PORT, 1)
    private val packetProcessor: PacketProcessor = ServerPacketProcessor(world, broadcaster)
    private val messageReceiver: MessageReceiver = ServerMessageReceiver(packetProcessor)

    private val scanner = Scanner(System.`in`)

    @Test
    fun something() {
        val map = ConcurrentHashMap<Int, Float>(mapOf(1 to 1.0f, 2 to 2.0f, 3 to 3.0f))
        for ((i, f) in map) {
            if (i == 2) {
                map.remove(i)
            }
        }
    }

    @Test
    fun step1() {
        broadcaster.startBroadcasting()
        messageReceiver.startReceiving()
        println("Start the client application in 5 seconds.")
        waitAndCountdown()
        step2()
    }

    fun step2() {
        Assert.assertEquals(1, packetProcessor.playerCount())
        Assert.assertEquals(GameState.WAITING_FOR_START, world.gameState)
        println("Press start on the client in 5 seconds.")
        waitAndCountdown()
        step3()
    }

    fun step3() {
        Assert.assertEquals(GameState.RUNNING, world.gameState)
        Assert.assertEquals(1, world.playerCount())
    }

    private fun waitAndCountdown() {
        listOf(5, 4, 3, 2, 1).forEach {
            println(it)
            Thread.sleep(1000)
        }
    }
}