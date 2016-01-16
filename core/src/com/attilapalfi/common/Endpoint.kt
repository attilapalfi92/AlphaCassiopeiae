package com.attilapalfi.common

import java.net.InetAddress

/**
 * Created by palfi on 2016-01-14.
 */
open class Endpoint(public val IP: InetAddress,
                    public val port: Int) {

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Endpoint

        if (IP != other.IP) return false
        if (port != other.port) return false

        return true
    }

    override fun hashCode(): Int{
        var result = IP.hashCode()
        result += 31 * result + port
        return result
    }
}