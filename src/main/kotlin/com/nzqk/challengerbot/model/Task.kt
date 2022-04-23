package com.nzqk.challengerbot.model

import java.sql.Date
import javax.persistence.*

@Table(name = "task")
@Entity
class Task(
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    var owner: Owner? = null,
    var title: String? = null,
    var description: String? = null,
    var deadline: Date = Date(java.util.Date().time),
    var status: Boolean? = null,
    var report: String? = null,
) : IdBaseEntity()