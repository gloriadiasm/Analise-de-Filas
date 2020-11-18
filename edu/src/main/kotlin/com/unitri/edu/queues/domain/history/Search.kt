package com.unitri.edu.queues.domain.history

import com.unitri.edu.queues.domain.bases.BaseAuditEntity
import com.unitri.edu.queues.domain.user.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Search(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var input: Input,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var output: Output
): BaseAuditEntity()