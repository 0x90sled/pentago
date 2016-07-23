package me.glatteis.pentago

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