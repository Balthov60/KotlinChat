package fr.balthazarfrolin.kotlin.tools.views.input.incremental

import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class IncrementalInputWithValueAndLabel
(
    current: Int,
    MIN: Int,
    MAX: Int,

    private val NAME: String,
    value: SimpleIntegerProperty = SimpleIntegerProperty(current)
)
    : IncrementalInputViewWithValue(current, MIN, MAX, value)
{
    override val root = gridpane()
    {
        style { backgroundColor += super.color }

        row()
        {
            label(NAME)
            {
                addClass(GlobalStyles.centeredContent)
                useMaxWidth = true
            }
        }

        row { add(super.skin()) }
    }
}