package com.attilapalfi.various

import com.attilapalfi.game.entities.Attila
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by palfi on 2016-04-02.
 */
class ConcurrentHashMapTest {

    private val testMap: MutableMap<Attila, Int> = ConcurrentHashMap()

    @Before
    fun initMap() {
        for(i in 0..9) {
            testMap.put(Attila(i.toFloat(), i.toFloat()), i)
        }
    }

    @Test
    fun testRemoveInForeach() {
        testMap.forEach {
            if (it.value % 2 == 0) {
                testMap.remove(it.key)
            }
        }

        Assert.assertEquals(5, testMap.size)
    }
}