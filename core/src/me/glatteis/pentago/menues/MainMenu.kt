package me.glatteis.pentago.menues

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import me.glatteis.pentago.Pentago
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.Textures

/**
 * Created by Linus on 23.07.2016!
 */
object MainMenu : MenuStage() {

    init {
        val title = Label("PENTAGO", Label.LabelStyle(Textures.vanadineFontBig, Color.BLACK))
        title.setPosition(0F, 800F, Align.center)
        addActor(title)

        val labelStyle = Label.LabelStyle(Textures.montserratMedium, Color.BLACK)
        val buttonStyle = Button.ButtonStyle()

        val newGame = Button(Label("New Game", labelStyle), buttonStyle)
        newGame.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PentagoCore.instance.screen = NewGameMenu
            }
        })
        newGame.setPosition(0F, 400F, Align.center)
        addActor(newGame)

        val joinNetworkGame = Button(Label("Join Network Game", labelStyle), buttonStyle)
        joinNetworkGame.setPosition(0F, 0F, Align.center)
        addActor(joinNetworkGame)

        val options = Button(Label("Options", labelStyle), buttonStyle)
        options.setPosition(0F, -400F, Align.center)
        addActor(options)
    }

    override fun show() {
        Gdx.input.inputProcessor = this
    }



}