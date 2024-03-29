package com.nzqk.challengerbot.repository

import com.nzqk.challengerbot.model.Owner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<Owner, Long>

