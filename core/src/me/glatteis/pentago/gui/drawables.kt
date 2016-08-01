package me.glatteis.pentago.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.Drawable

/**
 * Created by Linus on 23.07.2016!
 */
class ColorDrawable(var color: Color) : BaseDrawable() {
    val textureRegion = TextureRegion(Texture(Gdx.files.internal("textures/white_pixel.png")))
    override fun draw(batch: Batch?, x: Float, y: Float, width: Float, height: Float) {
        batch ?: return
        batch.color = color
        batch.draw(textureRegion, x, y, width, height)
    }
}

class BandColorDrawable(val color: Color, var drawWidth: Float) : BaseDrawable() {
    val textureRegion = TextureRegion(Texture(Gdx.files.internal("textures/white_pixel.png")))
    override fun draw(batch: Batch?, x: Float, y: Float, width: Float, height: Float) {
        batch ?: return
        batch.color = color
        batch.draw(textureRegion, 0F, y, drawWidth, height)
    }
}