package me.glatteis.pentago.connection

import com.badlogic.gdx.graphics.Color
import com.esotericsoftware.kryonet.EndPoint
import me.glatteis.pentago.gui.GUIChip
import me.glatteis.pentago.gui.NoGUIChip
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection
import java.util.*

/**
 * Created by Linus on 23.07.2016!
 */

/*
Kryonet forces classes to have empty constructors so I kind of cheated a bit. I just didn't want to null-check everything.
 */

//Packets that go from server to clients

class AddDisplayedGUIChip(var subtileX: Int, var subtileY: Int, var x: Int, var y: Int, var chipColor: Color) {
    constructor() : this(0, 0, 0, 0, Color())
}

class SetTurnPlayer(var player: Player) {
    constructor() : this(Player(Color(), ""))
}

class RotateSubtile(var subtileX: Int, var subtileY: Int, var direction: RotateDirection) {
    constructor() : this(0, 0, RotateDirection.CLOCKWISE)
}

class DisplayGameWon(var player: Player) {
    constructor() : this(Player(Color.WHITE, ""))
}

class LetsGo(var width: Int, var height: Int, val players: Array<Player>) {
    constructor() : this(0, 0, emptyArray())
}

//Packets that go from clients to server

class HandleInput(var subtileX: Int, var subtileY: Int, var tileXRelative: Int, var tileYRelative: Int) {
    constructor() : this(0, 0, 0, 0)
}

class HandleTurn(var subtileX: Int, var subtileY: Int, var touchRotation: RotateDirection) {
    constructor() : this(0, 0, RotateDirection.CLOCKWISE)
}

//Packets for discovery

class WhatsYourName
class MyNameIs() {
    var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }
}

object PacketRegistrar {
    fun registerPacketsFor(endpoint: EndPoint) {
        val kryo = endpoint.kryo
        val classes = listOf(AddDisplayedGUIChip::class.java, SetTurnPlayer::class.java, RotateSubtile::class.java,
                DisplayGameWon::class.java, LetsGo::class.java, HandleInput::class.java, HandleTurn::class.java,
                WhatsYourName::class.java, MyNameIs::class.java, RotateDirection::class.java, Color::class.java,
                Player::class.java, UUID::class.java, Array<Player>::class.java)
        for (c in classes) {
            kryo.register(c)
        }
    }
}