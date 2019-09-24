package fr.ifa.kotlinchat.client.app

import fr.ifa.kotlinchat.client.view.LoginView
import javafx.stage.Stage
import tornadofx.App

class KotlinClientChat: App(LoginView::class, Styles::class)
{
    override fun start(stage: Stage) {
        super.start(stage)
    }
}