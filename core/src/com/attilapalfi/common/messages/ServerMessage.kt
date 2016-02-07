package com.attilapalfi.common.messages

import java.io.Serializable

/**
 * Created by palfi on 2016-01-15.
 */
class ServerMessage(public val messageType: Byte = 0) : Serializable

val DISCOVERY_BROADCAST: Byte = 0b0
val CLIENT_ACKNOWLEDGED: Byte = 0b10
val SHINE_1: Byte =             0b110
val SHINE_2: Byte =             0b1110
val VIBRATE_1: Byte =           0b11110
val VIBRATE_2: Byte =           0b111110

val MESSAGE_END: ByteArray = ByteArray(16, {i -> i.toByte()})