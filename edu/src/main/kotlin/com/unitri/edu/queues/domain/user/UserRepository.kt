package com.unitri.edu.queues.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
@Transactional
interface UserRepository: JpaRepository<User, Long>{
    fun findByEmail(email: String): User?
}