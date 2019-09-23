package fr.ifa.kotlinchat.common.message

object MessageFactory {
    fun createSimpleMessage(identifier: MessageIdentifier, content: String): String {
        return "$identifier|$content\n"
    }

    fun createMessageFromString(message: String): Message {
        val args: List<String> = message.split("|")
        return Message(MessageIdentifier.valueOf(args[0]), args[1])
    }
}
