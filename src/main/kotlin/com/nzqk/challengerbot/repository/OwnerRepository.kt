package com.nzqk.challengerbot.repository

import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.service.OwnerService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<Owner, Long>


fun main(){

//    OwnerService().s
}