package fr.ifa.kotlinchat.common.message

enum class MessageIdentifier
{
    LOGIN,
    HISTORY,
    SEND,
    SEND_MULTI,
    LOGOUT,
}

const val END_DELIMITER = "/END"
