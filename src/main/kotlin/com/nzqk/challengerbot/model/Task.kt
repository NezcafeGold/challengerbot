package com.nzqk.challengerbot.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*

@Table(name = "task")
@Entity
class Task(
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    var owner: Owner? = null,
    var chat: Long? = null,
    var title: String? = null,
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var deadline: Date = Date(),
    @Enumerated(EnumType.STRING)
    var status: TaskStatus? = TaskStatus.PROGRESS,
    var report: String? = null,
) : IdBaseEntity() {

    fun getFormatDeadline(): String? {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return LocalDateTime.ofInstant(deadline.toInstant(), ZoneId.systemDefault()).format(formatter)
    }

    fun toShortString(): String = "$id: $title. До: ${getFormatDeadline()}"

    override fun toString(): String = """
        <b>Номер:</b> $id
        <b>Владелец:</b> ${owner?.name ?: ""}
        
        <b>$title</b>
        
        $description
        
        <b>Срок завершения:</b> ${getFormatDeadline()}
        <b>Статус:</b> ${status!!.message}
    """.trimIndent()

    fun deadlinePlusDays(days: Long) {
        deadline = Date.from(
            LocalDateTime.now().atZone(ZoneId.systemDefault()).plusDays(days)
                .toInstant()
        )
    }

    fun deadlinePlusHours(hours: Long) {
        deadline = Date.from(
            LocalDateTime.now().atZone(ZoneId.systemDefault()).plusHours(hours)
                .toInstant()
        )
    }
}
