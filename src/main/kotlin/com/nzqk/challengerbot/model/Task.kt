package com.nzqk.challengerbot.model

import java.time.LocalDateTime
import java.time.ZoneId
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
    var status: Boolean? = null,
    var report: String? = null,
) : IdBaseEntity() {

    fun toShortString(): String = "$id: $title. До: $deadline"

    override fun toString(): String = """
        <b>Номер:</b> $id
        
        <b>$title</b>
        
        <b>Владелец:</b> ${owner?.name ?: ""}
        
        <code>$description</code>
        
        <b>Срок завершения:</b> $deadline
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
