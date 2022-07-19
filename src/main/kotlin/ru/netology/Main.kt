package ru.netology

fun main () {
    println( ChatService.createMsg(1, 1, "a"))
    println(ChatService.createMsg(1, 1, "b"))
    println(ChatService.createMsg(1, 1, "c"))
    ChatService.deleteMsg(1, 2)
    ChatService.deleteMsg(1, 1)

    println(ChatService.getMessages(1))
    println()

   ChatService.createMsg(1, 1, "d")

}