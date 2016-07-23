package me.glatteis.pentago.gui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import me.glatteis.pentago.logic.Chip

/**
 * Created by Linus on 21.07.2016!
 */


fun ShapeRenderer.roundedRect(x: Float, y: Float, width: Float, height: Float, radius: Float) {
    //By J. Mac
    rect(x + radius, y + radius, width - 2 * radius, height - 2 * radius)
    rect(x + radius, y, width - 2 * radius, radius)
    rect(x + width - radius, y + radius, radius, height - 2 * radius)
    rect(x + radius, y + height - radius, width - 2 * radius, radius)
    rect(x, y + radius, radius, height - 2 * radius)

    arc(x + radius, y + radius, radius, 180f, 90f)
    arc(x + width - radius, y + radius, radius, 270f, 90f)
    arc(x + width - radius, y + height - radius, radius, 0f, 90f)
    arc(x + radius, y + height - radius, radius, 90f, 90f)
}

open class GUIChip(val color: Color) {
    open var radius = 0F
    private var timePassed = 0F
    open var big = false

    fun growth(x: Float) = Math.pow(x.toDouble() / 2, 2.toDouble()).toFloat()

    fun makeBigger(delta: Float) {
        timePassed += delta * 50
        radius = growth(timePassed)
        if (radius >= GUIConstants.chipRadius) {
            big = true
            radius = GUIConstants.chipRadius
        }
    }

    companion object {
        fun fromChip(chip: Chip): GUIChip {
            chip.player ?: return NoGUIChip
            return GUIChip(chip.player.color)
        }
    }

}

object NoGUIChip : GUIChip(Color.BLACK) {
    override var big = true
    override var radius = GUIConstants.chipRadius
}
