package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.*
import me.glatteis.pentago.gui.PentagoLabelButton
import me.glatteis.pentago.gui.Textures
import java.net.InetAddress
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by Linus on 23.07.2016!
 */
class ConnectMenu : MenuStage() {

    val list = Table()
    var searching = false
    val connector = ClientConnector()

    init {
        PentagoCore.connector = connector

        val title = Label("CONNECT TO SERVER", Label.LabelStyle(Textures.vanadineBig, Color.BLACK))
        title.setPosition(0F, 800F, Align.center)
        addActor(title)

        val scrollPane = ScrollPane(list)
        scrollPane.width = 2000F
        scrollPane.height = 1000F
        scrollPane.setPosition(0F, 400F, Align.center)
        addActor(scrollPane)

        thread {
            searchServers()
        }

        val back = PentagoLabelButton("Back", Label.LabelStyle(Textures.montserratMedium, Color.BLACK))
        back.setPosition(0F, -800F, Align.center)
        back.listener = (object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PentagoCore.instance.screen = MainMenu
            }
        })
        addActor(back)
    }

    fun addServerToList(pair: Pair<String, InetAddress>) {
        val button = Button(Label(pair.first, Label.LabelStyle(Textures.montserratSmall, Color.BLACK)), Button.ButtonStyle())
        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                thread {
                    connector.connectToServer(pair.second)
                }
                PentagoCore.instance.screen = WaitMenu()
            }
        })
        list.add(button)
        list.row()
    }

    fun searchServers() {
        searching = true
        val foundServers = ArrayList<String>()
        val client = Client()
        client.start()
        val hosts = client.discoverHosts(udpPort, 5000)
        client.stop()
        for (host in hosts) {
            val clientForThis = Client()
            PacketRegistrar.registerPacketsFor(clientForThis)
            clientForThis.start()
            clientForThis.connect(5000, host, tcpPort, udpPort)
            clientForThis.addListener(object : Listener() {
                override fun received(connection: Connection, any: Any?) {
                    any ?: return
                    if (any is MyNameIs) {
                        any.name ?: return
                        if (!foundServers.contains(any.name as String)) {
                            foundServers.add(any.name as String)
                            addServerToList(Pair(any.name as String, host))
                        }
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
        val timer = Timer()
        timer.scheduleTask(object : Timer.Task() {
            override fun run() {
                searching = false
            }
        }, 6F)
    }

}