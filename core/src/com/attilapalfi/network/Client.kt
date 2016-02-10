package com.attilapalfi.network

import com.attilapalfi.common.Endpoint
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-15.
 */
class Client(IP: InetAddress, tcpPort: Int, public val deviceName: String) : Endpoint(IP, tcpPort) {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}