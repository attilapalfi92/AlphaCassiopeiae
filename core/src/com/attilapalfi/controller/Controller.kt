package com.attilapalfi.controller

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-10.
 */
interface Controller {

    var address: InetAddress?
    var name: String?

    fun aPressed()
    fun bPressed()
    fun xPressed()
    fun yPressed()
    fun vibrate(milliseconds: Long)
}