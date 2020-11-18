package com.unitri.edu.queues.domain.history

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OutputRepository: JpaRepository<Output, Long>