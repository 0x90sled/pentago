package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Timer
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.ClientConnector
import me.glatteis.pentago.connection.MyNameIs
import me.glatteis.pentago.connection.WhatsYourName
import me.glatteis.pentago.connection.udpPort
import me.glatteis.pentago.gui.Textures
import java.net.InetAddress
import kotlin.concurrent.thread

/**
 * Created by Linus on 23.07.2016!
 */
class ConnectMenu : MenuStage() {

    val list = Table()

    init {
        val connector = ClientConnector()
        PentagoCore.connector = connector

        val title = Label("CONNECT TO SERVER", Label.LabelStyle(Textures.vanadineFontBig, Color.BLACK))
        title.setPosition(0F, 800F, Align.center)
        addActor(title)

        val scrollPane = ScrollPane(list)
        scrollPane.width = 1000F
        scrollPane.height = 500F
        scrollPane.setPosition(0F, 400F, Align.center)
        addActor(scrollPane)

        thread {
            searchServers()
        }
    }

    fun addServerToList(pair: Pair<String, InetAddress>) {
        val button = Button(Label(pair.first, Label.LabelStyle(Textures.montserratMedium, Color.BLACK)), Button.ButtonStyle())
        list.add(button)
        list.row()
    }

    fun searchServers() {
        val client = Client()
        client.start()
        val hosts = client.discoverHosts(udpPort, 5000)
        client.stop()
        for (host in hosts) {
            val clientForThis = Client()
            clientForThis.start()
            clientForThis.addListener(object : Listener() {
                override fun received(connection: Connection, any: Any?) {
                    any ?: return
                    if (any is MyNameIs) {
                        addServerToList(Pair(any.name, host))
                    }
                }
            })
            println("Sending packet!")
            clientForThis.sendTCP(WhatsYourName())
            val timer = Timer()
            timer.scheduleTask(object : Timer.Task() {
                override fun run() {
                    println("Got no response from " + host.toString())
                    clientForThis.stop()
                    clientForThis.dispose()
                }
            }, 5F)
        }

    }

}