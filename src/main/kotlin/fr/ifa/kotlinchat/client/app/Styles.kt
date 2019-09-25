package fr.ifa.kotlinchat.client.app

import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import javafx.geometry.VPos
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            +GlobalStyles.centeredContentMixin
        }

        label {
            padding = box(10.px)
            vAlignment = VPos.CENTER
        }

        button {
            minWidth = 150.px
            maxWidth = 150.px
        }
    }
}