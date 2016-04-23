package com.attilapalfi.network

/**
 * Created by palfi on 2016-04-10.
 */
interface TcpConnectionEventListener {
    fun onConnect(tcpConnection: TcpConnection)
    fun onDisconnect(tcpConnection: TcpConnection)
}