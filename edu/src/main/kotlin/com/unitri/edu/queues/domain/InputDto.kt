package com.unitri.edu.queues.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.unitri.edu.queues.domain.bases.BaseDto

@JsonIgnoreProperties(ignoreUnknown = true)
data class InputDto (
    var user: Long = 0L,
    var requisition: Double = 0.0,
    var processing: Double = 0.0,
    var users: Int = 0,
    var servers: Int = 0,
    var noRequisitions: Double = 0.0,
    var rowB: Int = 0,
    var population: Int = 0
): BaseDto()