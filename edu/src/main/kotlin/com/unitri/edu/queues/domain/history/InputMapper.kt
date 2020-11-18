package com.unitri.edu.queues.domain.history

import com.unitri.edu.queues.domain.InputDto

open class InputMapper {
    fun toDto(entity: Input, userId: Long): InputDto {
        return InputDto(
            user = userId,
            requisition = entity.requisition,
            processing = entity.processing,
            users = entity.users,
            servers = entity.servers,
            noRequisitions = entity.noRequisitions,
            rowB = entity.rowB,
            population = entity.population
        )
    }
    fun toEntity(dto: InputDto): Input{
        return Input(
            requisition = dto.requisition,
            processing = dto.processing,
            users = dto.users,
            servers = dto.servers,
            noRequisitions = dto.noRequisitions,
            rowB = dto.rowB,
            population = dto.population
        )
    }
}