package me.glatteis.pentago.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by Linus on 23.11.2016!
 */
class AIPlayer(color: Color, name: String) : Player(color, name) {

    var firstTurn = true

    constructor() : this(Color(), "")

    var thisPlayer = 0

    //Get the "best" turn using the minimax algorithm
    fun turn(gameLogic: GameLogic) {
        thisPlayer = gameLogic.players.indexOf(this)

        thread {
            val turn = max(thisPlayer, gameLogic.players, gameLogic.board)
            firstTurn = false
            Gdx.app.postRunnable {
                gameLogic.handleInput(turn.first.first, turn.first.second, turn.second.first, turn.second.second, true)
                gameLogic.handleTurn(turn.third.first, turn.third.second, turn.third.third, true)
            }
        }
    }

    fun max(player: Int, players: List<Player>, board: Array<Array<Subtile>>):
            Triple<Pair<Int, Int>, Pair<Int, Int>, Triple<Int, Int, RotateDirection>> {

        var maxValue = -Int.MAX_VALUE
        val chip = Chip(players[player])

        val savedTurns = HashSet<Triple<Pair<Int, Int>, Pair<Int, Int>, Triple<Int, Int, RotateDirection>>>()

        val width = board.size
        val height = board[0].size

        //Place a marble.
        for (r in 0..width - 1) for (c in 0..height - 1) {
            for (x in 0..2) for (y in 0..2) {
                if (board[r][c].board[x][y] != NoChip) continue

                if (!firstTurn && !chipInArea(board, r * 3 + x, c * 3 + y, width * 3, height * 3)) {
                    continue
                }

                board[r][c].board[x][y] = chip

                //Spin a tile.
                for (rSpin in 0..width - 1) {
                    for (cSpin in 0..height - 1) {
                        for (spinDirection in RotateDirection.values()) {

                            if (firstTurn) {
                                savedTurns += Triple(Pair(r, c), Pair(x, y), Triple(rSpin, cSpin, spinDirection))
                                continue
                            }

                            board[rSpin][cSpin].rotate(spinDirection)

                            val nextPlayer = (player + 1) % players.size
                            val value = min(nextPlayer, players, board, width, height)

                            board[rSpin][cSpin].rotate(
                                    if (spinDirection == RotateDirection.CLOCKWISE) RotateDirection.COUNTERCLOCKWISE
                                    else RotateDirection.CLOCKWISE
                            )

                            if (value > maxValue) {
                                savedTurns.clear()
                                maxValue = value
                                savedTurns += Triple(Pair(r, c), Pair(x, y), Triple(rSpin, cSpin, spinDirection))
                            } else if (value == maxValue) {
                                savedTurns += Triple(Pair(r, c), Pair(x, y), Triple(rSpin, cSpin, spinDirection))
                            }
                        }
                    }
                }

                board[r][c].board[x][y] = NoChip
            }
        }
        return savedTurns.elementAt(MathUtils.random(0, savedTurns.size - 1))
    }

    fun min(player: Int, players: List<Player>, board: Array<Array<Subtile>>, width: Int, height: Int): Int {
        var minValue = Int.MAX_VALUE
        val chip = Chip(players[player])

        //Place a marble.
        for (r in 0..width - 1) for (c in 0..height - 1) {
            for (x in 0..2) for (y in 0..2) {
                if (board[r][c].board[x][y] != NoChip) continue
                board[r][c].board[x][y] = chip

                //Spin a tile.
                for (rSpin in 0..width - 1) {
                    for (cSpin in 0..height - 1) {
                        for (spinDirection in RotateDirection.values()) {
                            board[rSpin][cSpin].rotate(spinDirection)

                            val value = value(board)

                            if (value < minValue) {
                                minValue = value
                            }

                            board[rSpin][cSpin].rotate(
                                    if (spinDirection == RotateDirection.CLOCKWISE) RotateDirection.COUNTERCLOCKWISE
                                    else RotateDirection.CLOCKWISE
                            )
                        }
                    }
                }

                board[r][c].board[x][y] = NoChip
            }
        }
        return minValue
    }

    fun chipInArea(board: Array<Array<Subtile>>, x: Int, y: Int, w: Int, h: Int): Boolean {
        for (xi in x - 1..x + 1) {
            for (yi in y - 1..y + 1) {
                if (xi > 0 && yi > 0 && xi < w && yi < h && board[xi / 3][yi / 3].getRotated(xi % 3, yi % 3) != NoChip) {
                    return true
                }
            }
        }
        return false
    }

    val tileWidth = 3
    val maxCounts = HashMap<Player, Int>()
    fun value(board: Array<Array<Subtile>>): Int {
        maxCounts.clear()
        val width = board.size
        val height = board[0].size

        for (r in 0..width - 1) for (h in 0..height - 1) {
            for (x in 0..tileWidth - 1) for (y in 0..tileWidth - 1) {
                val chip = board[r][h].getRotated(x, y)
                if (chip.player == null) continue
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
                        if (maxCounts[player] == null || maxCounts[player]!! < count) {
                            maxCounts[player] = count
                        }
                    }
                }
            }
        }
        var value = 0
        for ((player, count) in maxCounts) {
            val factor = if (player == this) 2 else -1
            if (count == 5) {
                value += (Int.MAX_VALUE / 2) * factor
            } else {
                value += count * factor
            }
        }
        return value
    }
}