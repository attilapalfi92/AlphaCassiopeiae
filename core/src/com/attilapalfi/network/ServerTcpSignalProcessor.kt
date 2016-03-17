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
class ServerTcpSignalProcessor(private val gameEventHandler: GameEventHandler,
                               private val tcpConnection: TcpConnection,
                               private val tcpConnectionEventHandler: TcpConnectionEventHandler) : TcpSignalProcessor {

    override fun process(messageBytes: ByteArray) {
        try {
            val clientMessage = ClientMessageConverter.byteArrayToTcpClientMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    registration(clientMessage)
                }
                START -> {
                    gameEventHandler.onGameStartReceived()
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
        tcpConnectionEventHandler.clientConnected(client)
        gameEventHandler.onPlayerJoined(ipAddress, client)
        tcpConnection.sendRegAck()
    }
}