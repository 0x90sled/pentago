package me.glatteis.pentago.logic

import com.badlogic.gdx.graphics.Color
import me.glatteis.pentago.floorMod
import java.util.*

/**
 * Created by Linus on 21.07.2016!
 */

class Subtile(val width: Int) {
    var board = Array(width, {
        Array<Chip>(width, {
            NoChip
        })
    })

    var rotationInDegrees = 0

    fun rotate(direction: RotateDirection) {
        rotationInDegrees += if (direction == RotateDirection.CLOCKWISE) -90 else 90
        rotationInDegrees = floorMod(rotationInDegrees, 360)
    }

    val cos = mapOf(
            Pair(0, 1),
            Pair(90, 0),
            Pair(180, -1),
            Pair(270, 0)
    )

    val sin = mapOf(
            Pair(0, 0),
            Pair(90, 1),
            Pair(180, 0),
            Pair(270, -1)
    )

    fun getRotated(x: Int, y: Int): Chip {
        val s = -sin[rotationInDegrees]!!
        val c = -cos[rotationInDegrees]!!
        return board[(1 - x) * c - (1 - y) * s + 1][(1 - x) * s + (1 - y) * c + 1]
    }


}


enum class Mode {
    PUT, ROTATE
}

open class Chip(val player: Player?) {

}

object NoChip: Chip(null)

open class Player(val color: Color, val name: String) {
    //Empty constructor for packets
    constructor() : this(Color(), "")
}

enum class RotateDirection {
    CLOCKWISE, COUNTERCLOCKWISE
}