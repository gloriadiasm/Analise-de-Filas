package com.unitri.edu.queues.domain.bases

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditEntity : BaseEntity() {

    @Column(nullable = false)
    @CreatedDate
    open var createdAt: Instant? = null

    @Column(nullable = false)
    @LastModifiedDate
    open var updatedAt: Instant? = null
}
