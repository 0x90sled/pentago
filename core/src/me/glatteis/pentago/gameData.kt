package me.glatteis.pentago

import com.badlogic.gdx.graphics.Color
import me.glatteis.pentago.connection.Connector
import me.glatteis.pentago.gui.Board
import me.glatteis.pentago.logic.GameLogic

/**
 * Created by Linus on 21.07.2016!
 */


object PentagoCore {
    lateinit var board: Board               //Represents the GUI
    lateinit var logic: GameLogic           //Represents the logic (not there when in client mode)
    lateinit var connector: Connector       //Receives and sends signals
    lateinit var instance: Pentago
    var backgroundColor: Color = Color.WHITE
}