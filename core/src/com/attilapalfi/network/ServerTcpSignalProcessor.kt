package com.attilapalfi.network

import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.messages.ClientTcpMessageType.*
import com.attilapalfi.commons.messages.PressedButton.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.controller.Controller
import org.apache.commons.lang3.SerializationException

/**
 * Created by palfi on 2016-04-10.
 */
class ServerTcpSignalProcessor(private val controller: Controller) : TcpSignalProcessor {

    val messageConverter = ClientMessageConverter()

    override fun process(messageBytes: ByteArray) {
        try {
            val clientMessage = messageConverter.byteArrayToTcpMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    controller.name = clientMessage.deviceName
                }
                DISCONNECTION -> {
                    // TODO: handle disconnection
                }
                BUTTON_PRESS -> {
                    when (clientMessage.pressedButton) {
                        A -> controller.xPressed()
                        B -> controller.bPressed()
                        X -> controller.xPressed()
                        Y -> controller.yPressed()
                        PressedButton.None -> {
                        }
                    }
                }
            }
        } catch (e: SerializationException) {

        }
    }
}