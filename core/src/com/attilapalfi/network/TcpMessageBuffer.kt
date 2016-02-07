package com.attilapalfi.network

import com.attilapalfi.common.messages.MESSAGE_END

/**
 * Created by palfi on 2016-02-07.
 */
class TcpMessageBuffer {

    private val buffer = ByteArray(1500)
    private var currentBufferSize: Int = 0

    fun tryToProcess(array: ByteArray, readBytes: Int) {
        for (i in 0..readBytes - 1) {
            buffer[currentBufferSize + i] = array[i]
        }
        currentBufferSize += readBytes
        val messageEnd = findMessageEnd()
        if (messageEnd != -1) {
            processMessage(messageEnd)
        }
    }

    private fun findMessageEnd(): Int {
        for (i in 0..currentBufferSize - 1 - MESSAGE_END.size) {
            if (buffer.copyOfRange(i, i + MESSAGE_END.size) == MESSAGE_END) {
                return i
            }
        }
        return -1
    }

    private fun processMessage(messageEnd: Int) {
        val messageBytes: ByteArray = buffer.copyOfRange(0, messageEnd)
        truncateBuffer(messageEnd)

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