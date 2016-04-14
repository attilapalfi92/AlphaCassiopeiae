package com.attilapalfi.network

import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.messages.PAUSE
import com.attilapalfi.commons.messages.REGISTRATION
import com.attilapalfi.commons.messages.RESUME
import com.attilapalfi.commons.messages.START
import com.attilapalfi.commons.utlis.ClientMessageConverter
import com.attilapalfi.controller.Controller
import org.apache.commons.lang3.SerializationException

/**
 * Created by palfi on 2016-04-10.
 */
class ServerTcpSignalProcessor2(private val controller: Controller) : TcpSignalProcessor {

    override fun process(messageBytes: ByteArray) {
        try {
            val clientMessage = ClientMessageConverter.byteArrayToTcpClientMessage(messageBytes)
            when (clientMessage.messageType) {
                REGISTRATION -> {
                    controller.name = clientMessage.deviceName
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