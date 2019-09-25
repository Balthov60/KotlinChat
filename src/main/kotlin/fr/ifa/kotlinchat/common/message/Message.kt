package fr.ifa.kotlinchat.common.message

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Message(
    val identifier: MessageIdentifier,
    val content: List<String>,
    val time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy à HH:mm:ss"))
) {
    override fun toString() = "$identifier|$time|${content.joinToString("|")}\n"

    fun getUsername() : String {
        return content[0]
    }

    fun getUserMessageContent(): String {
        return content[1]
    }
}