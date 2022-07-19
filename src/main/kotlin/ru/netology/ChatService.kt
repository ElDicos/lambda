package ru.netology

import java.lang.RuntimeException

object ChatService {

    private val chats = mutableListOf<Chat>()
    private var chatId = 1

    fun createMsg(fromId: Int, toId: Int, text: String = "TestMsg"): Chat {
        val chat = chats.firstOrNull() {
            it.fromId == fromId && it.toId == toId
        }?.let {
            val actualChat = it
            val message = MsgService.add(fromId = fromId, toId = toId, chatId = it.id, text = text)
            var newMsgList = it.messages
            newMsgList.add(message)
            val updatedChat = updateChatUnread(it, newMsgList)
            chats.add(updatedChat)
            chats.remove(it)
            updatedChat
        } ?: let {
            val message = MsgService.add(fromId = fromId, toId = toId, chatId = chatId, text = text)
            val newChat = addChat(
                id = chatId,
                fromId = fromId,
                toId = toId,
                messages = mutableListOf(message),
                unreadChat = true,
            )
            chats.add(newChat)
            chatId++
            newChat
        }
        return chat
    }

    fun addChat(
        id: Int = 1,
        fromId: Int = 1,
        toId: Int = 1,
        messages: MutableList<Msg>,
        inRead: Int = 0,
        outRead: Int = 0,
        unreadCount: Int = 1,
        unreadChat: Boolean = true,
    ): Chat {
        val chat = Chat(
            id = id,
            fromId = fromId,
            toId = toId,
            messages = messages,
            inRead = inRead,
            outRead = outRead,
            unreadCount = unreadCount,
            unreadChat = unreadChat)
        return chat
    }

    fun updateChatUnread(chat: Chat, newMessageList: MutableList<Msg>): Chat {
        val updatedChat = chat.copy(
            messages = newMessageList,
            unreadCount = chat.unreadCount + 1,
            unreadChat = true
        )
        return updatedChat
    }

    fun getUnreadChatsCount(userId: Int): Int {
        val userChatList = chats.filter { Chat ->
            Chat.toId == userId
        }.filter {
            it.unreadChat
        }.size
        return userChatList
    }

    fun getUnreadChats(userId: Int): List<Chat>? {
        val userChatList = chats.filter { Chat ->
            Chat.toId == userId
        }.filter {
            it.unreadChat
        }.ifEmpty {
            throw RuntimeException("Нет непрочитанных чатов")
        }.toList()
        return userChatList
    }

    fun getMessages(chatId: Int): List<Msg>? {
        val chat: Chat
        val messagesCount: Int
        val inReadId: Int
        val chatList = chats.asSequence().first { Chat -> Chat.id == chatId }. let {
            chat = it
            inReadId = it.inRead
            messagesCount = it.unreadCount
            it.messages
        }.filter { Msg -> Msg.id > inReadId }
            .take(messagesCount).ifEmpty { throw RuntimeException("Нет непрочитанных сообщений") }

        val newInRead = chatList.maxOf { Msg ->
            Msg.id
        }

        val updatedChat = chat.copy(
            inRead = newInRead,
            unreadCount = chat.unreadCount - messagesCount,
            unreadChat = false,
        )
        chats.remove(chat)
        chats.add(updatedChat)

        return chatList
    }

    fun deleteMsg(chatId: Int, msgId: Int): Boolean {
        var chat: Chat
        var newMessageList: MutableList<Msg>

        val message = chats.filter {
            it.id == chatId
        }.ifEmpty { throw IndexOutOfBoundsException("Чата не существует") }
            .let {
                chat = it[0]
                newMessageList = it[0].messages
                it[0].messages
            }.filter{
                it.id == msgId
            }.ifEmpty { throw IndexOutOfBoundsException("Сообщения не существует") }
            .first {
                it.id == msgId
            }
        chats.remove(chat)
        newMessageList.remove(message)
        if (newMessageList.size == 0) {
           return true
        }
        chats.add(chat.copy(messages = newMessageList))
        return true
    }

    fun updateMsg(chatId: Int, msgId: Int, newText: String): Msg {
        var chat: Chat
        var newMessageList: MutableList<Msg>
        val message = chats.filter {
            it.id == chatId
        }.ifEmpty { throw IndexOutOfBoundsException("Чата не существует") }
            .let {
                chat = it[0]
                newMessageList = it[0].messages
                it[0].messages
            }.filter{
                it.id == msgId
            }.ifEmpty { throw IndexOutOfBoundsException("Сообщения не существует") }
            .first {
                it.id == msgId
            }

        val newMessage = message.copy(text = newText)
        newMessageList.add(newMessage)
        newMessageList.remove(message)
        chats.remove(chat)
        chats.add(chat.copy(messages = newMessageList))
        return newMessage

    }

    fun deleteChat(chatId: Int): Boolean {
        val chat = chats.filter { Chat ->
            Chat.id == chatId
        }
        return if (chat.isNotEmpty()) {
            chats.remove(chat[0])
            true
        } else {
            throw Exception("Чат не найден")
        }
    }

    fun clearTest() {
        chats.clear()
        chatId = 1
        MsgService.clear()
    }

}