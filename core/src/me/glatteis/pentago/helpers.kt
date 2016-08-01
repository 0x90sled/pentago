package me.glatteis.pentago

import com.badlogic.gdx.graphics.Color
import java.util.*

/**
 * Created by Linus on 23.07.2016!
 */

fun floorMod(a: Int, b: Int): Int {
    val rem = a % b
    if (rem < 0) {
        return rem + b
    }
    return rem
}

fun generateRandomColor(mix: Color): Color {
    val random = Random()
    var red = random.nextFloat()
    var green = random.nextFloat()
    var blue = random.nextFloat()

    red = (red + mix.r) / 2
    green = (green + mix.g) / 2
    blue = (blue + mix.b) / 2

    val color = Color(red, green, blue, 1F)
    return color
}