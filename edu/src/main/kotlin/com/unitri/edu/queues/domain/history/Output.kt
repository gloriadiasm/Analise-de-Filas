package com.unitri.edu.queues.domain.history

import com.unitri.edu.queues.domain.bases.BaseEntity
import javax.persistence.Entity

@Entity
class Output (
    var trafficIntensity: Double? = null,
    val noUserProbability: Double? = null,
    val probabilityOfUser: Double? = null,
    val probabilityOfNUserOrMore: Double? = null,
    val probabilityOfUserOrMore: Double? = null,
    val averageResponseTime: Double? = null,
    val averageTimeOfQueueWaiting: Double? = null,
    val averageUsers: Double? = null,
    val averageRequisition: Double? = null,
    val cerlang: Double? = null,
    val utilization: Double? = null,
    val probabilityOFNUser: Double? = null,
    val serviceRequest: Double? = null,
    val averageOfWaitingRequests: Double? = null,
    val receivingRate: Double? = null,
    val lossRate: Double? = null,
    val averageUsersQueueWaiting: Double? = null
): BaseEntity()