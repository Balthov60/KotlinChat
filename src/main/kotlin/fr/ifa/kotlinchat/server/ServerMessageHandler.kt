package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageFactory
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
    private val historyFile = File("history.txt")

    override fun run()
    {
        try
        {
            while (true)
            {
                if (queue.isNotEmpty())
                {
                    val messageReceived = queue.poll()
                    val message = messageReceived.second
                    val socket = messageReceived.first

                    println("Debug: ServerMessageHandle : ID = ${message.identifier} | C = ${message.content}")

                    when (message.identifier)
                    {
                        MessageIdentifier.LOGIN -> {
                            val username = message.content[0]

                            // Send History
                            val history = historyFile.readText()
                            if (history.isNotEmpty()) {
                                val historyMessage = MessageFactory.createMessageFromHistory(historyFile.readText())
                                sendMessageTo(socket, historyMessage)
                            }

                            // Send Login Message
                            val newMessage = MessageFactory.createSendMessage("server", "$username connecté.")
                            sendMessageToAll(newMessage)
                        }
                        MessageIdentifier.SEND -> {
                            println("Append to History : ${message.toString()}")
                            historyFile.appendText(message.toString())
                            sendMessageFrom(socket, message)
                        }
                        MessageIdentifier.LOGOUT -> {
                            val socketIndex = clientSockets.indexOfFirst { t -> t.socket == socket }
                            clientSockets[socketIndex].close()
                            clientSockets.removeAt(socketIndex)

                            val username = message.content[0]
                            val newMessage = MessageFactory.createSendMessage("server", "$username déconnecté.\n")
                            sendMessageToAll(newMessage)
                        }
                        else -> {}
                    }
                }
            }
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendMessageFrom(fromSocket: Socket, message: Message) {
        for (clientSocket in clientSockets) {
            if (fromSocket != clientSocket.socket)
                clientSocket.sendMessage(message)
        }
    }
    private fun sendMessageTo(toSocket: Socket, message: Message) {
        val socketIndex = clientSockets.indexOfFirst { it.socket == toSocket }
        clientSockets[socketIndex].sendMessage(message)
    }

    private fun sendMessageToAll(message: Message) {
        for (clientSocket in clientSockets) {
            clientSocket.sendMessage(message)
        }
    }
}