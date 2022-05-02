package com.nzqk.challengerbot.repository

import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findAllByOwner(owner: Owner): List<Task>
}
