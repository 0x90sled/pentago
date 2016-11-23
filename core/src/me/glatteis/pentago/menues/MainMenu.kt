package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.PentagoLabelButton
import me.glatteis.pentago.gui.Textures

/**
 * Created by Linus on 23.07.2016!
 */
object MainMenu : MenuStage() {

    init {
        val title = Label("PENTAGO", Label.LabelStyle(Textures.vanadineBig, Color.BLACK))
        title.setPosition(0F, 800F, Align.center)
        addActor(title)

        val labelStyle = Label.LabelStyle(Textures.montserratMedium, Color.BLACK)

        val newGame = PentagoLabelButton("New Game", labelStyle)
        newGame.listener = object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PentagoCore.instance.screen = NewGameMenu()
            }
        }
        newGame.setPosition(0F, 400F, Align.center)
        addActor(newGame)

        val joinNetworkGame = PentagoLabelButton("Join Network Game", labelStyle)
        joinNetworkGame.setPosition(0F, 0F, Align.center)
        joinNetworkGame.listener = (object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PentagoCore.instance.screen = ConnectMenu()
            }
        })
        addActor(joinNetworkGame)

        val options = PentagoLabelButton("Licenses", labelStyle)
        options.listener = (object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PentagoCore.instance.screen = OptionsMenu()
            }
        })
        options.setPosition(0F, -400F, Align.center)
        
        addActor(options)
    }






}