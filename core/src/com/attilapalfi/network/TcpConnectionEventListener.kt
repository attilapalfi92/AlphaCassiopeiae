package com.attilapalfi.network

/**
 * Created by palfi on 2016-04-10.
 */
interface TcpConnectionEventListener {
    fun onTcpConnected(tcpConnection: TcpConnection)
    fun onTcpDisconnect(tcpConnection: TcpConnection)
}