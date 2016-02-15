package com.attilapalfi.network

import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.UdpMessageBroadcaster
import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.exception.ConnectionException
import com.attilapalfi.game.GameState
import com.attilapalfi.game.Player
import com.attilapalfi.game.World
import org.apache.commons.lang3.SerializationException
import java.net.InetAddress

/**
 * Created by 212461305 on 2016.02.10..
 */
class ServerTcpSignalProcessor(private val world: World,
                               private val tcpConnection: TcpConnection,
                               private val messageBroadcaster: UdpMessageBroadcaster,
                               private val ackSender: AckSender) : TcpSignalProcessor {

    override fun process(messageBytes: ByteArray) {
        try {
            val clientMessage = ClientMessageConverter.byteArrayToTcpClientMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    registration(clientMessage)
                }
                START -> {
                    start()
                }
                PAUSE -> {

                }
                RESUME -> {

                }
            }
        } catch (e: SerializationException) {

        }
    }

    private fun registration(clientMessage: TcpClientMessage) {
        if (tcpConnection.clientIp == null || tcpConnection.clientPort == null) {
            throw ConnectionException("Client IP or PORT was null, when connection was already alive.")
        } else {
            handleRegistration(clientMessage,
                    tcpConnection.clientIp as InetAddress,
                    tcpConnection.clientPort as Int)
        }
    }

    private fun handleRegistration(clientMessage: TcpClientMessage, ipAddress: InetAddress, port: Int) {
        messageBroadcaster.clientConnected()
        val client = Client(ipAddress, port, clientMessage.deviceName as String, Player())
        world.addPlayer(ipAddress, client)
        tcpConnection.sendRegAck()
    }

    private fun start() {
        ackSender.sendStartAcksToClients()
        if (world.gameState == GameState.WAITING_FOR_START) {
            world.gameState = GameState.RUNNING
            world.start()
        }
    }
}