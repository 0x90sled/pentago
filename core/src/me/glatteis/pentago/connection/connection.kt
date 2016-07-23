package me.glatteis.pentago.connection

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Timer
import com.esotericsoftware.kryo.KryoCopyable
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import me.glatteis.pentago.Pentago
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.GUIChip
import me.glatteis.pentago.logic.Player
import me.glatteis.pentago.logic.RotateDirection
import java.net.InetAddress
import java.util.*
import com.esotericsoftware.kryonet.Connection as KryoConnection

/**
 * Created by Linus on 23.07.2016!
 */

val tcpPort = 54555
val udpPort = 53639

class Connection {

    lateinit var server: Server

    fun createServer(uuid: UUID) {
        val server = Server()
        server.bind(tcpPort, udpPort)
        server.start()
        server.addListener(object : Listener() {
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

    fun addDisplayedGUIChip(subtileX: Int, subtileY: Int, x: Int, y: Int, chip: GUIChip) {
        (PentagoCore.connector as LocalConnector).addDisplayedGUIChip(subtileX, subtileY, x, y, chip)
        server.sendToAllTCP(AddDisplayedGUIChip(subtileX, subtileY, x, y, chip))
    }

    fun setTurnColor(color: Color) {
        (PentagoCore.connector as LocalConnector).setTurnColor(color)
        server.sendToAllTCP(SetTurnColor(color))
    }

    fun rotateSubtile(subtileX: Int, subtileY: Int, direction: RotateDirection) {
        (PentagoCore.connector as LocalConnector).rotateSubtile(subtileX, subtileY, direction)
        server.sendToAllTCP(RotateSubtile(subtileX, subtileY, direction))
    }

    fun displayGameWon(player: Player) {
        (PentagoCore.connector as LocalConnector).displayGameWon(player)
        server.sendToAllTCP(DisplayGameWon(player))
    }

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

class ClientConnector() : Connector {

    val client = Client()

    fun searchServers(callback: (Pair<String, InetAddress>) -> Unit) {
        client.start()
        val hosts = client.discoverHosts(udpPort, 5000)
        client.stop()
        for (host in hosts) {
            val clientForThis = Client()
            clientForThis.start()
            clientForThis.addListener(object : Listener() {
                override fun received(connection: KryoConnection, any: Any?) {
                    any ?: return
                    if (any is MyNameIs) {
                        callback(Pair(any.name, host))
                    }
                }
            })
            clientForThis.sendTCP(WhatsYourName())
            val timer = Timer()
            timer.scheduleTask(object : Timer.Task() {
                override fun run() {
                    clientForThis.stop()
                    clientForThis.dispose()
                }
            }, 5F)
        }

    }

    fun connectToServer(ip: String) {
        client.connect(5000, ip, tcpPort)
        client.addListener(object : Listener() {
            override fun received(connection: KryoConnection, any: Any?) {
                any ?: return
                with(any) {
                    when (this) {
                        is AddDisplayedGUIChip -> {
                            PentagoCore.board.subtiles[subtileX][subtileY].addDisplayedGUIChip(x, y, chip)
                        }
                        is SetTurnColor -> {
                            PentagoCore.board.turn(color)
                        }
                        is RotateSubtile -> {
                            PentagoCore.board.rotateSubtile(subtileX, subtileY, direction)
                        }
                        is DisplayGameWon -> {
                            PentagoCore.board.displayGameWon(player)
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