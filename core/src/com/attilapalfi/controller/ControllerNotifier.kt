package com.attilapalfi.controller

/**
 * Created by palfi on 2016-04-10.
 */
interface ControllerNotifier {
    fun sendVibration(milliseconds: Int)
    fun sendStartSensorDataStream()
    fun sendStopSensorDataStream()
}