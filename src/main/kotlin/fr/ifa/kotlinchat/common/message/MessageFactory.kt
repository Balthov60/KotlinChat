package fr.ifa.kotlinchat.common.message

object MessageFactory {
    fun createSimpleMessage(identifier: MessageIdentifier, content: String): String {
        return "$identifier|$content\n"
    }

    fun createSendMessage(username: String, content: String): Message {
        return Message(MessageIdentifier.SEND, arrayListOf(username, content))
    }

    fun createMessageFromString(message: String): Message { val args: List<String> = message.split("|")
        val content: List<String> = args.subList(1, args.size)
        return Message(MessageIdentifier.valueOf(args[0]), content)
    }

    fun createLoginMessage(username: String): Message {
        return Message(MessageIdentifier.LOGIN, arrayListOf(username))
    }

    fun createLogoutMessage(): Message {
        return Message(MessageIdentifier.LOGOUT, emptyList())
    }
}
