package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.ClientConnector
import me.glatteis.pentago.gui.Textures
import java.net.InetAddress

/**
 * Created by Linus on 23.07.2016!
 */
class ConnectMenu {

    val dropdown = Table()

    init {
        val connector = ClientConnector()
        PentagoCore.connector = connector

        val title = Label("CONNECT TO SERVER", Label.LabelStyle(Textures.vanadineFontBig, Color.BLACK))
        title.setPosition(0F, 800F, Align.center)
        MainMenu.addActor(title)

        val scrollPane = ScrollPane(dropdown)
        scrollPane.width = 1000F
        scrollPane.height = 500F
        scrollPane.setPosition(0F, 600F)
    }

    fun addServerToDropdown(pair: Pair<String, InetAddress>) {
        dropdown.add(Button(Label(pair.first, Label.LabelStyle(Textures.montserratMedium, Color.BLACK)), Button.ButtonStyle()))
    }

}