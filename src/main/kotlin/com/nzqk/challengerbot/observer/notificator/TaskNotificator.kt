package com.nzqk.challengerbot.observer.notificator

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import com.nzqk.challengerbot.ChallengerBot
import com.nzqk.challengerbot.model.TaskStatus
import com.nzqk.challengerbot.observer.JobTimer
import com.nzqk.challengerbot.service.TaskService
import com.nzqk.challengerbot.utils.ApplicationContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Component
class TaskNotificator : JobTimer(1L.minutes.toLong(DurationUnit.MILLISECONDS)) {

    @Autowired
    private lateinit var taskService: TaskService

    val appCtx: ApplicationContext by lazy { ApplicationContextUtils.applicationContext!! }
    val challengerBot by lazy { appCtx.getBean("challengerBot", ChallengerBot::class.java) }

    override fun job() {
        val tasks = taskService.getAllTaskWithDeadline()

        tasks.forEach { task ->
            task.status = TaskStatus.NOTIFIED
            challengerBot.bot.sendMessage(
                chatId = ChatId.fromId(task.chat!!),
                text = notification(task.owner!!.id!!, task.owner!!.name!!) + task.toString(),
                parseMode = ParseMode.HTML,
            )
            taskService.addTask(task)
        }
    }

    private fun notification(id: Long, name: String) = "<a href=\"tg://user?id=$id\">$name</a>" +
        " \n<i>ЗАКОНЧИЛСЯ СРОК ПО ЗАДАЧЕ</i>\n\n"
}
