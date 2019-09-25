package fr.ifa.kotlinchat.common.message

data class Message(
    val identifier: MessageIdentifier,
    val content: List<String>
) {
    override fun toString() = "$identifier|${content.joinToString("|")}\n"

    fun getUsername() : String {
        return content[0]
    }

    fun getUserMessageContent(): String {
        return content[1]
    }
}