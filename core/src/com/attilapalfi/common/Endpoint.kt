package com.attilapalfi.common

import java.net.InetAddress

/**
 * Created by palfi on 2016-01-14.
 */
open class Endpoint(public val IP: InetAddress,
                    public val port: Int) {
}