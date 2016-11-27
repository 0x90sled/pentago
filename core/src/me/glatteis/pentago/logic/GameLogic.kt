package me.glatteis.pentago.logic

import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.LocalConnector
import me.glatteis.pentago.gui.GUIChip
import java.util.*

/**
 * Created by Linus on 21.07.2016!
 */

class GameLogic(val tileWidth: Int, val width: Int, val height: Int, val players: List<Player>, val pointsToWin: Int) {

    var turnPlayer = (Math.random() * players.size).toInt()
    var mode = Mode.PUT
    val connection = (PentagoCore.connector as LocalConnector).connection
    var playerHasWon = false

    val board = Array(width) {
        Array(height) {
            Subtile(tileWidth)
        }
    }

    init {
        connection.setTurnPlayer(players[turnPlayer])
        if (players[turnPlayer] is AIPlayer) {
            (players[turnPlayer] as AIPlayer).turn(this)
        }
    }

    fun handleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int, isAI: Boolean = false) {
        if (mode == Mode.PUT && !playerHasWon && (players[turnPlayer] !is AIPlayer || isAI)) {
            if (board[subtileX][subtileY].board[tileXRelative][tileYRelative] != NoChip) return
            val newChip = Chip(players[turnPlayer])
            board[subtileX][subtileY].board[tileXRelative][tileYRelative] = newChip
            connection.addDisplayedGUIChip(subtileX, subtileY, tileXRelative, tileYRelative, GUIChip.fromChip(newChip))
            mode = Mode.ROTATE
            isGameOver()
        }
    }

    fun handleTurn(subtileX: Int, subtileY: Int, direction: RotateDirection, isAI: Boolean = false) {
        if (mode == Mode.ROTATE && !playerHasWon && (players[turnPlayer] !is AIPlayer || isAI)) {
            board[subtileX][subtileY].rotate(direction)
            mode = Mode.PUT
            connection.rotateSubtile(subtileX, subtileY, direction)
            if (isGameOver()) return
            newTurn()
        }
    }

    fun newTurn() {
        turnPlayer += 1
        turnPlayer %= players.size
        connection.setTurnPlayer(players[turnPlayer])
        if (players[turnPlayer] is AIPlayer) {
            (players[turnPlayer] as AIPlayer).turn(this)
        }
    }

    fun isGameOver(): Boolean {
        val playersThatWon = testIfWon()
        if (playersThatWon == null) {
            connection.displayGameWon(null)
            return true
        }
        if (playersThatWon.isNotEmpty()) {
            playerHasWon = true
            for (p in playersThatWon) {
                connection.displayGameWon(p)
            }
            return true
        }
        return false
    }

    //Returns an empty list if the game is not over yet.
    //Returns null if the game is over and no one won.
    fun testIfWon(): List<Player>? {
        val playersThatWon = ArrayList<Player>()
        var occupiedChips = 0
        for (r in 0..width - 1) for (h in 0..height - 1) {
            for (x in 0..tileWidth - 1) for (y in 0..tileWidth - 1) {
                val chip = board[r][h].getRotated(x, y)
                if (chip.player == null) continue
                occupiedChips++
                val player = chip.player
                for (i in -1..1) {
                    for (j in -1..1) {
                        var count = 0
                        var locationX = r * tileWidth + x
                        var locationY = h * tileWidth + y
                        if (i == 0 && j == 0) continue
                        while (locationX / tileWidth < width && locationY / tileWidth < height &&
                                locationX >= 0 && locationY >= 0 &&
                                board[locationX / tileWidth][(locationY / tileWidth)]
                                        .getRotated(locationX % tileWidth, locationY % tileWidth).player == player) {
                            locationX += i
                            locationY += j
                            count++
                        }
                        if (count == pointsToWin) {
                            playersThatWon.add(player)
                        }
                    }
                }
            }
        }
        if (occupiedChips == width * height * 3 * 3) {
            return null
        }
        return playersThatWon
    }

    fun printBoard(board: Array<Array<Subtile>>) {
        for (y in 0..board[0].size * 3 - 1) {
            println()
            if (y % 3 == 0) {
                for (x in 0..board.size * 3 - 1) {
                    print(board[x / 3][y / 3].rotationInDegrees)
                    print(" ")
                }
                println()
            }
            for (x in 0..board.size * 3 - 1) {
                if (x % 3 == 0) print(" ")
                val s = board[x / 3][y / 3].getRotated(x % 3, y % 3)
                if (s == NoChip) {
                    print(".")
                } else {
                    print(s.player!!.color.toString()[0])
                }
            }
        }
    }
}