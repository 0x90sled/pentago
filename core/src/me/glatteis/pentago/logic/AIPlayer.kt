package me.glatteis.pentago.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Timer
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by Linus on 23.11.2016!
 */
class AIPlayer(color: Color, name: String) : Player(color, name) {

    val horizon = 2
    val timer = Timer()
    var thisPlayer = 0

    var enemyMoves = 0

    //Get the "best" turn using the minimax algorithm
    fun turn(gameLogic: GameLogic) {
        enemyMoves = 0
        thisPlayer = gameLogic.players.indexOf(this)
        val elapsedTime = System.currentTimeMillis()
        thread {
            val turn = max(thisPlayer, gameLogic.players, gameLogic.board)
            Gdx.app.postRunnable {
                if (System.currentTimeMillis() - elapsedTime < 1000) {
                    timer.scheduleTask(object : Timer.Task() {
                        override fun run() {
                            gameLogic.handleInput(turn.first.first, turn.first.second, turn.second.first, turn.second.second, true)
                            gameLogic.handleTurn(turn.third.first, turn.third.second, turn.third.third, true)
                        }
                    }, (1000 - (System.currentTimeMillis() - elapsedTime)).toFloat() / 1000F)
                } else {
                    gameLogic.handleInput(turn.first.first, turn.first.second, turn.second.first, turn.second.second, true)
                    gameLogic.handleTurn(turn.third.first, turn.third.second, turn.third.third, true)
                }
            }
        }
    }

    fun max(player: Int, players: List<Player>, board: Array<Array<Subtile>>):
            Triple<Pair<Int, Int>, Pair<Int, Int>, Triple<Int, Int, RotateDirection>> {
        var maxValue = -Int.MAX_VALUE

        val savedTurns = HashSet<Triple<Pair<Int, Int>, Pair<Int, Int>, Triple<Int, Int, RotateDirection>>>()

        //Place a marble.
        for (r in 0..board.size - 1) for (c in 0..board[0].size - 1) {
            for (x in 0..2) for (y in 0..2) {
                if (board[r][c].board[x][y] != NoChip) continue
                board[r][c].board[x][y] = Chip(players[player])

                //Spin a tile.
                for (rSpin in 0..board.size - 1) {
                    for (cSpin in 0..board[0].size - 1) {
                        for (spinDirection in listOf(RotateDirection.CLOCKWISE, RotateDirection.COUNTERCLOCKWISE)) {
                            board[rSpin][cSpin].rotate(spinDirection)

                            val nextPlayer = (player + 1) % players.size
                            val value = min(nextPlayer, players, board)

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

    fun min(player: Int, players: List<Player>, board: Array<Array<Subtile>>): Int {
        var minValue = Int.MAX_VALUE



        //Place a marble.
        for (r in 0..board.size - 1) for (c in 0..board[0].size - 1) {
            for (x in 0..2) for (y in 0..2) {
                if (board[r][c].board[x][y] != NoChip) continue
                board[r][c].board[x][y] = Chip(players[player])

                //Spin a tile.
                for (rSpin in 0..board.size - 1) {
                    for (cSpin in 0..board[0].size - 1) {
                        for (spinDirection in listOf(RotateDirection.CLOCKWISE, RotateDirection.COUNTERCLOCKWISE)) {
                            board[rSpin][cSpin].rotate(spinDirection)
                            enemyMoves++

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


    val tileWidth = 3
    fun value(board: Array<Array<Subtile>>): Int {
        val width = board.size
        val height = board[0].size
        val allRotatedSubtiles = Array(width, {
            Array(height, {
                Array(tileWidth, {
                    Array<Chip>(tileWidth, {
                        NoChip
                    })
                })
            })
        })
        for (a in 0..width - 1) for (b in 0..height - 1) {
            allRotatedSubtiles[a][b] = board[a][b].rotated()
        }

        val maxCounts = HashMap<Player, Int>()

        for (r in 0..width - 1) for (h in 0..height - 1) {
            for (x in 0..tileWidth - 1) for (y in 0..tileWidth - 1) {
                val chip = allRotatedSubtiles[r][h][x][y]
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
                                allRotatedSubtiles[locationX / tileWidth][locationY / tileWidth]
                                        [locationX % tileWidth][locationY % tileWidth].player == player) {
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
                println("${player.name} could win! Give it a $value")
            } else {
                value += count * factor
            }
        }
        return value
    }
}