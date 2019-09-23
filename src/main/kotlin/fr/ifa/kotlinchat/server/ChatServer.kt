package fr.ifa.kotlinchat.server

import java.net.ServerSocket
import kotlin.system.exitProcess

class ChatServer(
    private val portNumber: Int
) {
    fun start() {
        val listenSocket: ServerSocket

        try {
            listenSocket = ServerSocket(portNumber) //port
            println("Server ready...")

            while (true) {
                val clientSocket = listenSocket.accept()
                println("Connexion from:" + clientSocket.inetAddress)
                val ct = ClientThread(clientSocket)
                ct.start()
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