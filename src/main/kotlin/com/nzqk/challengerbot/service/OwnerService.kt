package com.nzqk.challengerbot.service

import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.repository.OwnerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OwnerService {

    @Autowired
    private var ownerRepository: OwnerRepository? = null

    fun addNewOwner(id2: Long){
//        ownerRepository?.save(Owner(id2))
    }
}