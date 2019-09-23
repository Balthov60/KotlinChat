package fr.ifa.kotlinchat.server

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message
import fr.ifa.kotlinchat.common.message.MessageIdentifier
import fr.ifa.kotlinchat.common.message.MessageFactory
import java.io.*
import java.net.Socket

class ClientThread(
    private val clientSocket: Socket
) : Thread()
{
    private var isLogged: Boolean = false
    private var username: String? = null

    override fun run() {
        try {
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

            while (true) {
                println("test launch")
                val line = reader.readLine()

                try {
                    if (line == null)
                        continue
                    println(line)

                    val message = MessageFactory.createMessageFromString(line)

                    // while not logged
                    if (!isLogged) {
                        if (message.identifier == MessageIdentifier.LOGIN) {
                            isLogged = true
                            username = message.content
                            sendMessage("Bienvenue $username !\n")
                            println("salut")
                        } else {
                            continue
                        }
                    }

                    // send History

                    // while (check disconnection)
                } catch (iae: IllegalArgumentException) {
                    iae.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun sendMessage(message: String) {
        try {
            val outStream = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
            outStream.write(message)
            outStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}