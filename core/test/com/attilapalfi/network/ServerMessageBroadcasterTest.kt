package com.attilapalfi.network

import com.attilapalfi.AbstractTest
import org.junit.Before
import org.junit.Test

/**
 * Created by palfi on 2016-01-12.
 */
class ServerMessageBroadcasterTest : AbstractTest() {

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun testBroadcaster() {
        messageBroadcaster.startBroadcasting()
        connectAndDisconnectClients(100)
        for (i in 1..1000) {
            connectAndDisconnectClients(0)
        }
    }

    private fun connectAndDisconnectClients(sleepTime: Long) {
        if (sleepTime == 0L) {
            messageBroadcaster.clientConnected()
            messageBroadcaster.clientConnected()
            messageBroadcaster.clientDisconnected()
            messageBroadcaster.clientDisconnected()
        } else {
            Thread.sleep(sleepTime)
            messageBroadcaster.clientConnected()
            Thread.sleep(sleepTime)
            messageBroadcaster.clientConnected()
            Thread.sleep(sleepTime)
            messageBroadcaster.clientDisconnected()
            Thread.sleep(sleepTime)
            messageBroadcaster.clientDisconnected()
        }
    }
}