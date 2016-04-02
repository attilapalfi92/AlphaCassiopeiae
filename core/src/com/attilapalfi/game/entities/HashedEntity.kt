package com.attilapalfi.game.entities

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by palfi on 2016-04-02.
 */
open class HashedEntity {

    companion object {
        val instanceCounter = AtomicInteger(0)
    }

    private val hashCode: Int = instanceCounter.incrementAndGet()

    override fun hashCode(): Int {
        return hashCode
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as HashedEntity

        if (hashCode != other.hashCode) return false

        return true
    }
}
