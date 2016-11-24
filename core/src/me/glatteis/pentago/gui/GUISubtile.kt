package me.glatteis.pentago.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.logic.RotateDirection

/**
 * Created by Linus on 21.07.2016!
 */
class GUISubtile(val slotWidth: Int) : Actor() {

    val shapeRenderer = ShapeRenderer()

    var thisX = 0
    var thisY = 0

    val putSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sound/put.wav"))

    val displayedGUIChips = Array(slotWidth, {
        Array<GUIChip>(slotWidth, {
            NoGUIChip
        })
    })

    val pixelWidth = (slotWidth + 1) * GUIConstants.chipGap  + slotWidth * GUIConstants.chipRadius * 2
    val pixelShift = (slotWidth / 2) * (GUIConstants.chipRadius  * 2 + GUIConstants.chipGap )

    fun tap(screenX: Float, screenY: Float, count: Int, button: Int): Boolean {
        val coords = screenToLocalCoordinates(Vector2(screenX.toFloat(), screenY.toFloat()))
        if (Math.abs(coords.x) < pixelWidth / 2 && Math.abs(coords.y) < pixelWidth / 2) {
            var x = coords.x + pixelShift
            var y = coords.y + pixelShift
            x /= GUIConstants.chipGap  + GUIConstants.chipRadius  * 2
            y /= GUIConstants.chipGap  + GUIConstants.chipRadius  * 2
            val roundedX = Math.round(x)
            val roundedY = Math.round(y)
            if (roundedX >= 0 && roundedY >= 0 && roundedX < slotWidth && roundedY < slotWidth) {
                PentagoCore.connector.handleInput(thisX, thisY, roundedX, roundedY)
                return true
            }
        }
        return false
    }

    fun touchRotation(screenX: Float, screenY: Float, deltaX: Float, deltaY: Float): RotateDirection? {
        val coords = screenToLocalCoordinates(Vector2(screenX.toFloat(), screenY.toFloat())).rotate(rotation)
        //Apparently screenToLocalCoordinates does not take the rotation in count. It only took me 2 hours to
        //figure that out and I thought it was something wrong with the math below this comment!
        if (Math.abs(coords.x) < pixelWidth / 2 && Math.abs(coords.y) < pixelWidth / 2) {
            val dir = Vector2(deltaX, deltaY).nor()
            if (Math.abs(coords.x) > Math.abs(coords.y)) {
                if (coords.x * dir.y > 0) {
                    return RotateDirection.CLOCKWISE
                } else {
                    return RotateDirection.COUNTERCLOCKWISE
                }
            } else if (Math.abs(coords.y) > Math.abs(coords.x)) {
                if (coords.y * dir.x > 0) {
                    return RotateDirection.CLOCKWISE
                } else {
                    return RotateDirection.COUNTERCLOCKWISE
                }
            }
        }
        return null
    }

    override fun act(delta: Float) {
        super.act(delta)
        for (c in displayedGUIChips) for (chip in c) {
            if (!chip.big) {
                chip.makeBigger(delta)
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        shapeRenderer.identity()

        shapeRenderer.projectionMatrix = stage.batch.projectionMatrix
        shapeRenderer.transformMatrix = stage.batch.transformMatrix

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        shapeRenderer.color = Color.GRAY

        val position = localToStageCoordinates(Vector2(0F, 0F))

        shapeRenderer.translate(position.x, position.y, 0F)
        shapeRenderer.rotate(0F, 0F, 1F, rotation)

        shapeRenderer.roundedRect(-(pixelWidth / 2), -(pixelWidth / 2), pixelWidth, pixelWidth, GUIConstants.chipRadius)

        for (chips in displayedGUIChips.indices) for (chip in displayedGUIChips[0].indices) {
            val thisChip = displayedGUIChips[chips][chip]
            if (thisChip == NoGUIChip) {
                shapeRenderer.color = Color.DARK_GRAY
            } else {
                shapeRenderer.color = thisChip.color
            }
            shapeRenderer.circle(
                    GUIConstants.chipRadius * 2 * chips + GUIConstants.chipGap  * chips - pixelShift,
                    GUIConstants.chipRadius * 2 * chip + GUIConstants.chipGap  * chip - pixelShift,
                    thisChip.radius
            )

        }

        shapeRenderer.end()
    }

    fun addDisplayedGUIChip(x: Int, y: Int, chip: GUIChip) {
        putSound.play()
        displayedGUIChips[x][y] = chip
    }

    fun removeDisplayedGUIChip(x: Int, y: Int) {
        displayedGUIChips[x][y] = NoGUIChip
    }

}