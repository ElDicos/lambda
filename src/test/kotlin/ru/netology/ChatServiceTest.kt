package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clear() {
        ChatService.clearTest()
    }

    @Test
    fun createMsg_correctNewChat() {
        val defaultMessage = Msg()
        val expectedChat = Chat(messages = mutableListOf(defaultMessage))

        val testResult = ChatService.createMsg(1, 1, "default_msg")

        assertEquals(expectedChat, testResult)

    }

    @Test
    fun createMsg_correctAddMessageToExistingChat() {
        val message1 = Msg()
        val message2 = Msg(id = 2)


        val expectedChat = Chat(messages = mutableListOf(message1, message2))

        val testResult = ChatService.createMsg(1, 1, "default_msg")
        ChatService.createMsg(1, 1, "default_msg")

        assertEquals(expectedChat, testResult)

    }

    @Test
    fun addChat_correctChatCreate() {
        val expectedChat = Chat(messages = mutableListOf())

        val res = ChatService.addChat(messages = mutableListOf())

        assertEquals(expectedChat, res)
    }

    @Test
    fun updateChatUnread_shouldCorrectChatUpdating() {
        val message1 = Msg()
        val message2 = Msg(id = 2)

        val expectedChat = Chat(messages = mutableListOf(message1, message2), unreadCount = 2)

        val testChat = ChatService.createMsg(1, 1, "default_msg")
        val result = ChatService.updateChatUnread(testChat, mutableListOf(message1, message2))

        assertEquals(expectedChat, result)

    }

    @Test
    fun getUnreadChatsCount_shouldCountCorrect() {
        ChatService.createMsg(1, 1, "default_msg")
        ChatService.createMsg(2, 1, "default_msg")
        ChatService.createMsg(3, 1, "default_msg")

        val expectedResult = 3

        val result = ChatService.getUnreadChatsCount(1)

        assertEquals(expectedResult, result)

    }

    @Test
    fun getUnreadChats_shouldGetCorrectListOfChats() {
        val chat1 = ChatService.createMsg(1, 1, "default_msg")
        val chat2 = ChatService.createMsg(2, 1, "default_msg")

        val expectedChatList = mutableListOf<Chat>(chat1, chat2)

        val result = ChatService.getUnreadChats(1)

        assertEquals(expectedChatList, result)
    }

    @Test(expected = RuntimeException::class)
    fun getUnreadChats_shouldThrow() {
        val result = ChatService.getUnreadChats(1)
    }

    @Test
    fun getMessages_ShouldGetMessagesList() {
        val msg1 = ChatService.createMsg(1, 1, "1")
        val msg2 = ChatService.createMsg(1, 1, "2")

        val expectedList = mutableListOf<Msg>(Msg(text = "1"), Msg(id = 2, fromId = 1, text = "2"))
        val res = ChatService.getMessages(1)

        assertEquals(expectedList, res)
    }

    @Test(expected = RuntimeException::class)
    fun getMessages_shouldThrowNoChatFounded() {
        val msg1 = ChatService.createMsg(1, 1, "1")
        val msg2 = ChatService.createMsg(2, 1, "2")
        ChatService.getMessages(1)
        ChatService.getMessages(1)
    }

    @Test
    fun deleteMsg_shouldCorrectDelete() {
        val msg1 = ChatService.createMsg(1, 1, "1")
        val msg2 = ChatService.createMsg(2, 1, "2")

        val expectedTrue = ChatService.deleteMsg(1, 1)

        assertTrue(expectedTrue)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun deleteMsg_shouldThrowChatNotFound() {
        val msg1 = ChatService.createMsg(1, 1, "1")
        val msg2 = ChatService.createMsg(2, 1, "2")
        val expectedTrue = ChatService.deleteMsg(2, 1)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun deleteMsg_shouldThrowCMessageNotFound() {
        val msg1 = ChatService.createMsg(1, 1, "1")
        val msg2 = ChatService.createMsg(2, 1, "2")
        val expectedTrue = ChatService.deleteMsg(1, 10)

    }

    @Test
    fun updateMsg_shouldUpdateCorrect() {
        ChatService.createMsg(1, 1, "1")
        val expectedMsg = Msg(text = "TestDone")
        val testRes = ChatService.updateMsg(1, 1, "TestDone")

        assertEquals(expectedMsg, testRes)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun updateMsg_shouldThrowException() {
        ChatService.createMsg(1, 1, "1")
        val expectedMsg = Msg(text = "TestDone")
        val testRes = ChatService.updateMsg(2, 1, "TestDone")
    }


    @Test
    fun deleteChat_shouldCorrectDeleteChat() {
        ChatService.createMsg(1, 1, "1")
        val expectedTrue = ChatService.deleteChat(1)

        assertTrue(expectedTrue)
    }

    @Test(expected = Exception::class)
    fun deleteChat_shouldThrowChatNotFound() {
        ChatService.deleteChat(1)
    }
}


