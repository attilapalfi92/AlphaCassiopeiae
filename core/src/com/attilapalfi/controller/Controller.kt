package com.attilapalfi.controller

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-10.
 */
interface Controller {
    fun aPressed()
    fun bPressed()
    fun xPressed()
    fun yPressed()
    fun vibrate(milliseconds: Long)
    fun address(): InetAddress
    fun name(): String
}