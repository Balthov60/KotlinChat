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
        private val socket: Socket,
        messageProcessingQueue: Queue<Message>
) {
    private val outputStream: BufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    private val inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

    init {
        thread {
            while(true)
            {
                val line = inputStream.readLine()
                println("Message received : $line")
                messageProcessingQueue.add(MessageFactory.createMessageFromString(line))
            }
        }
    }

    fun sendMessage(message: String)
    {
        outputStream.write(message)
        outputStream.flush()
    }
}