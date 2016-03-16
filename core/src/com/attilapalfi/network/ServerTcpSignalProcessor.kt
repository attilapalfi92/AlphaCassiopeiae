package com.attilapalfi.network

import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.exception.ConnectionException
import com.attilapalfi.logic.GameState
import com.attilapalfi.logic.Player
import com.attilapalfi.logic.World
import org.apache.commons.lang3.SerializationException
import java.net.InetAddress

/**
 * Created by 212461305 on 2016.02.10..
 */
class ServerTcpSignalProcessor(private val world: World,
                               private val tcpConnection: TcpConnection,
                               private val tcpConnectionManager: TcpConnectionManager,
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
        val client = Client(ipAddress, port, clientMessage.deviceName as String, Player())
        tcpConnectionManager.clientConnected(client)
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