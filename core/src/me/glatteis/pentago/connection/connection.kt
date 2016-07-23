package me.glatteis.pentago.connection

import com.badlogic.gdx.graphics.Color
import com.esotericsoftware.kryonet.Server
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.GUIChip
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection

/**
 * Created by Linus on 23.07.2016!
 */

val tcpPort = 54555

class Connection {

    lateinit var server: Server

    fun createServer() {
        val server = Server()
        server.bind(tcpPort)
        server.start()
    }

    fun addDisplayedGUIChip(subtileX: Int, subtileY: Int, x: Int, y: Int, chip: GUIChip) {
        (PentagoCore.connector as LocalConnector).addDisplayedGUIChip(subtileX, subtileY, x, y, chip)
        //todo send packets
    }

    fun setTurnColor(color: Color) {
        (PentagoCore.connector as LocalConnector).setTurnColor(color)
        //todo send packets
    }

    fun rotateSubtile(subtileX: Int, subtileY: Int, direction: RotateDirection) {
        (PentagoCore.connector as LocalConnector).rotateSubtile(subtileX, subtileY, direction)
        //todo send packets
    }

    fun displayGameWon(player: Player) {
        (PentagoCore.connector as LocalConnector).displayGameWon(player)
        //todo send packets
    }

    //todo receive packets for input

}

interface Connector {

    fun handleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int)
    fun handleTurn(subtileX: Int, subtileY: Int, touchRotation: RotateDirection)

}

class LocalConnector : Connector {

    val connection = Connection()

    override fun handleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int) {
        PentagoCore.logic.handleInput(subtileX, subtileY, tileXRelative, tileYRelative)
    }

    override fun handleTurn(subtileX: Int, subtileY: Int, touchRotation: RotateDirection) {
        PentagoCore.logic.handleTurn(subtileX, subtileY, touchRotation)
    }

    fun addDisplayedGUIChip(subtileX: Int, subtileY: Int, x: Int, y: Int, chip: GUIChip) {
        PentagoCore.board.subtiles[subtileX][subtileY].addDisplayedGUIChip(x, y, chip)
    }

    fun setTurnColor(color: Color) {
        PentagoCore.board.turn(color)
    }

    fun rotateSubtile(subtileX: Int, subtileY: Int, direction: RotateDirection) {
        PentagoCore.board.rotateSubtile(subtileX, subtileY, direction)
    }

    fun displayGameWon(player: Player) {
        PentagoCore.board.displayGameWon(player)
    }

}

class ClientConnector(val ip: String) : Connector {

    override fun handleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int) {
        //todo send packet
    }

    override fun handleTurn(subtileX: Int, subtileY: Int, touchRotation: RotateDirection) {
        //todo send packet
    }

    //todo listen for packets

}