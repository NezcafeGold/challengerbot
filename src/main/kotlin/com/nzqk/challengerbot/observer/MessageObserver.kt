package com.nzqk.challengerbot.observer

import java.util.*
import kotlin.concurrent.thread

class MessageObserver {

    private val lifeTime: Long = 1800000L
    private lateinit var thread: Thread

    private fun destroyAllOldMessage() {
        val iterator = messages.iterator()
        val currentTime = System.currentTimeMillis()
        while (iterator.hasNext()) {
            if (currentTime - iterator.next().value.createdTime > lifeTime) {
                iterator.remove()
            }
        }
    }

    fun startTimer() {
        val timeForCheck = 600_000L
        thread = thread {
            var savedTime = System.currentTimeMillis()
            while (true) {
                if (System.currentTimeMillis() - savedTime > timeForCheck) {
                    destroyAllOldMessage()
                    savedTime = System.currentTimeMillis()
                }
            }
        }
    }

    fun get(messageId: Long) =
            messages[messageId]


    fun add(messageId: Long, chatId: Long, userId: Long) {
        messages[messageId] = CacheMessage(chatId = chatId, userId = userId)
    }

    fun add(messageId: Long, cacheMessage: CacheMessage) {
        messages[messageId] = cacheMessage
    }

    fun remove(messageId: Long) {
        messages.remove(messageId)
    }

    companion object {
         val messages: MutableMap<Long, CacheMessage> = Collections.synchronizedMap(mutableMapOf<Long, CacheMessage>())
    }
}