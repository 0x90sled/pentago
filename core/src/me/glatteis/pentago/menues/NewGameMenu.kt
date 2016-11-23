package me.glatteis.pentago.menues

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.LocalConnector
import me.glatteis.pentago.generateRandomColor
import me.glatteis.pentago.gui.*
import me.glatteis.pentago.logic.AIPlayer
import me.glatteis.pentago.logic.GameLogic
import me.glatteis.pentago.logic.Player
import java.util.*

/**
 * Created by Linus on 23.07.2016!
 */
class NewGameMenu : MenuStage() {

    init {
        PentagoCore.connector = LocalConnector()

        val titleStyle = Label.LabelStyle(Textures.vanadineBig, Color.BLACK)
        val labelStyle = Label.LabelStyle(Textures.montserratMedium, Color.BLACK)
        val smallLabelStyle = Label.LabelStyle(Textures.montserratSmall, Color.BLACK)
        val textFieldStyle = TextField.TextFieldStyle()

        val title = Label("NEW GAME", titleStyle)
        title.setPosition(0F, 800F, Align.center)
        addActor(title)

        val playerTable = Table()

        val players = ArrayList<Player>()


        textFieldStyle.font = Textures.montserratMedium
        textFieldStyle.fontColor = Color.BLACK
        textFieldStyle.background =
                TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("textures/textedit_background.png"))))
        textFieldStyle.cursor =
                TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("textures/cursor.png"))))

        val playerTypeBox = TextField("", textFieldStyle)
        playerTypeBox.width = 1200F
        playerTypeBox.maxLength = 12
        playerTypeBox.setPosition(0F, 500F, Align.center)
        keyboardFocus = playerTypeBox
        addActor(playerTypeBox)

        val addPlayer = PentagoLabelButton("Add Player", smallLabelStyle)
        val addAI = PentagoLabelButton("Add AI", smallLabelStyle)
        val listener = object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (playerTypeBox.text.isBlank()) return

                val playerName: String
                val player: Player

                if (event.listenerActor == addPlayer) {
                    playerName = playerTypeBox.text.trim()
                    player = Player(generateRandomColor(Color.WHITE), playerName)
                } else {
                    playerName = playerTypeBox.text.trim() + " [BOT]"
                    player = AIPlayer(generateRandomColor(Color.WHITE), playerName)
                }

                playerTypeBox.text = ""
                players.add(player)

                val group = Table()
                group.width = 2000F
                group.add(Label(playerName + "   ", Label.LabelStyle(Textures.montserratMedium, Color.BLACK))).left().expand()

                val remove = PentagoLabelButton("Remove", Label.LabelStyle(Textures.montserratSmall, Color.BLACK))
                remove.listener = (object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        playerTable.removeActor(group)
                        players.remove(player)
                    }
                })

                group.add(remove).right()

                group.background = BandColorDrawable(player.color, 2000F)

                playerTable.add(group).row()
            }
        }
        addPlayer.addListener(listener)
        addAI.addListener(listener)
        addPlayer.setPosition(-500F, 300F, Align.center)
        addAI.setPosition(500F, 300F, Align.center)
        addActor(addPlayer)
        addActor(addAI)

        val scrollPane = ScrollPane(playerTable)
        scrollPane.width = 2000F
        scrollPane.height = 650F
        scrollPane.setPosition(0F, -100F, Align.center)
        addActor(scrollPane)

        val boxStyle = SelectBox.SelectBoxStyle()
        boxStyle.font = Textures.montserratSmall
        boxStyle.fontColor = Color.BLACK
        boxStyle.scrollStyle = ScrollPane.ScrollPaneStyle()
        val listStyle = List.ListStyle()
        listStyle.font = Textures.montserratSmall
        listStyle.fontColorSelected = Color.BLACK
        listStyle.fontColorUnselected = Color.BLACK
        listStyle.selection = ColorDrawable(Color.YELLOW)
        listStyle.background = ColorDrawable(Color.WHITE)
        boxStyle.listStyle = listStyle
        val boardSizeDropdown = SelectBox<String>(boxStyle)
        boardSizeDropdown.items = Array(arrayOf("2x2", "3x3", "4x4", "1x3", "1x4", "2x3", "2x4"))
        boardSizeDropdown.width = 200F
        boardSizeDropdown.setPosition(0F, -500F, Align.center)
        addActor(boardSizeDropdown)

        val openToWifiButton = PentagoLabelButton("Open this game to WiFi",
                Label.LabelStyle(Textures.montserratSmall, Color.BLACK))
        openToWifiButton.addListener(object : ClickListener() {
            var clicked = false
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (clicked) return
                clicked = true
                val uuid = UUID.randomUUID()
                (PentagoCore.connector as LocalConnector).connection.createServer(uuid)
                (openToWifiButton.children[0] as Label).setText("Your UUID is $uuid")
            }
        })
        openToWifiButton.setPosition(0F, -600F, Align.center)
        addActor(openToWifiButton)

        val back = PentagoLabelButton("Back", labelStyle)
        back.setPosition(-1000F, -800F, Align.left)
        back.listener = (object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (PentagoCore.connector is LocalConnector) {
                    (PentagoCore.connector as LocalConnector).connection.disconnect()
                }
                PentagoCore.instance.screen = MainMenu
            }
        })
        addActor(back)

        val playButton = PentagoLabelButton("Play", labelStyle)
        playButton.listener = (object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (players.size < 2) return
                val selectedSize = boardSizeDropdown.selected.split("x")
                val sizeX = selectedSize[0].toInt()
                val sizeY = selectedSize[1].toInt()
                (PentagoCore.connector as LocalConnector).connection.startGame(sizeX, sizeY, players.toTypedArray())
                PentagoCore.board = Board(3, sizeX, sizeY, this@NewGameMenu, players.toTypedArray())
                PentagoCore.logic = GameLogic(3, sizeX, sizeY, players, 5)
                PentagoCore.instance.screen = PentagoCore.board
            }
        })
        playButton.setPosition(1000F, -800F, Align.right)
        addActor(playButton)
    }


}