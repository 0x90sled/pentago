package me.glatteis.pentago.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import me.glatteis.pentago.Pentago

class DesktopLauncher {

    companion object {
        @JvmStatic
        fun main(arg: Array<String>) {
            val config = LwjglApplicationConfiguration()
            LwjglApplication(Pentago(), config)
        }
    }

}