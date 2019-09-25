package fr.ifa.kotlinchat.common.socket

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

class KotlinChatSocket(
        val socket: Socket,
        messageProcessingQueue: BlockingQueue<Pair<Socket, Message>>
) {
    private val outputStream: BufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    private val inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

    private val listeningThread: Thread

    init {
        listeningThread = thread {
            try {
                while (!socket.isClosed) {
                    println("Wait Message from ${socket.inetAddress}:${socket.port}/${socket.localPort}")
                    val line = inputStream.readLine()
                    println("Message received : $line")

                    if (!line.isNullOrEmpty())
                        messageProcessingQueue.add(Pair(socket, MessageFactory.createMessageFromString(line)))
                }
            }
            catch (e: SocketException) {
                return@thread
            }
        }
    }

    fun close()
    {
        println("Stop listening to messages")

        listeningThread.interrupt()
        socket.close()
    }

    fun sendMessage(message: Message)
    {
        outputStream.write(message.toString())
        outputStream.flush()

        print("Message sent : $message")
    }
}