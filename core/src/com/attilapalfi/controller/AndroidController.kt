package com.attilapalfi.controller

/**
 * Created by palfi on 2016-04-10.
 */
class AndroidController(private val controlEventSender: ControlEventSender) : Controller {

    override fun aPressed() {
        throw UnsupportedOperationException()
    }

    override fun bPressed() {
        throw UnsupportedOperationException()
    }

    override fun xPressed() {
        throw UnsupportedOperationException()
    }

    override fun yPressed() {
        throw UnsupportedOperationException()
    }

    override fun vibrate(milliseconds: Long) {
        controlEventSender.vibrate(milliseconds)
    }

    override fun address(): String {
        throw UnsupportedOperationException()
    }

    override fun name(): String {
        throw UnsupportedOperationException()
    }
}