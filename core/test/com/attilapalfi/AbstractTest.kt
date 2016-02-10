package com.attilapalfi

import com.attilapalfi.common.PORT
import com.attilapalfi.game.World
import com.attilapalfi.network.MessageBroadcaster
import com.attilapalfi.network.DiscoveryBroadcaster

/**
 * Created by palfi on 2016-01-13.
 */
abstract class AbstractTest {
    protected var maxPlayers = 2
    protected lateinit var world: World
    protected lateinit var messageBroadcaster: MessageBroadcaster

    open fun setup() {
        world = World()
        messageBroadcaster = DiscoveryBroadcaster(PORT, maxPlayers)
    }
}