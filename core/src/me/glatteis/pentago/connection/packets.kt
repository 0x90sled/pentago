package me.glatteis.pentago.connection

import com.badlogic.gdx.graphics.Color
import me.glatteis.pentago.gui.GUIChip
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection

/**
 * Created by Linus on 23.07.2016!
 */

//Packets that go from server to clients

class AddDisplayedGUIChip(subtileX: Int, subtileY: Int, x: Int, y: Int, chip: GUIChip)
class SetTurnColor(color: Color)
class RotateSubtile(subtileX: Int, subtileY: Int, direction: RotateDirection)
class DisplayGameWon(player: Player)

//Packets that go from clients to server

class HandleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int)
class HandleTurn(subtileX: Int, subtileY: Int, touchRotation: RotateDirection)