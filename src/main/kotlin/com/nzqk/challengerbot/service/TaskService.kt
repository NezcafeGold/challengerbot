package com.nzqk.challengerbot.service

import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.model.Task
import com.nzqk.challengerbot.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService() {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    fun addTask(task: Task): Task {
        taskRepository.save(task)
        return task
    }

    fun getTask(id: Long) =
        taskRepository.findById(id)

    fun getAllTaskWithOwnerId(ownerId: Long) = taskRepository.findAllByOwner(Owner().also { it.id = ownerId })
}
