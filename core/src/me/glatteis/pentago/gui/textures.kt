package me.glatteis.pentago.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/**
 * Created by Linus on 21.07.2016!
 */

object Textures {

    val vanadineFontBig = BitmapFont(Gdx.files.internal("vanadine_font/vanadine.fnt"))
    val vanadineFontSmall = BitmapFont(Gdx.files.internal("vanadine_font_small/vanadine_font_small.fnt"))
    val montserratMedium = BitmapFont(Gdx.files.internal("m_medium/montserrat_medium.fnt"))
    val montserratSmall = BitmapFont(Gdx.files.internal("m_small/montserrat_small.fnt"))

    fun load() = Unit
}

