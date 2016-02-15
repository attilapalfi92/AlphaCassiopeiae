package com.attilapalfi.network

/**
 * Created by palfi on 2016-02-14.
 */
interface AckSender {
    fun sendStartAcksToClients()
    fun sendPauseAcksToClients()
    fun sendResumeAcksToClients()
}

interface TcpConnectionManager {
    fun onTcpConnectionDeath(tcpConnection: TcpConnection)
}