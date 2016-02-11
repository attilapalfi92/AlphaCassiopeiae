package com.attilapalfi.network

import com.attilapalfi.common.MessageBuffer
import com.attilapalfi.common.SignalProcessor
import com.attilapalfi.common.TCP_BUFFER_BUFFER_SIZE
import com.attilapalfi.common.messages.MESSAGE_END

/**
 * Created by palfi on 2016-02-07.
 */
class TcpMessageBuffer(private val tcpServer: TcpServer,
                       private val signalProcessor: SignalProcessor) : MessageBuffer {

    private val buffer = ByteArray(TCP_BUFFER_BUFFER_SIZE)
    private var currentBufferSize: Int = 0

    override fun tryToProcess(array: ByteArray, readBytes: Int) {
        for (i in 0..readBytes - 1) {
            buffer[currentBufferSize + i] = array[i]
        }
        currentBufferSize += readBytes
        val messageEndIndex = findMessageEndIndex()
        if (messageEndIndex != -1) {
            processMessage(messageEndIndex)
        }
    }

    private fun findMessageEndIndex(): Int {
        for (i in 0..currentBufferSize - 1 - MESSAGE_END.size) {
            if (buffer.copyOfRange(i, i + MESSAGE_END.size) == MESSAGE_END) {
                return i
            }
        }
        return -1
    }

    private fun processMessage(messageEndIndex: Int) {
        val messageBytes: ByteArray = buffer.copyOfRange(0, messageEndIndex)
        truncateBuffer(messageEndIndex)
        signalProcessor.processMessage(messageBytes, tcpServer.clientIp, tcpServer.clientPort)
    }

    private fun truncateBuffer(messageEnd: Int) {
        val tempArray: ByteArray = buffer.copyOfRange(messageEnd + MESSAGE_END.size, currentBufferSize)
        buffer.fill(0)
        currentBufferSize = tempArray.size
        for (i in 0..currentBufferSize - 1) {
            buffer[i] = tempArray[i]
        }
    }
}