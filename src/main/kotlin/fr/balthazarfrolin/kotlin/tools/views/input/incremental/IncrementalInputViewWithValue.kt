package fr.balthazarfrolin.kotlin.tools.views.input.incremental

import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.layout.Pane
import tornadofx.*

open class IncrementalInputViewWithValue
(
    current: Int,
    MIN: Int,
    MAX: Int,
    value: SimpleIntegerProperty = SimpleIntegerProperty(current)

)
    : IncrementalInputView(current, MIN, MAX, value = value)
{
    override fun skin(): Pane
    {
        return hbox()
        {
            addClass(GlobalStyles.centeredContent)
            style { backgroundColor += color }
            hboxConstraints { margin = insets(2, 2) }

            hbox {
                hboxConstraints { margin = insets(2, 2) }
                add(super.lessButton())
            }

            label(value)
            {
                addClass(GlobalStyles.centeredSmallText)
                hboxConstraints { margin = insets(2, 0) }
            }

            hbox {
                hboxConstraints { margin = insets(2, 2) }
                add(super.moreButton())
            }
        }
    }
}