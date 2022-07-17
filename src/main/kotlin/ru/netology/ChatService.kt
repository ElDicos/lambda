package ru.netology

import java.lang.RuntimeException

object ChatService {

    fun clearTest() {
        chats.clear()
        chatId = 1
        MsgService.clear()
    }

    private val chats = mutableListOf<Chat>()
    private var chatId = 1

    fun createMsg(fromId: Int, toId: Int, text: String = "TestMsg"): Chat {
        val chat = chats.filter { Chat ->
            Chat.fromId == fromId && Chat.toId == toId
        }
        return if (chat.isEmpty()) {
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
            return newChat
        } else {
            val actualChat = chat[0]
            val message = MsgService.add(fromId = fromId, toId = toId, chatId = actualChat.id, text = text)
            var newMsgList = actualChat.messages
            newMsgList.add(message)
            val updatedChat = updateChatUnread(actualChat, newMsgList)
            chats.add(updatedChat)
            chats.remove(actualChat)
            updatedChat
        }
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
        }
        val unreadChatsCount = userChatList.filter { Chat ->
            Chat.unreadChat
        }
        if (unreadChatsCount.isNotEmpty()) {
            return unreadChatsCount.size
        }
        return 0
    }

    fun getUnreadChats(userId: Int): List<Chat>? {
        val userChatList = chats.filter { Chat ->
            Chat.toId == userId
        }
        val unreadChats = userChatList.filter { Chat ->
            Chat.unreadChat
        }
        if (unreadChats.isNotEmpty()) {
            return unreadChats
        }
        throw RuntimeException("Нет непрочитанных чатов")
    }

    fun getMessages(chatId: Int): List<Msg>? {
        val chatList = chats.filter { Chat ->
            Chat.id == chatId
        }
        if (chatList.isNotEmpty()) {
            val chat = chatList[0]
            val messages = chat.messages
            val inUnread = messages.filter { Msg ->
                Msg.id > chat.inRead
            }
            val unreadCount = inUnread.size
            val lastMsg = inUnread.maxOf { Msg ->
                Msg.id
            }
            val updatedChat = chat.copy(
                inRead = lastMsg,
                unreadCount = chat.unreadCount - unreadCount,
                unreadChat = false)

            chats.remove(chat)
            chats.add(updatedChat)
            return inUnread
        }
        throw RuntimeException("Чат не найден")
    }

    fun deleteMsg(chatId: Int, msgId: Int): Boolean {
        val chat = chats.filter { Chat ->
            Chat.id == chatId
        }
        if (chat.isEmpty()) {
            throw IndexOutOfBoundsException("Чат не найден")
        }
        val chatForUpdate = chat[0]
        val msgForDelete = chatForUpdate.messages.filter { Msg ->
            Msg.id == msgId
        }
        if (msgForDelete.isEmpty()) {
            throw IndexOutOfBoundsException("Сообщение не найдено")
        }
        chatForUpdate.messages.remove(msgForDelete[0])
        if (chatForUpdate.messages.isEmpty()) {
            chats.remove(chatForUpdate)
            return true
        }
        return true
    }

    fun updateMsg(chatId: Int, msgId: Int, newText: String): Msg {
        val chat = chats.filter { Chat ->
            Chat.id == chatId
        }
        if (chat.isEmpty()) {
            throw IndexOutOfBoundsException("Чат не найден")
        }
        val chatForUpdate = chat[0]
        val msgForUpdate = chatForUpdate.messages.filter { Msg ->
            Msg.id == msgId
        }
        if (msgForUpdate.isEmpty()) {
            throw IndexOutOfBoundsException("Сообщение не найдено")
        }
        val newMsg = msgForUpdate[0].copy(text = newText)
        chatForUpdate.messages.remove(msgForUpdate[0])
        chatForUpdate.messages.add(newMsg)

        return newMsg
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

}