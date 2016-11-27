package me.glatteis.pentago.menues

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import me.glatteis.pentago.generateRandomColor
import me.glatteis.pentago.gui.*
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection
import kotlin.system.exitProcess

/**
 * Created by Linus on 27.11.2016!
 */
class Tutorial : Board(3, 2, 2, MainMenu, arrayOf(Player(generateRandomColor(Color.WHITE), "Player 1"),
        Player(generateRandomColor(Color.WHITE), "Player 2"), Player(Color.WHITE, ""))) {

    val timer = Timer()

    init {

        val table = Table()
        table.background = BandColorDrawable(Color.WHITE, pixelHeight)
        table.width = pixelHeight

        val explainLabel = Label("Pentago's rules are pretty simple.", Label.LabelStyle(Textures.montserratMedium, Color.BLACK))
        explainLabel.setWrap(true)
        explainLabel.width = pixelHeight
        explainLabel.setAlignment(Align.center)
        table.add(explainLabel).align(Align.center).expand()

        playerList.row()
        playerList.add(table).align(Align.center)

        setTurnColor(players[0].color)

        for (p in multiplexer.processors) {
            multiplexer.removeProcessor(p)
        }


        schedule(5F) {
            explainLabel.setText("When it's your turn:")
        }

        schedule(10F) {
            explainLabel.setText("Place a marble on the board,")
            subtiles[0][0].addDisplayedGUIChip(0, 1, GUIChip(players[0].color))
        }

        schedule(15F) {
            explainLabel.setText("then rotate a tile on the board.")
        }

        schedule(20F) {
            rotateSubtile(0, 0, RotateDirection.CLOCKWISE)
            setTurnColor(players[1].color)
        }

        schedule(23F) {
            explainLabel.setText("The next player follows.")
            subtiles[1][0].addDisplayedGUIChip(1, 1, GUIChip(players[1].color))
        }

        schedule(25F) {
            rotateSubtile(1, 1, RotateDirection.COUNTERCLOCKWISE)
            setTurnColor(players[0].color)
        }

        schedule(30F) {
            explainLabel.setText("5 marbles in a row win.")
        }

        schedule(35F) {
            setTurnColor(Color.WHITE)
            explainLabel.setText("Have fun!")
        }

        schedule(40F) {
            returnToMenu()
        }

    }

    fun schedule(delay: Float, task: () -> Unit) {
        timer.scheduleTask(object : Timer.Task() {
            override fun run() {
                task()
            }
        }, delay)
    }

}