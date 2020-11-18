package com.unitri.edu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
open class QueuesApplication
fun main(args: Array<String>) {
	runApplication<QueuesApplication>(*args)
}