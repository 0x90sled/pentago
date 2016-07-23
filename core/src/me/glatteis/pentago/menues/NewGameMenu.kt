package me.glatteis.pentago.menues

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import me.glatteis.pentago.Pentago
import me.glatteis.pentago.PentagoCore
import me.glatteis.pentago.connection.LocalConnector
import me.glatteis.pentago.gui.Board
import me.glatteis.pentago.gui.ColorDrawable
import me.glatteis.pentago.gui.Textures
import me.glatteis.pentago.logic.GameLogic
import me.glatteis.pentago.logic.Player
import java.util.*

/**
 * Created by Linus on 23.07.2016!
 */
class NewGameMenu : MenuStage() {

    init {
        PentagoCore.connector = LocalConnector()

        val titleStyle = Label.LabelStyle(Textures.vanadineFontBig, Color.BLACK)

        val title = Label("NEW GAME", titleStyle)
        title.setPosition(0F, 800F, Align.center)
        addActor(title)

        val playerTable = Table()

        val players = ArrayList<Player>()

        val labelStyle = Label.LabelStyle(Textures.montserratMedium, Color.BLACK)
        val smallLabelStyle = Label.LabelStyle(Textures.montserratSmall, Color.BLACK)
        val buttonStyle = Button.ButtonStyle()
        val textFieldStyle = TextField.TextFieldStyle()
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

        val addPlayer = Button(Label("Add Player", smallLabelStyle), buttonStyle)
        addPlayer.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val playerName = playerTypeBox.text
                if (playerName.isBlank()) return
                playerTypeBox.text = ""
                val player = Player(generateRandomColor(Color.WHITE), playerName)
                players.add(player)


                val group = Table()
                group.width = 2000F
                group.add(Label(playerName + "   ", Label.LabelStyle(Textures.montserratMedium, Color.BLACK))).left().expand()

                val remove = Button(Label("Remove", Label.LabelStyle(Textures.montserratSmall, Color.BLACK)),
                        Button.ButtonStyle())
                remove.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        playerTable.removeActor(group)
                        players.remove(player)
                    }
                })

                group.add(remove).right()

                group.background = ColorDrawable(player.color)

                playerTable.add(group).row()
            }
        })
        addPlayer.setPosition(0F, 300F, Align.center)
        addActor(addPlayer)

        val scrollPane = ScrollPane(playerTable)
        scrollPane.width = 2000F
        scrollPane.height = 600F
        scrollPane.setPosition(0F, -200F, Align.center)
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
        boxStyle.listStyle = listStyle
        val boardSizeDropdown = SelectBox<String>(boxStyle)
        boardSizeDropdown.items = Array(arrayOf("2x2", "3x3", "4x4"))
        boardSizeDropdown.width = 200F
        boardSizeDropdown.setPosition(-400F, -600F, Align.right)
        addActor(boardSizeDropdown)

        val openToWifiButton = Button(Label("Open this game to WiFi",
                Label.LabelStyle(Textures.montserratSmall, Color.BLACK)), buttonStyle)
        openToWifiButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val uuid = UUID.randomUUID()
                (PentagoCore.connector as LocalConnector).connection.createServer(uuid)
                (openToWifiButton.children[0] as Label).setText("Your UUID is $uuid")
            }
        })
        openToWifiButton.setPosition(200F, -600F, Align.left)

        addActor(openToWifiButton)

        val playButton = Button(Label("Play", labelStyle), Button.ButtonStyle())
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (players.size < 2) return

                val size: Int
                when (boardSizeDropdown.selected) {
                    "2x2" -> size = 2
                    "3x3" -> size = 3
                    "4x4" -> size = 4
                    else -> size = 2
                }
                PentagoCore.board = Board(3, size, size)
                PentagoCore.logic = GameLogic(3, size, size, players, 5)
                PentagoCore.instance.screen = PentagoCore.board
            }
        })
        playButton.setPosition(0F, -800F, Align.center)
        addActor(playButton)
    }

    override fun show() {
        Gdx.input.inputProcessor = this
    }

    fun generateRandomColor(mix: Color): Color {
        val random = Random()
        var red = random.nextFloat()
        var green = random.nextFloat()
        var blue = random.nextFloat()

        red = (red + mix.r) / 2
        green = (green + mix.g) / 2
        blue = (blue + mix.b) / 2

        val color = Color(red, green, blue, 1F)
        return color
    }

    fun displayWonPopup(player: Player) {
        val dialog = Dialog("${player.name} won!", Window.WindowStyle(Textures.montserratMedium, Color.BLACK, ColorDrawable(player.color)))
                .button(Button(Label("OK", Label.LabelStyle(Textures.montserratMedium, Color.BLACK)), Button.ButtonStyle()))
        dialog.width = 1000F
        dialog.height = 500F
        dialog.setPosition(0F, 0F, Align.center)
        addActor(dialog)
    }

}