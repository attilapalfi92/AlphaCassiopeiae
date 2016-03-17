package com.attilapalfi.logic

/**
 * Created by palfi on 2016-01-11.
 */
class Player(@Volatile
             var x: Int = 0,
             @Volatile
             var y: Int = 0) {

    @Volatile
    var speedX: Float = 0f
    @Volatile
    var speedY: Float = 0f
    @Volatile
    var health: Int = 100
}