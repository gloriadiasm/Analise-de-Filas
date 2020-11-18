package com.unitri.edu.queues.domain.controller

import com.unitri.edu.queues.domain.login.LoginService
import com.unitri.edu.queues.domain.login.UserAuthenticationDto
import com.unitri.edu.queues.domain.user.UserDto
import com.unitri.edu.queues.domain.user.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class LoginController {
    @Autowired
    lateinit var loginService: LoginService
    val userMapper =  UserMapper()

    @PostMapping("/signup")
    fun signUp(@RequestBody userDto: UserDto): UserDto {
        return userMapper.toDto(loginService.signUp(userMapper.toEntity(userDto)))
    }

    @PostMapping("/login")
    fun authenticate(@RequestBody userDto: UserDto): UserDto {
        return userMapper.toDto(loginService.login(userMapper.toEntity(userDto)))
    }
}