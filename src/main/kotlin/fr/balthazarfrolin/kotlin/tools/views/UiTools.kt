package fr.balthazarfrolin.kotlin.tools.views

import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.Region

/**
 * Force size of a Region by setting min & max size.
 */
fun Region.forceSize(width: Double, height: Double)
{
    setMinSize(width, height)
    setMaxSize(width, height)
}

fun Region.forceWidth(width: Double)
{
    minWidth = width
    maxWidth = width
}

fun Region.forceHeight(height: Double)
{
    minHeight = height
    maxHeight = height
}

fun ColumnConstraints.forceWidth(width: Double)
{
    minWidth = width
    maxWidth = width
}

fun Boolean.toFrenchString(): String
{
    return if (this) "Oui" else "Non"
}