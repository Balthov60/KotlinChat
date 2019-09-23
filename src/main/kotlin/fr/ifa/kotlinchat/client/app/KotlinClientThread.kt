package fr.ifa.kotlinchat.client.app

import java.io.*
import java.net.Socket
import kotlin.concurrent.thread


class KotlinClientSocket(
        address: String,
        port: Int
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
                println("read")
                val line = inputStream.readLine()
                println(line)
            }
        }
    }

    fun sendMessage(message: String)
    {
        println("test : $message")
        outputStream.write(message)
        outputStream.flush()
    }
}