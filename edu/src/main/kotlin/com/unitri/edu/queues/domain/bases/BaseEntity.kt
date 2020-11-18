package com.unitri.edu.queues.domain.bases

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.id.enhanced.SequenceStyleGenerator
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity : Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator")
    @GenericGenerator(
        name = "idGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(
                name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY,
                value = "true"
            )
        ]
    )
    override var id: Long? = null
}