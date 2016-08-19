package com.attilapalfi.game.levels

import com.attilapalfi.game.Renderable
import com.attilapalfi.game.Steppable
import com.attilapalfi.game.entities.Player
import com.badlogic.gdx.maps.Map
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractMap(protected val players: ConcurrentHashMap<InetAddress, Player>) :
        Map(), Steppable, Renderable;