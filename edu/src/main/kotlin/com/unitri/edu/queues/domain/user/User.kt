package com.unitri.edu.queues.domain.user

import com.unitri.edu.queues.domain.bases.BaseAuditEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tb_user")
class User(
    var email: String = "",
    var password: String = ""
): BaseAuditEntity()