package com.attilapalfi.network

import com.attilapalfi.common.messages.ClientMessage
import com.attilapalfi.common.messages.ServerMessage
import org.apache.commons.lang3.SerializationUtils

/**
 * Created by palfi on 2016-01-15.
 */
object Converter {
    fun byteArrayToMessage(payload: ByteArray): ClientMessage
            = SerializationUtils.deserialize(payload)

    fun messageToByteArray(message: ServerMessage): ByteArray
            = SerializationUtils.serialize(message)
}