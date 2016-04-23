package com.attilapalfi.controller

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-10.
 */
class AndroidController(private val controllerEventHandler: ControllerEventHandler,
                        private val controllerNotifier: ControllerNotifier,
                        override var address: InetAddress? = null) : Controller {

    override var name: String? = null
        @Synchronized
        get() = field
        @Synchronized
        set(value) {
            field = value
        }

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

    override fun vibrate(milliseconds: Int) {
        controllerNotifier.sendVibration(milliseconds)
    }

    override fun startSensorDataStream() {
        controllerNotifier.sendStartSensorDataStream()
    }

    override fun stopSensorDataStream() {
        controllerNotifier.sendStopSensorDataStream()
    }
}