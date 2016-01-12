package com.attilapalfi.common

/**
 * Created by palfi on 2016-01-13.
 */
enum class Message(public val string: String) {
    SEPARATOR("___"),
    SERVER_DISCOVERY_MESSAGE("Alpha Cassiopeiae server discovery message"),
    CLIENT_REGISTRATION("Alpha Cassiopeiae client registration"),
    ANDROID_ID("aid"),
    DEVICE_NAME("dev name"),
    ACKNOWLEDGED_CLIENT("Client registration acknowledged"),
    SENSOR_DATA("sensor data");
}