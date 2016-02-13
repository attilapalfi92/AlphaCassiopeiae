package com.attilapalfi.commons.messages

import java.io.Serializable

/**
 * Created by palfi on 2016-01-15.
 */
class UdpDiscoveryBroadcast : Serializable

class TcpServerMessage(public val messageType: Byte = 0) : Serializable

val DISCOVERY_BROADCAST: Byte   = 0b0
val REG_ACK: Byte               = 0b10
val START_ACK: Byte             = 0b110
val PAUSE_ACK: Byte             = 0b1110
val RESUME_ACK: Byte            = 0b11110
val SHINE: Byte                 = 0b111110
val VIBRATE: Byte               = 0b1111110

val MESSAGE_END: ByteArray = ByteArray(16, { i -> i.toByte() })