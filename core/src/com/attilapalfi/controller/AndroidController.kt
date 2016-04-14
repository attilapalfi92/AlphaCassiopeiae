package com.attilapalfi.controller

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-10.
 */
class AndroidController(private val controllerEventHandler: ControllerEventHandler,
                        private val controllerNotifier: ControllerNotifier,
                        override var name: String? = null,
                        override var address: InetAddress? = null) : Controller {

    override fun aPressed() {
        controllerEventHandler.onApressed(this)
    }

    override fun bPressed() {
        controllerEventHandler.onBpressed(this)
    }

    override fun xPressed() {
        controllerEventHandler.onXpressed(this)
    }

    override fun yPressed() {
        controllerEventHandler.onYpressed(this)
    }

    override fun vibrate(milliseconds: Long) {
        controllerNotifier.vibrate(milliseconds)
    }
}