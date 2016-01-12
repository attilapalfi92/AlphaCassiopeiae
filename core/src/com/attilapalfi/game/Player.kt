package com.attilapalfi.game

/**
 * Created by palfi on 2016-01-11.
 */
class Player(public val deviceName: String, public val androidId: String) {
    @Volatile
    public var x: Int = 0
    @Volatile
    public var y: Int = 0
    @Volatile
    public var speedX: Int = 0
    @Volatile
    public var speedY: Int = 0
    @Volatile
    public var health: Int = 100

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Player

        if (androidId != other.androidId) return false

        return true
    }

    override fun hashCode(): Int {
        return androidId.hashCode()
    }
}