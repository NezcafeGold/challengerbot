package com.nzqk.challengerbot.observer

import com.nzqk.challengerbot.model.Task

data class CacheMessage(
        var userId: Long,
        var chatId: Long,
        var createdTime: Long = System.currentTimeMillis(),
        var task: Task = Task()
)