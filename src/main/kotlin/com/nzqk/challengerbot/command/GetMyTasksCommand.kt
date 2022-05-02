package com.nzqk.challengerbot.command

import com.github.kotlintelegrambot.entities.Message
import com.nzqk.challengerbot.model.Task
import com.nzqk.challengerbot.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetMyTasksCommand {

    @Autowired
    private lateinit var taskService: TaskService

    fun execute(message: Message): List<Task> {
        return taskService.getAllTaskWithOwnerId(message.from!!.id)
    }
}
