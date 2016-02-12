package com.attilapalfi.game

/**
 * Created by palfi on 2016-01-11.
 */
class Player(@Volatile
             public var x: Int = 0,
             @Volatile
             public var y: Int = 0) {

    @Volatile
    public var speedX: Float = 0f
    @Volatile
    public var speedY: Float = 0f
    @Volatile
    public var health: Int = 100
}