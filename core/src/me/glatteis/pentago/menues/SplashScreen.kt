package me.glatteis.pentago.menues

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.gui.Textures
import me.glatteis.pentago.gui.setTurnColor

/**
 * Created by Linus on 18.08.2016!
 */
class SplashScreen() : MenuStage() {
    init {
        Textures.load()
        val image = Image(Texture(Gdx.files.internal("logo.png")))
        image.setSize(2000F, 2000F)
        image.setPosition(-1000F, -1000F)
        image.setOrigin(1000F, 1000F)
        image.setScale(10F)
        addActor(image)

        image.addAction(Actions.scaleTo(1F, 1F, 1F))
        image.addAction(Actions.rotateBy(360F, 1F))

        PentagoCore.backgroundColor = Color.BLACK
        setTurnColor(Color.WHITE)

        addAction(object : Action() {
            var time = 0F

            override fun act(delta: Float): Boolean {
                time += delta
                if (time > 2F) {
                    PentagoCore.instance.screen = MainMenu
                    dispose()
                    return true
                }
                return false
            }
        })
    }

}