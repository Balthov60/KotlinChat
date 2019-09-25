package fr.ifa.kotlinchat.common.message

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageFactory
{
    fun createSendMessage(username: String, content: String): Message {
        val time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM à HH:mm"))

        return Message(MessageIdentifier.SEND, arrayListOf(time, username, content))
    }

    fun createMessageFromString(message: String): Message {
        val messageIdentifier: String = message.substringBefore("|")
        val body: String = message.substringAfter("|")

        val array = body.substringAfter("[").substringBefore("]")
        val bodyNoArray = body.substringBefore("[")

        val content = if (bodyNoArray == "" || bodyNoArray == "|") ArrayList()
                      else bodyNoArray.split("|").toMutableList()

        if (body.contains("[") && body.contains("]"))
            content.add(array)

        return Message(MessageIdentifier.valueOf(messageIdentifier), content)
    }

    fun createMessageFromHistory(history: String): Message {
        val messages: List<String> = history.split("\n")

        var content = "["
        for ((i, message) in messages.withIndex())
        {
            if (message == "")
                continue

            content += "$message/"
        }
        return Message(MessageIdentifier.HISTORY, arrayListOf(content.substringBeforeLast("/").plus("]")))
    }

    fun createLoginMessage(username: String): Message {
        return Message(MessageIdentifier.LOGIN, arrayListOf(username))
    }

    fun createLogoutMessage(username: String): Message {
        return Message(MessageIdentifier.LOGOUT, arrayListOf(username))
    }
}
