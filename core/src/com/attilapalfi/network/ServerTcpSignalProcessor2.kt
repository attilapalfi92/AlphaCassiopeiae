package com.attilapalfi.network

import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.controller.Controller
import com.attilapalfi.exception.ConnectionException
import com.attilapalfi.game.entities.Player
import org.apache.commons.lang3.SerializationException
import java.net.InetAddress

/**
 * Created by palfi on 2016-04-10.
 */
class ServerTcpSignalProcessor2(private val controller: Controller) : TcpSignalProcessor {

    override fun process(messageBytes: ByteArray) {
        try {
            val clientMessage = ClientMessageConverter.byteArrayToTcpClientMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    controller.aPressed()
                }
                START -> {
                    controller.bPressed()
                }
                PAUSE -> {
                    controller.xPressed()
                }
                RESUME -> {
                    controller.yPressed()
                }
            }
        } catch (e: SerializationException) {

        }
    }
}