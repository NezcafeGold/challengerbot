package com.nzqk.challengerbot.command

import com.github.kotlintelegrambot.entities.Message
import com.nzqk.challengerbot.model.Task
import com.nzqk.challengerbot.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetTaskCommand : ICommand<Task> {

    @Autowired
    private lateinit var taskService: TaskService

    override fun execute(message: Message): Task =
        message.text?.replaceBefore(" ", "")?.trim()?.toLongOrNull()
            ?.let {
                taskService.getTask(it).get()
            } ?: Task()


}