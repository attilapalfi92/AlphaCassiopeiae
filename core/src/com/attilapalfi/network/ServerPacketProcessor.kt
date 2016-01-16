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

/**
 * Created by palfi on 2016-01-11.
 */
class ServerPacketProcessor(private val world: World,
                            private val messageBroadcaster: MessageBroadcaster) : PacketProcessor {

    private val messageSender: MessageSender = ServerMessageSender()
    private val players: MutableMap<Client, Player> = HashMap(11)

    override fun process(packet: DatagramPacket) {
        try {
            val clientMessage = Converter.byteArrayToMessage(packet.data)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    handleRegistration(clientMessage, packet)
                }
                START -> {
                    world.gameState = GameState.STARTED
                    world.start(players)
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
            messageSender.send(client, ServerMessage(CLIENT_ACKNOWLEDGED))
            world.gameState = GameState.WAITING_FOR_START
        }
    }
}