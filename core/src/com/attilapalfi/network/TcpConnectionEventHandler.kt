package com.attilapalfi.network

/**
 * Created by palfi on 2016-02-14.
 */
interface TcpConnectionEventHandler {
    fun onTcpConnectionDeath(tcpConnection: TcpConnection)
    fun clientConnected(client: Client)
}