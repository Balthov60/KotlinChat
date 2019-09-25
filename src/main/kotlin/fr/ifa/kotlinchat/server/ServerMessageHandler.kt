package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.io.File
import java.io.IOException
import java.net.Socket
import java.util.concurrent.BlockingQueue

class ServerMessageHandler(
        private val queue: BlockingQueue<Pair<Socket, Message>>,
        private val clientSockets: ArrayList<KotlinChatSocket>
) : Thread()
{
    override fun run() {
        val file = File("history.txt")
        try {
            while (true) {
                if (queue.isNotEmpty()) {
                    // Dépiler queue
                    val received = queue.poll()
                    val message = received.second
                    val socket = received.first

                    println("Debug: ServerMessageHandle : ID = ${message.identifier} | C = ${message.content}")

                    when (message.identifier) {
                        MessageIdentifier.LOGIN -> {
                            val username = message.content[0]
                            //TODO: get history
                            val newMessage = Message(MessageIdentifier.SEND, listOf("SERVER", "$username connecté.\n"))
                            sendMessageToAll(newMessage)
                        }
                        MessageIdentifier.SEND -> {
                            file.appendText(message.toString());
                            sendMessage(socket, message)
                        }
                        MessageIdentifier.LOGOUT -> {
                            val username = message.content[0]
                            // TODO: close socket
                            val newMessage = Message(MessageIdentifier.SEND, listOf("SERVER", "$username déconnecté.\n"))
                            sendMessageToAll(newMessage)
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

    fun sendMessage(fromSocket: Socket, message: Message) {
        for (clientSocket in clientSockets) {
            if (fromSocket != clientSocket.socket)
                clientSocket.sendMessage(message.toString())
        }
    }

    fun sendMessageToAll(message: Message) {
        for (clientSocket in clientSockets) {
            clientSocket.sendMessage(message.toString())
        }
    }
}