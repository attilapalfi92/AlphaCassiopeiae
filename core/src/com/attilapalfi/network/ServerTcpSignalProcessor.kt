package com.attilapalfi.network

import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.UdpMessageBroadcaster
import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.game.GameState
import com.attilapalfi.game.Player
import com.attilapalfi.game.World
import org.apache.commons.lang3.SerializationException
import java.net.InetAddress

/**
 * Created by 212461305 on 2016.02.10..
 */
class ServerTcpSignalProcessor(private val world: World,
                               private val tcpConnectionHandler: TcpConnectionHandler,
                               private val messageBroadcaster: UdpMessageBroadcaster,
                               private val communicationManager: CommunicationManager) : TcpSignalProcessor {

    override fun process(messageBytes: ByteArray) {
        try {
            val clientMessage = ClientMessageConverter.byteArrayToTcpClientMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    handleRegistration(clientMessage, tcpConnectionHandler.clientIp, tcpConnectionHandler.clientPort)
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
        messageBroadcaster.clientConnected()
        val client = Client(ipAddress, port, clientMessage.deviceName as String, Player())
        world.addPlayer(ipAddress, client)
        tcpConnectionHandler.sendRegAck()
    }

    private fun handleStart() {
        communicationManager.sendStartAcksToClients()
        if (world.gameState == GameState.WAITING_FOR_START) {
            world.gameState = GameState.RUNNING
            world.start()
        }
    }
}