package com.unitri.edu.queues.domain.controller

import com.unitri.edu.queues.domain.InputDto
import com.unitri.edu.queues.domain.history.Output
import com.unitri.edu.queues.domain.history.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/queue")
class QueuesController {

    @Autowired
    lateinit var service: Service

    @GetMapping("/single-server")
    fun getSingleServerQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getSingleServerQueue(
            inputDto
        )
    }

   @GetMapping("/m-servers")
    fun getMServersQueue(
       @RequestBody inputDto: InputDto
    ): Output {
        return service.getMServersQueue(
            inputDto
        )
    }

    @GetMapping("/infinite-servers")
    fun getInfiniteServersQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getInfiniteServersQueue(
            inputDto
        )
    }

    @GetMapping("/finite-capacity-server")
    fun getFiniteCapacityServerQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getFiniteCapacityServerQueue(
          inputDto
        )
    }

    @GetMapping("/finite-capacity-m-servers")
    fun getFiniteCapacityMServersQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getFiniteCapacityMServersQueue(
            inputDto
        )
    }

    @GetMapping("/finite-population-servers")
    fun getFinitePopulationServerQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getFinitePopulationServerQueue(
            inputDto
        )
    }

    @GetMapping("/finite-population-infinite-servers")
    fun getFinitePopulationInfiniteServersQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getFinitePopulationInfiniteServersQueue(
            inputDto
        )
    }

    @GetMapping("/finite-capacity-population-m-servers")
    fun getFiniteCapacityPopulationMServersQueue(
        @RequestBody inputDto: InputDto
    ): Output {
        return service.getFiniteCapacityPopulationServersQueue(
            inputDto
        )
    }
}