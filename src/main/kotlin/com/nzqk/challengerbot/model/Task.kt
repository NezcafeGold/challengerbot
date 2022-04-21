package com.nzqk.challengerbot.model

import java.sql.Date
import javax.persistence.*

@Table(name = "task")
@Entity
class Task(

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private val owner: Owner? = null,

    private val title: String? = null,
    private val description: String? = null,
    private val deadline: Date? = null,
    private val status: Boolean? = null,
    private val report: String? = null,
) : BaseEntity()