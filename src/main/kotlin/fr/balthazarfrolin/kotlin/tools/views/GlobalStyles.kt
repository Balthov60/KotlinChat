package fr.balthazarfrolin.kotlin.tools.views

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import tornadofx.*

class GlobalStyles : Stylesheet()
{
    companion object
    {

        val centeredContent by cssclass()
        val centeredContentMixin = mixin {
            alignment = Pos.CENTER
        }

        private val DEFAULT_PADDING = box(5.px, 10.px)
        val defaultPadding by cssclass()
        val defaultPaddingMixin = mixin {
            padding = DEFAULT_PADDING
        }

        private val SMALL_PADDING = box(2.px, 2.px)
        val smallPadding by cssclass()
        val smallPaddingMixin = mixin {
            padding = SMALL_PADDING
        }

        val centeredText by cssclass()
        val centeredTextMixin = mixin {
            textAlignment = TextAlignment.CENTER
        }
        val smallText by cssclass()
        val smallTextMixin = mixin {
            fontSize = 10.px
        }

        val centeredSmallText by cssclass()
        val errorMessage by cssclass()
        val warningMessage by cssclass()

        val defaultBorderColorMixin = mixin {
            borderColor = multi(box(Color.BLACK))
        }
        val bottomBorder by cssclass()
        val topBorder by cssclass()
        val fullBorder by cssclass()

        val flatButton by cssclass()
        val flatButtonMixin = mixin {
            backgroundColor += Color.WHITESMOKE
        }

        const val STANDARD_WIDTH = 25.0
        const val STANDARD_HEIGHT = 25.0
    }

    init
    {
        root { +defaultPaddingMixin }

        defaultPadding { +defaultPaddingMixin }
        smallPadding   { +smallPaddingMixin }

        centeredContent { +centeredContentMixin }
        centeredText { +centeredTextMixin }
        smallText { +smallTextMixin }

        centeredSmallText()
        {
            +smallTextMixin
            +centeredTextMixin
        }

        bottomBorder()
        {
            +defaultBorderColorMixin
            borderWidth = multi(box(0.px, 0.px, 1.px, 0.px))
        }
        topBorder()
        {
            +defaultBorderColorMixin
            borderWidth = multi(box(1.px, 0.px, 0.px, 0.px))
        }
        fullBorder()
        {
            +defaultBorderColorMixin
            borderWidth = multi(box(1.px, 1.px, 1.px, 1.px))
        }

        flatButton { +flatButtonMixin }
        errorMessage { textFill = Color.RED }
        warningMessage { textFill = Color.ORANGE }
    }
}
