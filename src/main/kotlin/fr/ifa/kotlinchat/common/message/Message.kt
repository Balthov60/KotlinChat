package fr.ifa.kotlinchat.common.message

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Message(
    val identifier: MessageIdentifier,
    val content: List<String>
) {
    fun toSendString() = "$identifier|${content.joinToString("|")}\n"
    override fun toString() = "$identifier|${content.joinToString("|")}"

    fun getUsername() : String {
        return content[1]
    }

    fun getUserMessageTime(): String {
        return content[0]
    }

    fun getUserMessageContent(): String {
        return content[2]
    }

    fun getHistoryMessagesList(): List<Message> {
        return content[0].split("/").map { println(it);MessageFactory.createMessageFromString(it) }
    }
}