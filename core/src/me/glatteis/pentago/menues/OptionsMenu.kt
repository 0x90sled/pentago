package me.glatteis.pentago.menues

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.BandColorDrawable
import me.glatteis.pentago.gui.ColorDrawable
import me.glatteis.pentago.gui.PentagoLabelButton
import me.glatteis.pentago.gui.Textures
import javax.swing.plaf.synth.ColorType

/**
 * Created by Linus on 30.07.2016!
 */
class OptionsMenu : MenuStage() {

    val text =
            """
PENTAGO

The game "pentago" was invented by Tomas/Michael Flodén

I have no affiliation with Mindtwister, Giseh or Kosmos

-- FONTS --

TITLE FONT
"Vandine": Font by Axel Lymphos, Suitable for free or comercial uses.

FONT FOR EVERYTHING ELSE
"Montserrat": Font by Julieta Ulanovsky, released under SIL Open Font License, 1.1
http://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=OFL

-- THIS GAME WAS CREATED USING --

LibGDX
https://libgdx.badlogicgames.com

Kotlin
https://kotlinlang.org

Kryonet
https://github.com/EsotericSoftware/kryonet

IntelliJ IDEA
https://www.jetbrains.com/idea/

-- BY --
glatteis

"""

    init {
        val style = Label.LabelStyle(Textures.montserratSmall, Color.BLACK)
        val creditsLabel = Label(text, style)
        creditsLabel.width = menuWidth
        creditsLabel.setWrap(true)

        val scrollPane = ScrollPane(creditsLabel)
        scrollPane.width = menuWidth
        scrollPane.height = menuHeight
        scrollPane.setPosition(0F, 0F, Align.center)
        addActor(scrollPane)
        val backButton = PentagoLabelButton("Back", style)
        backButton.setPosition(0F, -800F, Align.center)
        backButton.listener = (object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PentagoCore.instance.screen = MainMenu
            }
        })
        addActor(backButton)
    }

}