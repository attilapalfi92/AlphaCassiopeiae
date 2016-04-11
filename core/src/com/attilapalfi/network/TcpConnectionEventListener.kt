package com.attilapalfi.network

/**
 * Created by palfi on 2016-04-10.
 */
interface TcpConnectionEventListener {
    fun onConnect(tcpConnection2: TcpConnection2)
    fun onDisconnect(tcpConnection2: TcpConnection2)
}