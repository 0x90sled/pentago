package me.glatteis.pentago.connection

import com.badlogic.gdx.graphics.Color
import me.glatteis.pentago.gui.GUIChip
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection

/**
 * Created by Linus on 23.07.2016!
 */

//Packets that go from server to clients

class AddDisplayedGUIChip(val subtileX: Int, val subtileY: Int, val x: Int, val y: Int, val chip: GUIChip)
class SetTurnColor(val color: Color)
class RotateSubtile(val subtileX: Int, val subtileY: Int, val direction: RotateDirection)
class DisplayGameWon(val player: Player)

//Packets that go from clients to server

class HandleInput(val subtileY: Int, val subtileX: Int, val tileXRelative: Int, val tileYRelative: Int)
class HandleTurn(val subtileX: Int, val subtileY: Int, val touchRotation: RotateDirection)

//Packets for discovery

class WhatsYourName
class MyNameIs(val name: String)