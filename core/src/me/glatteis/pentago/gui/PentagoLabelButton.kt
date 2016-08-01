package me.glatteis.pentago.gui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Timer
import me.glatteis.pentago.generateRandomColor

/**
 * Created by Linus on 01.08.2016!
 */
class PentagoLabelButton(text: String, labelStyle: Label.LabelStyle) : Button(Label(text, labelStyle), ButtonStyle()) {

    var listener = ClickListener()

    val drawable = ColorDrawable(generateRandomColor(Color.WHITE))

    var clicked = false
    var elapsedTime = 0F
    var size = 0F
    var direction = 1

    var clickedInputEvent: InputEvent? = null
    var clickedX: Float? = null
    var clickedY: Float? = null

    init {
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                clicked = true
                clickedInputEvent = event
                clickedX = x
                clickedY = y
            }
        })
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (clicked) {
            elapsedTime += delta * 100F * direction
            size = Math.pow(elapsedTime.toDouble(), 2.0).toFloat()
            if (size >= width && direction == 1) {
                direction = -1
            } else if (elapsedTime <= 0 && direction == -1) {
                val x = clickedX
                val y = clickedY
                x ?: return
                y ?: return
                size = 0F
                elapsedTime = 0F
                clicked = false
                direction = 1
                drawable.color = generateRandomColor(drawable.color)
                listener.clicked(clickedInputEvent, x, y)
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch ?: return
        val coords = Vector2(localToParentCoordinates(Vector2(0F, 0F)))
        drawable.draw(batch, coords.x + width / 2 - size / 2, coords.y, size, height)
        super.draw(batch, parentAlpha)
    }



}