package com.nzqk.challengerbot.command

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.User
import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.model.Task
import com.nzqk.challengerbot.service.OwnerService
import com.nzqk.challengerbot.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateTaskCommand() : ICommand<Task> {

    @Autowired
    private lateinit var ownerService: OwnerService

    @Autowired
    private lateinit var taskService: TaskService

    override fun execute(message: Message): Task {
        val user = requireNotNull(message.from)
        val owner = findOwner(user)

        return createTaskForOwner(owner)
    }

    private fun createTaskForOwner(owner: Owner): Task {
        val task = Task().apply {
            this.owner = owner
        }
        return taskService.addTask(task)
    }

    private fun findOwner(user: User): Owner {
        var owner: Owner? = ownerService.getOwner(user.id).let {
            if (!it.isEmpty)
                it.get()
            else null
        }
        if (owner?.id == null) {
            owner = Owner().apply {
                id = user.id
                name = user.username
            }
            ownerService.addNewOwner(owner)
        }
        return owner
    }

}