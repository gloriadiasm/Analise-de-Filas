package com.unitri.edu.queues.domain.login

import com.unitri.edu.queues.domain.user.User
import com.unitri.edu.queues.domain.user.UserDto
import com.unitri.edu.queues.domain.user.UserRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@Service
open class LoginService {

    @Autowired
    lateinit var userRepository: UserRepository
    var passwordEncoder: PasswordEncoder = getEncoder()

    fun signUp(user: User): User {
        userRepository.findByEmail(user.email) ?: return userRepository.save(user)
        throw Exception("Usuário já cadastrado")
    }

    fun login(user: User): User {
        val userDb = userRepository.findByEmail(user.email)
            ?: throw NotFoundException("User with email :: ${user.email} not found")
        validateUserLogin(UserDto(user.email, user.password))
        return User(userDb.email, userDb.password).also { it.id = userDb.id }
    }

    fun validateUserLogin(userDto: UserDto): Boolean {
        val user = findByEmail(userDto.email)
        return checkPassword(userDto.password, user.password)
    }

    fun checkPassword(userInput: String, dbPassword: String): Boolean {
        if (userInput.isBlank() || dbPassword.isBlank()) {
            throw Exception("PASSWORD_CANNOT_BE_EMPTY")
        }
        return passwordEncoder.matches(userInput, dbPassword)
    }

    fun findByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(
            email
        ) ?: throw NotFoundException("User with email :: $email not found")
        return UserDto(user.email, user.password)
    }

    @Bean
    fun getEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}