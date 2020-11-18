package com.unitri.edu.queues.domain.history

import com.unitri.edu.queues.domain.bases.BaseEntity
import javax.persistence.Entity

@Entity
class Input(
    var requisition: Double = 0.0,
    var processing: Double = 0.0,
    var users: Int = 0,
    var servers: Int = 0,
    var noRequisitions: Double = 0.0,
    var rowB: Int = 0,
    var population: Int = 0
): BaseEntity()