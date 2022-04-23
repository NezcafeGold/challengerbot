package com.nzqk.challengerbot.service

import com.nzqk.challengerbot.model.Owner
import com.nzqk.challengerbot.repository.OwnerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OwnerService() {

    @Autowired
    private lateinit var ownerRepository: OwnerRepository

    fun addNewOwner(owner: Owner) {
        ownerRepository.save(owner)

    }

    fun getOwner(id: Long) =
        ownerRepository.findById(id)

}