package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

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
                            val username = message.content[0]
                            val newMessage = Message(MessageIdentifier.SEND, listOf("SERVER", "$username connecté.\n"))
                            sendMessage(newMessage)
                        }
                        MessageIdentifier.SEND -> {
                            println("SEEEND ${message.identifier} ${message.getUsername()} ${message.getUserMessageContent()}")
                        }
                        MessageIdentifier.LOGOUT -> {
                            val username = message.content[0]
                            val newMessage = Message(MessageIdentifier.SEND, listOf("SERVER", "$username déconnecté.\n"))
                            sendMessage(newMessage)
                        }
                        else -> {

                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

//    fun sendMessage(identifier: MessageIdentifier = MessageIdentifier.SEND, content: String) {
//        val message = "$identifier|$content"
//        for (socket in clientSockets) {
//            socket.sendMessage(message)
//        }
//    }

    fun sendMessage(message: Message) {
        for (socket in clientSockets) {
            socket.sendMessage(message.toString())
        }
    }
}