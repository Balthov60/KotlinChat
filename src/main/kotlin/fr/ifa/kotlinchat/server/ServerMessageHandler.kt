package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.io.File
import java.io.IOException
import java.util.concurrent.BlockingQueue
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ServerMessageHandler(
        private val queue: BlockingQueue<Message>,
        private val clientSockets: ArrayList<KotlinChatSocket>
) : Thread()
{
    override fun run() {
        val file = File("history.txt")
        try {
            while (true) {
                if (queue.isNotEmpty()) {
                    // Dépiler queue
                    val message = queue.poll()

                    println("Debug: ServerMessageHandle : ID = ${message.identifier} | C = ${message.content}")

                    when (message.identifier) {
                        MessageIdentifier.LOGIN -> {
                            val username = message.content[0]
                            //TODO: get history
                            val newMessage = Message(MessageIdentifier.SEND, listOf("SERVER", "$username connecté.\n"))
                            sendMessage(newMessage)
                        }
                        MessageIdentifier.SEND -> {
                            file.appendText(message.toString());
                            sendMessage(message)
                        }
                        MessageIdentifier.LOGOUT -> {
                            val username = message.content[0]
                            // TODO: close socket
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

    fun sendMessage(message: Message) {
        for (socket in clientSockets) {
            socket.sendMessage(message.toString())
        }
    }
}