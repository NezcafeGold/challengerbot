package com.nzqk.challengerbot.repository

import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.model.Task
import com.nzqk.challengerbot.model.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findAllByOwner(owner: Owner): List<Task>

    fun findAllByDeadlineBeforeAndStatus(date: Date, status: TaskStatus): List<Task>
}
