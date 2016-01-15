package com.attilapalfi.network

import com.attilapalfi.common.messages.ServerMessage
import java.io.Closeable

/**
 * Created by palfi on 2016-01-14.
 */
interface MessageSender : Closeable {
    fun send(client: Client, message: ServerMessage)
}