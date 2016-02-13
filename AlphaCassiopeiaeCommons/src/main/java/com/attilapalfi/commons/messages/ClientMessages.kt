package com.attilapalfi.commons.messages

import java.io.Serializable

/**
 * Created by palfi on 2016-01-15.
 */
class UdpSensorData(public val type: Byte,
                    public val x: Float = 0f,
                    public val y: Float = 0f) : Serializable

class TcpClientMessage(public val messageType: Byte = 0,
                       public val deviceName: String? = null) : Serializable

val SENSOR_DATA: Byte   = 0b0
val REGISTRATION: Byte  = 0b10
val START: Byte         = 0b110
val SHOOT: Byte         = 0b1110
val SHITBOMB: Byte      = 0b11110
val PAUSE: Byte         = 0b111110
val RESUME: Byte        = 0b1111110