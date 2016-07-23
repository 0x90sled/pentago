package me.glatteis.pentago.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection
import me.glatteis.pentago.menues.NewGameMenu
import java.util.*

/**
 * Created by Linus on 21.07.2016!
 */
class Board(val tileWidth: Int, val width: Int, val height: Int) : Stage(), Screen {

    val subtiles = Array(width, {
        Array<GUISubtile>(height, {
            GUISubtile(tileWidth)
        })
    })

    val boardGroup = Group()
    val popupGroup = Group()

    val subtileWidth = subtiles[0][0].pixelWidth

    val pixelWidth = subtileWidth * width + GUIConstants.subtileGap * (width - 1)
    val pixelHeight = subtileWidth * height + GUIConstants.subtileGap * (height - 1)
    val multiplexer = InputMultiplexer()

    init {
        multiplexer.addProcessor(GestureDetector(GUIGestureDetector()))
        multiplexer.addProcessor(object : InputAdapter() {
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                return false
            }
        })

        for (x in subtiles.indices) for (y in subtiles[0].indices) {
            val subtile = subtiles[x][y]
            subtile.thisX = x
            subtile.thisY = y
            boardGroup.addActor(subtile)
            subtile.setPosition(x * subtileWidth - pixelWidth / 2 + subtileWidth / 2 + if (x == 0) 0 else GUIConstants.subtileGap * x,
                    y * subtileWidth - pixelHeight / 2 + subtileWidth / 2 + if (y == 0) 0 else GUIConstants.subtileGap * y)
            subtile.setOrigin(subtile.width / 2, subtile.height / 2)
        }

        viewport = ExtendViewport(pixelWidth, pixelHeight)
        addActor(boardGroup)
        addActor(popupGroup)
    }

    fun rotateSubtile(x: Int, y: Int, direction: RotateDirection) {
        val subtile = subtiles[x][y]
        val degrees = if (direction == RotateDirection.CLOCKWISE) -90F else 90F
        subtile.addAction(Actions.rotateBy(degrees, 1F, Interpolation.pow2))
    }

    fun turn(color: Color) {
        println("Turning $color")
        addAction(object : Action() {
            val r = PentagoCore.backgroundColor.r
            val g = PentagoCore.backgroundColor.g
            val b = PentagoCore.backgroundColor.b
            val a = PentagoCore.backgroundColor.a

            var elapsedTime = 0F

            override fun act(delta: Float): Boolean {
                elapsedTime += delta
                PentagoCore.backgroundColor.r = elapsedTime * color.r + ((1 - elapsedTime) * r)
                PentagoCore.backgroundColor.g = elapsedTime * color.g + ((1 - elapsedTime) * g)
                PentagoCore.backgroundColor.b = elapsedTime * color.b + ((1 - elapsedTime) * b)
                PentagoCore.backgroundColor.a = elapsedTime * color.a + ((1 - elapsedTime) * a)
                if (elapsedTime >= 1) return true
                return false
            }
        })
    }

    fun displayGameWon(player: Player) {
        turn(Color.valueOf("FFFFFF"))
        val timer = Timer()
        timer.scheduleTask(object : Timer.Task(){
            override fun run() {
                PentagoCore.instance.screen = NewGameMenu
                NewGameMenu.displayWonPopup(player)
            }
        }, 2F)
    }

    override fun show() {
        Gdx.input.inputProcessor = multiplexer
    }

    override fun pause() {

    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun hide() {

    }

    override fun render(delta: Float) {
        super.act(delta)
        draw()
    }

    override fun resume() {

    }


}