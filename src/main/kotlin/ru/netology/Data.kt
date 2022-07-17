package ru.netology

data class Chat(
    val id: Int = 1,
    val fromId: Int = 1,
    val toId: Int = 1,
    val messages: MutableList<Msg>,
    val inRead: Int = 0,
    val outRead: Int = 0,
    val unreadCount: Int = 1,
    val unreadChat: Boolean = true,
) {

}

data class Msg(
    val id: Int = 1,
    val fromId: Int = 1,
    val toId: Int = 1,
    val chatId: Int = 1,
    val text: String = "default_msg",
)