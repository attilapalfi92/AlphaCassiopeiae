package com.attilapalfi.controller

/**
 * Created by palfi on 2016-04-10.
 */
interface ControllerConnectionListener {
    fun controllerConnected(controller: Controller)
    fun controllerDisconnected(controller: Controller)
}