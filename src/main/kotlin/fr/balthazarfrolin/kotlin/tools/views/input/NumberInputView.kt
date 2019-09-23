package fr.balthazarfrolin.kotlin.tools.views.input

import fr.balthazarfrolin.kotlin.tools.views.forceWidth
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import kotlin.math.abs

//TODO: make validation remove value from textfield
class NumberInputView : View()
{
    private val input = SimpleStringProperty()

    val value: Int?
        get() = input.value.toIntOrNull()

    override val root = textfield(input)
    {
        forceWidth(40.0)
    }

    init {
        input.onChange()
        {
            if (!input.value.matches(Regex("^[0-9]*$")))
            {
                input.set("")
            }
        }
    }

    fun getPositiveValueOrOne(): Int
    {
        if (input.value.isNullOrEmpty())
            return 1

        val value = input.value.toIntOrNull() ?: return 1

        if (value == 0)
            return 1

        return abs(value)
    }
}
