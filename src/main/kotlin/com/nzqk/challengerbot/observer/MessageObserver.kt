package com.nzqk.challengerbot.observer

import org.springframework.stereotype.Component
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Component
class MessageObserver : JobTimer(30L.minutes.toLong(DurationUnit.MILLISECONDS)) {

    private val lifeTime: Long = 30L.minutes.toLong(DurationUnit.MILLISECONDS)

    private fun destroyAllOldMessage() {
        val iterator = messages.iterator()
        val currentTime = System.currentTimeMillis()
        while (iterator.hasNext()) {
            if (currentTime - iterator.next().value.createdTime > lifeTime) {
                iterator.remove()
            }
        }
    }

    override fun job() = destroyAllOldMessage()

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
        private val messages: MutableMap<Long, CacheMessage> = Collections.synchronizedMap(mutableMapOf<Long, CacheMessage>())
    }
}
