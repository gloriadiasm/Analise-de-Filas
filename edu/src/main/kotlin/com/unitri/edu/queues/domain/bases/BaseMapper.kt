package com.unitri.edu.queues.domain.bases

import org.mapstruct.Mapping
import org.mapstruct.ObjectFactory
import org.mapstruct.TargetType
import org.springframework.beans.factory.annotation.Autowired
import java.util.Optional
import javax.persistence.EntityManager

abstract class BaseMapper<Entity : BaseEntity, Dto : BaseDto> {
    @Autowired
    lateinit var em: EntityManager

    protected open fun findEntity(id: Long): Optional<Entity> = Optional.empty<Entity>()

    protected open fun resolve(id: Long, type: Class<Entity>) =
        em.find(type, id) ?: findEntity(id).orElseThrow {
            throw Exception("Register not found")
        }

    protected open fun findEntity(dto: Dto): Optional<Entity> = Optional.empty<Entity>()

    protected open fun resolve(dto: Dto, type: Class<Entity>): Entity {
        val id = dto.id
        return if (id != null) {
            resolve(id, type)
        } else {
            findEntity(dto).orElseGet {
                type.getDeclaredConstructor().newInstance()
            }
        }
    }

    @ObjectFactory
    open fun createEntityByRef(ref: Long?, @TargetType type: Class<Entity>): Entity? {
        return ref?.let { resolve(ref, type) }
    }

    @ObjectFactory
    open fun createEntity(dto: Dto?, @TargetType type: Class<Entity>): Entity? {
        return dto?.let { resolve(dto, type) }
    }

    abstract fun toDto(entity: Entity): Dto

    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(id: Long?): Entity

    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(dto: Dto): Entity

    open fun toId(entity: Entity?): Long? = entity?.id
}