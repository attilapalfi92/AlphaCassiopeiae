package com.attilapalfi.network

import com.attilapalfi.common.PORT
import com.attilapalfi.common.PacketProcessor
import com.attilapalfi.common.messages.*
import com.attilapalfi.game.GameState
import com.attilapalfi.game.Player
import com.attilapalfi.game.World
import org.apache.commons.lang3.SerializationException
import java.net.DatagramPacket
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-01-11.
 */
class ServerPacketProcessor(private val world: World,
                            private val messageBroadcaster: MessageBroadcaster) : PacketProcessor {

    private val messageSender: MessageSender = ServerMessageSender()
    private val players: MutableMap<Client, Player> = HashMap(11)

    private val clientsToAcks: MutableMap<Client, Long> = ConcurrentHashMap()
    private val waitObject = Object()

    init {
        createAckReSenderThread().start()
    }

    private fun createAckReSenderThread(): Thread {
        return Thread({
            while (true) {
                synchronized(waitObject) {
                    if (clientsToAcks.isEmpty()) {
                        waitObject.wait()
                    } else {
                        resendAckOnTimeout()
                        Thread.sleep(250)
                    }
                }
            }
        })
    }

    private fun resendAckOnTimeout() {
        val currentTime = System.currentTimeMillis()
        for ((client, lastTime) in clientsToAcks) {
            if (currentTime - lastTime > 1000) {
                sendAckToClient(client, currentTime)
            }
        }
    }

    private fun sendAckToClient(client: Client, time: Long) {
        messageSender.send(client, ServerMessage(CLIENT_ACKNOWLEDGED))
        clientsToAcks.put(client, time)
    }

    override fun process(packet: DatagramPacket) {
        try {
            val clientMessage = Converter.byteArrayToMessage(packet.data)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    handleRegistration(clientMessage, packet)
                }
                START -> {
                    handleStart(packet)
                }
                SENSOR_DATA -> {

                }
                SHOOT -> {

                }
                SHITBOMB -> {

                }
                PAUSE -> {

                }
                RESUME -> {

                }
            }
        } catch (e: SerializationException) {

        }
    }

    private fun handleRegistration(clientMessage: ClientMessage, packet: DatagramPacket) {
        clientMessage.deviceName?.let {
            messageBroadcaster.clientConnected()
            val client = Client(packet.address, PORT, clientMessage.deviceName)
            players.put(client, Player())
            sendAckToClient(client, System.currentTimeMillis())
            synchronized(waitObject) { waitObject.notify() }
            world.gameState = GameState.WAITING_FOR_START
        }
    }

    private fun handleStart(packet: DatagramPacket) {
        for ((client, lastTime) in clientsToAcks) {
            if (client.IP == packet.address) {
                clientsToAcks.remove(client)
                break
            }
        }
        if (world.gameState == GameState.WAITING_FOR_START) {
            world.gameState = GameState.RUNNING
            world.start(players)
        }
    }

    override fun playerCount(): Int = players.size
}