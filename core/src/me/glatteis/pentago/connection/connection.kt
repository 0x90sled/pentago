package me.glatteis.pentago.connection

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.Board
import me.glatteis.pentago.gui.GUIChip
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection
import me.glatteis.pentago.menues.WaitMenu
import java.net.InetAddress
import java.util.*
import com.esotericsoftware.kryonet.Connection as KryoConnection

/**
 * Created by Linus on 23.07.2016!
 */

val tcpPort = 54555
val udpPort = 53639

class Connection {

    var server: Server? = null

    fun createServer(uuid: UUID) {
        server = Server()
        //Sorry for these !!'s, Kotlin
        PacketRegistrar.registerPacketsFor(server!!)
        server!!.start()
        server!!.bind(tcpPort, udpPort)
        server!!.addListener(object : Listener() {
            override fun received(connection: KryoConnection, any: Any?) {
                any ?: return
                with(any) {
                    if (this is HandleInput) {
                        PentagoCore.logic.handleInput(subtileX, subtileY, tileXRelative, tileYRelative)
                    } else if (this is HandleTurn) {
                        PentagoCore.logic.handleTurn(subtileX, subtileY, touchRotation)
                    } else if (this is WhatsYourName) {
                        connection.sendTCP(MyNameIs(uuid.toString()))
                    }
                }
            }
        })
    }

    fun setTurnColor(color: Color) {
        (PentagoCore.connector as LocalConnector).setTurnColor(color)
        sendPacketToAll(SetTurnColor(color))
    }

    fun addDisplayedGUIChip(subtileX: Int, subtileY: Int, x: Int, y: Int, chip: GUIChip) {
        (PentagoCore.connector as LocalConnector).addDisplayedGUIChip(subtileX, subtileY, x, y, chip)
        sendPacketToAll(AddDisplayedGUIChip(subtileX, subtileY, x, y, chip.color))
    }

    fun rotateSubtile(subtileX: Int, subtileY: Int, direction: RotateDirection) {
        (PentagoCore.connector as LocalConnector).rotateSubtile(subtileX, subtileY, direction)
        sendPacketToAll(RotateSubtile(subtileX, subtileY, direction))
    }

    fun displayGameWon(player: Player) {
        (PentagoCore.connector as LocalConnector).displayGameWon(player)
        sendPacketToAll(DisplayGameWon(player))

    }

    fun startGame(width: Int, height: Int) {
        sendPacketToAll(LetsGo(width, height))
    }

    fun sendPacketToAll(packet: Any) {
        server ?: return
        println(server!!.connections)
        for (connection in server!!.connections) {
            connection.sendTCP(packet)
        }
    }

}

fun postRunnable(runnable: () -> Unit) {
    Gdx.app.postRunnable(runnable)
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
        println("Adding to subtile $subtileX, $subtileY")
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

class ClientConnector() : Connector {

    val client = Client()

    fun connectToServer(ip: InetAddress) {
        PacketRegistrar.registerPacketsFor(client)
        client.start()
        client.connect(5000, ip, tcpPort, udpPort)
        client.addListener(object : Listener() {
            override fun received(connection: KryoConnection, any: Any?) {
                any ?: return
                with(any) {
                    when (this) {
                        is AddDisplayedGUIChip -> {
                            postRunnable {
                                PentagoCore.board.subtiles[subtileX][subtileY].addDisplayedGUIChip(x, y, GUIChip(chipColor))
                            }
                        }
                        is SetTurnColor -> {
                            postRunnable {
                                PentagoCore.board.turn(color)
                            }
                        }
                        is RotateSubtile -> {
                            postRunnable {
                                PentagoCore.board.rotateSubtile(subtileX, subtileY, direction)
                            }
                        }
                        is DisplayGameWon -> {
                            postRunnable {
                                PentagoCore.board.displayGameWon(player)
                            }
                            client.stop()
                        }
                        is LetsGo -> {
                            postRunnable {
                                PentagoCore.board = Board(3, this.width, this.height, WaitMenu())
                                PentagoCore.instance.screen = PentagoCore.board
                            }
                        }
                    }
                }
            }
        })
    }

    override fun handleInput(subtileX: Int, subtileY: Int, tileXRelative: Int, tileYRelative: Int) {
        client.sendTCP(HandleInput(subtileX, subtileY, tileXRelative, tileYRelative))
    }

    override fun handleTurn(subtileX: Int, subtileY: Int, touchRotation: RotateDirection) {
        client.sendTCP(HandleTurn(subtileX, subtileY, touchRotation))
    }

}