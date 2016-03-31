package com.attilapalfi.game

import com.attilapalfi.WORLD_HEIGHT
import com.attilapalfi.WORLD_WIDTH
import com.attilapalfi.game.entities.Attila
import com.attilapalfi.network.Client
import java.net.InetAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by 212461305 on 2016.03.17..
 */
class GameMap(private val lock: ReentrantLock) {

    var currentPosition: Float = 0f
    val preCalculationSize: Float = WORLD_WIDTH / 2

    var attilas: LinkedList<Attila> = LinkedList()

    init {
        val initialAttilaCount = 20
        for (i in 0..initialAttilaCount - 1) {
            val x: Float = (Math.random().toFloat() * WORLD_WIDTH) + preCalculationSize
            val y: Float = (Math.random().toFloat() * WORLD_HEIGHT * 3 / 4) + (WORLD_HEIGHT / 4)
            attilas.add(Attila(x, y))
        }
    }

    fun step(players: ConcurrentHashMap<InetAddress, Client>, cameraPos: Float, deltaT: Long) {
        lock.lock()
        val i = attilas.listIterator()
        while (i.hasNext()) {
            if (i.next().posX < cameraPos) {
                i.remove()
            }
        }

        attilas.forEach { it.step() }
        lock.unlock()
    }
}