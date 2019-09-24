package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.net.ServerSocket
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class ChatServer(
    private val portNumber: Int
) {
    fun start() {
        val listenSocket: ServerSocket

        try {
            listenSocket = ServerSocket(portNumber) //port
            println("Server ready...")

            // Create Queue
            val clientSockets = ArrayList<KotlinChatSocket>()
            // Create ServerMessageHandler with queue & clientSockets -- Start

            while (true) {
                val clientSocket = listenSocket.accept()
                println("Connexion from:" + clientSocket.inetAddress)

                // Create Socket
                // Add socket to clientSockets
            }
        } catch (e: Exception) {
            System.err.println("Error in EchoServer:$e")
        }

    }
}

fun main(args: Array<String>)
{
    if (args.size != 1) {
        println("Usage: java EchoServer <EchoServer port>")
        exitProcess(1)
    }

    ChatServer(args[0].toInt()).start()
}