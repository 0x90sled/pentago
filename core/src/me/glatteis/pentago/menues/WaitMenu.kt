package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import me.glatteis.pentago.gui.Textures

/**
 * Created by Linus on 24.07.2016!
 */
class WaitMenu : MenuStage() {

    init {
        val title = Label("WAITING FOR HOST", Label.LabelStyle(Textures.vanadineFontBig, Color.BLACK))
        title.setPosition(0F, 0F, Align.center)
        addActor(title)
    }

}