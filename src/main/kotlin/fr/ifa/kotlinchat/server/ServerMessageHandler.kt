package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageFactory
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.io.*
import java.net.Socket
import java.util.*

class ServerMessageHandler(
    private val queue: Queue<Message>,
    private val clientSockets: ArrayList<KotlinChatSocket>
) : Thread()
{
    override fun run() {
        try {
            while (true) {
                if (queue.isNotEmpty()) {
                    // Dépiler queue
                    val message = queue.poll()

                    println("Debug: ServerMessageHandle : ID = ${message.identifier} | C = ${message.content}")

                    when (message.identifier) {
                        MessageIdentifier.LOGIN -> {
                            sendMessage("SEND|Bienvenue ${message.content} !\n")
                        }
                        MessageIdentifier.SEND -> {
                            
                        }
                        MessageIdentifier.LOGOUT -> {

                        }
                        else -> {

                        }
                    }
                }
/*
                try {
                    if (line == null)
                        continue
                    println(line)

                    val message = MessageFactory.createMessageFromString(line)

                    // while not logged
                    if (!isLogged) {
                        if (message.identifier == MessageIdentifier.LOGIN) {
                            isLogged = true
                            username = message.content
                            sendMessage("Bienvenue $username !\n")
                            println("salut")
                        } else {
                            continue
                        }
                    }

                    // send History

                    // while (check disconnection)
                } catch (iae: IllegalArgumentException) {
                    iae.printStackTrace()
                }

 */
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun sendMessage(message: String) {
        for (socket in clientSockets) {
            socket.sendMessage(message)
        }
    }
}