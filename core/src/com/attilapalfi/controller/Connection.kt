package com.attilapalfi.controller

/**
 * Created by palfi on 2016-04-10.
 */
interface Connection {
    fun sendVibration(milliseconds: Int);
    fun sendGlow(milliseconds: Int);
    fun sendStartSensorDataStream();
    fun sendStopSensorDataStream();
}