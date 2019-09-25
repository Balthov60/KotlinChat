package fr.ifa.kotlinchat.common.message

data class Message(
    val identifier: MessageIdentifier,
    val content: List<String>
) {
    override fun toString() = "$identifier|${content.joinToString("|")}"
}