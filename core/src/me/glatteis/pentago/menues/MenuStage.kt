package me.glatteis.pentago.menues

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport

/**
 * Created by Linus on 23.07.2016!
 */
open class MenuStage : Stage(), Screen{

    val menuWidth = 2000F
    val menuHeight = 2000F

    init {
        viewport = ExtendViewport(menuWidth, menuHeight)
    }

    override fun show() {
        Gdx.input.inputProcessor = this
    }

    override fun pause() {
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun hide() {
    }

    override fun render(delta: Float) {
        act(delta)
        draw()
    }

    override fun resume() {
    }
}