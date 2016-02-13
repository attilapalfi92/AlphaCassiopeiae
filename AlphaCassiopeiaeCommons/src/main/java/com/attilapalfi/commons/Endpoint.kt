package com.attilapalfi.commons

import java.net.InetAddress
import kotlin.jvm.javaClass

/**
 * Created by palfi on 2016-01-14.
 */
open class Endpoint(public val IP: InetAddress,
                    public val port: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Endpoint

        if (IP != other.IP) return false

        return true
    }

    override fun hashCode(): Int {
        return IP.hashCode()
    }
}