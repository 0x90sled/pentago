package me.glatteis.pentago

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import me.glatteis.pentago.gui.Textures
import me.glatteis.pentago.menues.MainMenu
import me.glatteis.pentago.menues.SplashScreen

/**
 * Created by Linus on 21.07.2016!
 */

class Pentago : Game() {

    var glCoverageBuffer: Int = 0

    override fun create() {
        PentagoCore.instance = this
        glCoverageBuffer = if (Gdx.graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0
        setScreen(SplashScreen())
    }



    override fun render() {
        Gdx.gl.glClearColor(PentagoCore.backgroundColor.r, PentagoCore.backgroundColor.g, PentagoCore.backgroundColor.b,
                PentagoCore.backgroundColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or glCoverageBuffer)
        screen.render(Gdx.graphics.deltaTime)
    }
}
