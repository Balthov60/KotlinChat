package fr.ifa.kotlinchat.server

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.socket.KotlinChatSocket
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import kotlin.collections.ArrayList
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

            val queue: ArrayBlockingQueue<Pair<Socket, Message>> = ArrayBlockingQueue(10)
            val clientSockets = ArrayList<KotlinChatSocket>()

            // Create ServerMessageHandler with queue & clientSockets -- Start
            val serverMessageHandler = ServerMessageHandler(queue, clientSockets)
            serverMessageHandler.start()

            while (true) {
                val clientSocket = listenSocket.accept()
                println("Connexion from:" + clientSocket.inetAddress)

                // Create Socket and it to clientSockets
                val socket = KotlinChatSocket(clientSocket, queue)
                clientSockets.add(socket)
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