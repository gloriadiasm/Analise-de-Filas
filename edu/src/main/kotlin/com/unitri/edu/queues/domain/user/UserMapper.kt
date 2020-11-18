package com.unitri.edu.queues.domain.user

class UserMapper{
    fun toEntity(dto: UserDto): User{
        return User(
            email = dto.email,
            password = dto.password
        ).also { it.id = dto.id }
    }
    fun toDto(entity: User): UserDto{
        return UserDto(
            entity.email,
            entity.password
        ).also { it.id = entity.id }
    }
}