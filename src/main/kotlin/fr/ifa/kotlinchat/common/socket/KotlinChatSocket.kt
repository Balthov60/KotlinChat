package fr.ifa.kotlinchat.common.socket

import fr.ifa.kotlinchat.common.message.Message
import fr.ifa.kotlinchat.common.message.MessageFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.*
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

class KotlinChatSocket(
        val socket: Socket,
        messageProcessingQueue: BlockingQueue<Pair<Socket, Message>>
) {
    companion object {
        val GROUP_ADDRESS: InetAddress = InetAddress.getByName("226.214.173.15")
        const val GROUP_PORT: Int = 8888
    }

    private val listeningThread: Thread
    private val outputStream: BufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    private val inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

    private val multicastListeningThread: Thread
    private val multicastSocket = MulticastSocket(GROUP_PORT)

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

        multicastListeningThread = thread {
            multicastSocket.joinGroup(GROUP_ADDRESS)

            while (!multicastSocket.isClosed) {
                println("Wait Multicast Message on ${GROUP_ADDRESS}:${GROUP_PORT}")

                val buffer = ByteArray(2000)
                val packet = DatagramPacket(buffer, buffer.size)

                try {
                    multicastSocket.receive(packet)
                    val line = String(packet.data, 0, packet.length)

                    println("Message received : $line")

                    if (line.isEmpty())
                        continue

                    messageProcessingQueue.add(Pair(socket, MessageFactory.createMessageFromString(line)))
                }
                catch (e: SocketException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun close()
    {
        println("Stop listening to messages")

        listeningThread.interrupt()
        multicastListeningThread.interrupt()

        println(multicastListeningThread.isInterrupted)

        socket.close()

        multicastSocket.leaveGroup(GROUP_ADDRESS)
        multicastSocket.close()
    }

    fun sendMessage(message: Message)
    {
        outputStream.write(message.toSendString())
        outputStream.flush()

        print("Message sent : $message")
    }

    fun sendMulticastMessage(message: Message)
    {
        val packet = DatagramPacket(message.toSendString().toByteArray(), message.toSendString().toByteArray().size, GROUP_ADDRESS, GROUP_PORT)

        multicastSocket.send(packet)

        print("Message sent : $message")
    }
}