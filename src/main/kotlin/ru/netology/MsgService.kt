package ru.netology

object MsgService {

    var newId = 1

    fun add(fromId: Int = 1, toId: Int = 1, chatId: Int = 1, text: String = "_"): Msg {
        val newMsg = Msg(
            id = newId,
            fromId = fromId,
            toId = toId,
            chatId = chatId,
            text = text)
        newId++
        return newMsg
    }

    fun clear() {
        newId = 1
    }

}