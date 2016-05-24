package com.attilapalfi.controller

/**
 * Created by palfi on 2016-04-14.
 */
interface ControllerInputHandler {
    fun onApressed(controller: Controller)
    fun onBpressed(controller: Controller)
    fun onXpressed(controller: Controller)
    fun onYpressed(controller: Controller)
}