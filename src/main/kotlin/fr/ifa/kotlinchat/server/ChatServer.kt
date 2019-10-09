package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ArrayBlockingQueue
import kotlin.system.exitProcess

class ChatServer(
    private val portNumber: Int
) {
    fun start() {
        val listenSocket: ServerSocket

        try {
            listenSocket = ServerSocket(portNumber)
            println("Server ready on ${listenSocket.inetAddress}:${listenSocket.localPort}")

            val queue: ArrayBlockingQueue<Pair<Socket, Message>> = ArrayBlockingQueue(1000)
            val clientSockets = ArrayList<KotlinChatSocket>()

            ServerMessageHandler(queue, clientSockets).start()

            while (true) {
                val clientSocket = listenSocket.accept()
                println("Connexion from : ${clientSocket.inetAddress}:${clientSocket.port}")

                clientSockets.add(KotlinChatSocket(clientSocket, queue))
            }
        }
        catch (e: Exception) {
            System.err.println("Error in KotlinChatServer:$e")
        }

    }
}

fun main(args: Array<String>)
{
    if (args.size != 1) {
        println("Usage: java KotlinChatServer <KotlinChatServer port>")
        exitProcess(1)
    }

    ChatServer(args[0].toInt()).start()
}