package com.attilapalfi.commons

/**
 * Created by 212461305 on 2016.02.10..
 */
interface TcpMessageBuffer {
    fun tryToProcess(array: ByteArray, readBytes: Int)
}