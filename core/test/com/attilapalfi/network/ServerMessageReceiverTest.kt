package com.attilapalfi.network

import com.attilapalfi.AbstractTest
import com.attilapalfi.common.BUFFER_SIZE
import com.attilapalfi.common.MessageReceiver
import com.attilapalfi.common.PORT
import com.attilapalfi.game.TestPacketProcessorImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-13.
 */
class ServerMessageReceiverTest : AbstractTest() {

    private lateinit var messageProcessor: TestPacketProcessorImpl
    private lateinit var messageReceiver: MessageReceiver
    private val messageCount: Int = 1000

    private val localHost: InetAddress = InetAddress.getLocalHost()
    private val socket: DatagramSocket = DatagramSocket(PORT + 1)

    @Before
    override fun setup() {
        super.setup()
        messageProcessor = TestPacketProcessorImpl(messageCount)
        messageReceiver = ServerMessageReceiver(messageProcessor)
    }

    @Test
    fun testReceiving() {
        messageReceiver.startReceiving()
        for (i in 0..messageCount - 1) {
            sendToLocalHost(i.toString().toByteArray())
            Thread.sleep(2)
        }
        assertReceiving()
    }

    private fun sendToLocalHost(payload: ByteArray) {
        val packet: DatagramPacket = DatagramPacket(payload, payload.size, localHost, PORT)
        socket.send(packet)
    }

    private fun assertReceiving() {
        Assert.assertTrue(messageProcessor.processCallCount <= messageCount)
        Assert.assertTrue(messageProcessor.processCallCount >= messageCount * 0.99)
        assertPayload()
    }

    private fun assertPayload() {
        var found = 0
        for (messageIndex in 0..messageCount - 1) {
            val expectedPayload = generateExceptedPayload(messageIndex)
            for (historyIndex in 0..messageProcessor.messageHistory.size - 1) {
                if (payloadEquals(historyIndex, expectedPayload)) {
                    found++
                }
            }
        }
        Assert.assertEquals(messageProcessor.processCallCount, found)
    }

    private fun generateExceptedPayload(messageIndex: Int): ByteArray {
        val payload = ByteArray(BUFFER_SIZE)
                .apply { messageIndex.toString().toByteArray().forEachIndexed { index, byte -> set(index, byte) } }
        return payload
    }

    private fun payloadEquals(historyIndex: Int, expectedPayload: ByteArray): Boolean {
        val actualPayload = messageProcessor.messageHistory[historyIndex].data
        for (payloadIndex in 0..BUFFER_SIZE - 1) {
            if ( actualPayload[payloadIndex] != expectedPayload[payloadIndex] ) {
                return false
            }
        }
        return true
    }
}