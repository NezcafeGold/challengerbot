package com.nzqk.challengerbot.model

import javax.persistence.*


@Entity
class Owner : BaseEntity() {
    @Id
    override var id: Long? = null

    @Column
    var name: String? = null

    @Column
    var isAdmin: Boolean = false
}