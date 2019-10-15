package fr.ifa.kotlinchat.client.app

import fr.ifa.kotlinchat.client.view.MainView
import tornadofx.*
import kotlin.system.exitProcess

class KotlinClientChat: App(MainView::class, Styles::class)

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: java KotlinChatServer <KotlinChatServer port>")
        exitProcess(1)
    }

    AppParameters.portNumber = args[0].toInt()

    KotlinClientChat()
}

object AppParameters {
    var portNumber = 4242
}