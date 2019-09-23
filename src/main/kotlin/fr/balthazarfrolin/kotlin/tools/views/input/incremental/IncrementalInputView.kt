package fr.balthazarfrolin.kotlin.tools.views.input.incremental

import fr.balthazarfrolin.kotlin.tools.views.forceSize
import fr.balthazarfrolin.kotlin.tools.views.GlobalStyles
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.*

open class IncrementalInputView
(
    current: Int,

    private val MIN: Int,
    private val MAX: Int,
    protected val color: Paint = Color.LIGHTGRAY,
    protected val value: SimpleIntegerProperty = SimpleIntegerProperty(current)

)
    : View()
{
    // Logic
    init {
        if (MIN > current || MAX < current)
            throw Exception("La valeur courante($current) doit être comprise entre $MIN et $MAX")
    }

    fun value(): Int {
        return value.get()
    }
    fun valueAsProperty(): SimpleIntegerProperty {
        return value
    }

    // UI

    override val root: Parent = this.skin()

    protected open fun skin(): Pane
    {
        return vbox()
        {
            addClass(GlobalStyles.smallPadding)
            addClass(GlobalStyles.centeredContent)
            style { backgroundColor += color }

            add(moreButton())
            add(lessButton())
        }
    }

    protected fun lessButton() : Button
    {
        return button("-")
        {
            forceSize(
                GlobalStyles.STANDARD_WIDTH,
                GlobalStyles.STANDARD_HEIGHT
            )
            addClass(GlobalStyles.centeredSmallText)

            action()
            {
                if ((value.get() - 1) >= MIN)
                    value.set(value.get() - 1)
            }
        }
    }
    protected fun moreButton() : Button
    {
        return button("+")
        {
            forceSize(
                GlobalStyles.STANDARD_WIDTH,
                GlobalStyles.STANDARD_HEIGHT
            )
            addClass(GlobalStyles.centeredSmallText)

            action()
            {
                if ((value.get() + 1) <= MAX)
                    value.set(value.get() + 1)
            }
        }
    }
}