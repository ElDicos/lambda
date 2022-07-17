package ru.netology

interface BasicOperations<T> {

    fun add(id: Int, item: T) : T

    fun update(item: T) : Boolean

    fun delete(item: T) : Boolean

    fun get(id: Int): List<T>
}