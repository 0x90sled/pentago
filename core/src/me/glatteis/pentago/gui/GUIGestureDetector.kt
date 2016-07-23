package me.glatteis.pentago.gui

import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Timer
import me.glatteis.pentago.PentagoCore

/**
 * Created by Linus on 21.07.2016!
 */

class GUIGestureDetector : GestureDetector.GestureListener {

    var block = false
    val timer = Timer()

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        if (block) return false
        for (s in PentagoCore.board.subtiles) for (subtile in s) {
            val touchRotation = subtile.touchRotation(x, y, deltaX, deltaY)
            if (touchRotation != null) {
                PentagoCore.connector.handleTurn(subtile.thisX, subtile.thisY, touchRotation)
                block()
                return true
            }
        }
        return false
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        if (block) return false
        for (s in PentagoCore.board.subtiles) for (subtile in s) {
            val done = subtile.tap(x, y, count, button)
            if (done) {
                block()
                return true
            }
        }
        return false
    }

    fun block() {
        block = true
        timer.scheduleTask(object : Timer.Task() {
            override fun run() {
                block = false
            }
        }, 0.4F)
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return false
    }



    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return false
    }

    override fun pinchStop() {

    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        return false
    }

}