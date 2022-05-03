package com.nzqk.challengerbot.observer.notificator

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import com.nzqk.challengerbot.ChallengerBot
import com.nzqk.challengerbot.model.TaskStatus
import com.nzqk.challengerbot.observer.JobTimer
import com.nzqk.challengerbot.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Component
class TaskNotificator : JobTimer(1L.minutes.toLong(DurationUnit.MILLISECONDS)) {

    @Autowired
    private lateinit var taskService: TaskService

    override fun job() {
        val tasks = taskService.getAllTaskWithDeadline()

        tasks.forEach { task ->
            task.status = TaskStatus.NOTIFIED
            ChallengerBot.bot.sendMessage(
                chatId = ChatId.fromId(task.chat!!),
                text = notification() + task.toString(),
                parseMode = ParseMode.HTML,
            )
            taskService.addTask(task)
        }
    }

    private fun notification() = "<i>ЗАКОНЧИЛСЯ СРОК ПО ЗАДАЧЕ</i>\n\n"
}
