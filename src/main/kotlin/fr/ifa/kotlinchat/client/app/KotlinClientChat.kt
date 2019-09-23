package fr.ifa.kotlinchat.client.app

import fr.ifa.kotlinchat.client.view.MainView
import javafx.stage.Stage
import tornadofx.App
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class KotlinClientChat: App(MainView::class, Styles::class)
{
    override fun start(stage: Stage) {
        super.start(stage)
    }
}