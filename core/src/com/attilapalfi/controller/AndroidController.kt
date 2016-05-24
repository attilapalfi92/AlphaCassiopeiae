package com.attilapalfi.controller

import java.net.InetAddress

/**
 * Created by palfi on 2016-04-10.
 */
class AndroidController(private val controllerInputHandler: ControllerInputHandler,
                        private val connection: Connection,
                        override var address: InetAddress? = null) : Controller {

    @Volatile
    override var name: String? = null;
//        @Synchronized
//        get() = field
//        @Synchronized
//        set(value) {
//            field = value
//        }

    override fun aPressed() {
        controllerInputHandler.onApressed(this);
    }

    override fun bPressed() {
        controllerInputHandler.onBpressed(this);
    }

    override fun xPressed() {
        controllerInputHandler.onXpressed(this);
    }

    override fun yPressed() {
        controllerInputHandler.onYpressed(this);
    }

    override fun vibrate(milliseconds: Int) {
        connection.sendVibration(milliseconds);
    }

    override fun glow(milliseconds: Int) {
        connection.sendGlow(milliseconds);
    }

    override fun startSensorDataStream() {
        connection.sendStartSensorDataStream();
    }

    override fun stopSensorDataStream() {
        connection.sendStopSensorDataStream();
    }
}