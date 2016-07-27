package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.ClientConnector
import me.glatteis.pentago.gui.Textures

/**
 * Created by Linus on 24.07.2016!
 */
class WaitMenu : MenuStage() {

    init {
        val title = Label("WAITING FOR HOST", Label.LabelStyle(Textures.vanadineFontBig, Color.BLACK))
        title.setPosition(0F, 0F, Align.center)
        addActor(title)

        val disconnect = Button(Label("Disconnect", Label.LabelStyle(Textures.montserratMedium, Color.BLACK)),
                Button.ButtonStyle())
        disconnect.setPosition(0F, -500F, Align.center)
        disconnect.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (PentagoCore.connector is ClientConnector) {
                    (PentagoCore.connector as ClientConnector).disconnect()
                    PentagoCore.instance.screen = MainMenu
                }
            }
        })
        addActor(disconnect)
    }

}