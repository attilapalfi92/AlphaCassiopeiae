package com.attilapalfi.network

import com.attilapalfi.common.SignalProcessor
import com.attilapalfi.common.messages.*
import com.attilapalfi.game.GameState
import com.attilapalfi.game.Player
import com.attilapalfi.game.World
import com.attilapalfi.network.utlis.Converter
import org.apache.commons.lang3.SerializationException
import java.net.InetAddress

/**
 * Created by 212461305 on 2016.02.10..
 */
class TcpSignalProcessor(private val world: World,
                         private val tcpServer: TcpServer,
                         private val messageBroadcaster: MessageBroadcaster,
                         private val communicationManager: CommunicationManager) : SignalProcessor {

    override fun processMessage(messageBytes: ByteArray, ipAddress: InetAddress, port: Int) {
        try {
            val clientMessage = Converter.byteArrayToTcpMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    handleRegistration(clientMessage, ipAddress, port)
                }
                START -> {
                    handleStart()
                }
                PAUSE -> {

                }
                RESUME -> {

                }
            }
        } catch (e: SerializationException) {

        }
    }

    private fun handleRegistration(clientMessage: TcpClientMessage, ipAddress: InetAddress, port: Int) {
        clientMessage.deviceName?.let {
            messageBroadcaster.clientConnected()
            val client = Client(ipAddress, port, clientMessage.deviceName, Player())
            world.addPlayer(ipAddress, client)
            tcpServer.sendRegAck()
        }
    }

    private fun handleStart() {
        communicationManager.sendStartAcksToClients()
        if (world.gameState == GameState.WAITING_FOR_START) {
            world.gameState = GameState.RUNNING
            world.start()
        }
    }
}