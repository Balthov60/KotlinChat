package fr.ifa.kotlinchat.common.socket

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread


class KotlinChatSocket(
        address: String,
        port: Int,
        messageProcessingQueue: Queue<Message>
) {
    private val socket: Socket = Socket(address, port)
    private val outputStream: BufferedWriter
    private val inputStream: BufferedReader

    init {
        outputStream = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        inputStream = BufferedReader(InputStreamReader(socket.getInputStream()))

        thread {
            while(true)
            {
                val line = inputStream.readLine()
                println("Debug: Message Received - $line")
                messageProcessingQueue.add(MessageFactory.createMessageFromString(line))
            }
        }
    }

    fun sendMessage(message: String)
    {
        println("Debug: Send Message - $message")
        outputStream.write(message)
        outputStream.flush()
    }
}