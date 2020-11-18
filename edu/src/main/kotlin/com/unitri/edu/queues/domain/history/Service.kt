package com.unitri.edu.queues.domain.history

import com.unitri.edu.queues.domain.InputDto
import com.unitri.edu.queues.domain.user.User
import com.unitri.edu.queues.domain.user.UserRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.round

@Service
class Service {

    @Autowired
    lateinit var searchRepository: SearchRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var inputRepository: InputRepository
    @Autowired
    lateinit var outputRepository: OutputRepository
    val inputMapper = InputMapper()

    fun getSingleServerQueue(
        input: InputDto
    ): Output {
        val trafficIntensity: Double = getTrafficIntensity(input.requisition, input.processing, 1)
        val noUserProbability: Double = 1 - (trafficIntensity)
        val nUser: Double = trafficIntensity.pow(input.users) * noUserProbability
        val nMoreUser: Double = trafficIntensity.pow(input.users)
        val moreUser: Double = trafficIntensity.pow(1)

        if (1.0.minus(trafficIntensity).equals(0.0)) {
            throw Exception(com.unitri.edu.queues.domain.Message.INVALID_CALCULATION)
        }
        val avgTime: Double = 1 / (input.processing * (1 - trafficIntensity))
        val avgUser: Double = trafficIntensity / (1 - trafficIntensity)
        val avgRequisition: Double = trafficIntensity.pow(2).div(1 - trafficIntensity)

        var output = Output(
            trafficIntensity = round(trafficIntensity * 100),
            noUserProbability = round(noUserProbability * 100),
            probabilityOfUser = round(nUser * 100),
            probabilityOfNUserOrMore = round(nMoreUser * 100),
            probabilityOfUserOrMore = round(moreUser * 100),
            averageResponseTime = avgTime,
            averageTimeOfQueueWaiting = avgTime,
            averageUsers = round(avgUser),
            averageRequisition = round(avgRequisition)
            )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

   fun getMServersQueue(
       input: InputDto
    ): Output {
        val trafficIntensity = getTrafficIntensity(input.requisition, input.processing, input.servers)
        var noRequisitions: Double = (input.servers * trafficIntensity).pow(input.servers.toDouble()) / (factorial(input.servers) * (1 - trafficIntensity))

        for (n in 0 until input.servers) {
            noRequisitions += (input.servers * trafficIntensity).pow(n.toDouble()) / factorial(n)
        }
        noRequisitions = 1 / noRequisitions
        var cErlang: Double = (input.servers * trafficIntensity).pow(input.servers.toDouble()) / (factorial(input.servers) * (1 - trafficIntensity))
        cErlang *= noRequisitions
        val nRequisitions = if (input.users <= input.servers) {
            ((input.servers * trafficIntensity).pow(factorial(input.users).toDouble()) / factorial(input.users)) * noRequisitions
        } else {
            ((trafficIntensity.pow(input.users.toDouble()) * input.servers.toDouble().pow(input.servers.toDouble()))
                    / factorial(input.servers)) * noRequisitions
        }
        val waitingRequests = trafficIntensity * cErlang / (1 - trafficIntensity)
        val serviceRequests = input.servers * trafficIntensity
        val numberOfRequests = input.servers * trafficIntensity + waitingRequests
        var avgTime = cErlang / (input.servers * (1 - trafficIntensity))
        avgTime = (1 + avgTime) * (1.0 / input.processing)
        val avgWaitingTime = cErlang / (input.servers * input.processing * (1 - trafficIntensity))

        var output = Output(
            trafficIntensity = round(trafficIntensity * 100),
            noUserProbability = round(noRequisitions * 100),
            probabilityOFNUser = round(nRequisitions * 100),
            cerlang = cErlang,
            averageOfWaitingRequests = round(waitingRequests * 100),
            serviceRequest = round(serviceRequests * 100),
            averageRequisition = round(numberOfRequests * 100),
            utilization = round(trafficIntensity * 100),
            averageResponseTime = round(avgTime * 100),
            averageTimeOfQueueWaiting = round(avgWaitingTime * 100)
        )
       val user= userRepository.findById(input.user)
       if(user.isEmpty)
           throw Exception("User not found")
       output = outputRepository.save(output)
       val input = inputRepository.save(inputMapper.toEntity(input))

       searchRepository.save(Search(user.get(), input, output ))

       return output
    }

    fun getInfiniteServersQueue(
        input: InputDto
    ): Output {
        val trafficIntensity = getTrafficIntensity(input.requisition, input.processing, 1)
        val nRequisitions = (trafficIntensity.pow(input.users.toDouble()) / factorial(input.users)) * (input.noRequisitions)
        val avgUsers = input.requisition / input.processing
        val avgTime = 1 / input.processing
        var output = Output(
            trafficIntensity = round(trafficIntensity * 100),
            noUserProbability = round(nRequisitions * 100),
            averageUsers = round(avgUsers * 100),
            averageResponseTime = round(avgTime * 100)
        )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

    fun getFiniteCapacityServerQueue(
        input: InputDto
    ): Output {
        val trafficIntensity = getTrafficIntensity(input.requisition, input.processing, 1)
        val noRequisitions = (1 - trafficIntensity) / (1 - trafficIntensity.pow(input.rowB + 1.0))
        val nRequisitions = finiteCapacityNRequisitions(
            input.users, input.rowB, noRequisitions, trafficIntensity
        )
        val bRequisitions = finiteCapacityNRequisitions(
            input.rowB, input.rowB, noRequisitions, trafficIntensity
        )
        val utilization = trafficIntensity * bRequisitions
        val receivingRate = input.processing * (1 - bRequisitions)
        val lossRate = input.processing * bRequisitions
        var result = (input.rowB + 1) * trafficIntensity.pow(input.rowB + 1.0)
        val avgUsers = (trafficIntensity / (1 - trafficIntensity)) - (result / (1 - trafficIntensity.pow(input.rowB + 1.0)))

        result = 1 + input.rowB * trafficIntensity.pow(input.rowB)
        val avgUsersQueue = (trafficIntensity / (1 - trafficIntensity)) - trafficIntensity * (result / (1 - trafficIntensity.pow(
            input.rowB + 1
        )))
        val avgResponseTime = avgUsers / (input.processing * (1 - bRequisitions))
        val avgWaitingTime = avgUsersQueue / (input.processing * (1 - bRequisitions))
        var output = Output(
            trafficIntensity = round(trafficIntensity * 100),
            noUserProbability = round(noRequisitions * 100),
            probabilityOFNUser = round(nRequisitions * 100),
            utilization = round(utilization * 100),
            receivingRate = receivingRate,
            lossRate = lossRate,
            averageUsers = round(avgUsers * 100),
            averageUsersQueueWaiting = round(avgUsersQueue * 100),
            averageResponseTime = avgResponseTime,
            averageTimeOfQueueWaiting = avgWaitingTime
        )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

    fun getFiniteCapacityMServersQueue(
        input: InputDto
    ): Output {
        val trafficIntensity = getTrafficIntensity(input.requisition, input.processing, input.servers)
        var result = (1 - trafficIntensity.pow(input.rowB - input.servers + 1.0)) * ((input.servers * trafficIntensity).pow(input.servers))
        result = 1 + result / (factorial(input.servers) * (1 - trafficIntensity))

        for (i in 1 until input.servers) {
            result += (input.servers * trafficIntensity).pow(i) / factorial(i)
        }
        val noRequisitions = 1 / result
        val nRequisitions = finiteCapacityMNRequisitions(input.users, input.servers, noRequisitions, input.requisition, input.processing)
        val bRequisitions = finiteCapacityMNRequisitions(input.rowB, input.servers, noRequisitions, input.requisition, input.processing)
        val utilization = trafficIntensity * (1 - bRequisitions)
        val receivingRate = input.requisition * (1 - bRequisitions)
        val lossRate = input.requisition * bRequisitions
        var avgUsers = 0.0

        for (i in 1..input.rowB) {
            avgUsers += i * finiteCapacityMNRequisitions(i, input.servers, noRequisitions, input.requisition, input.processing)
        }
        var avgUsersQueue = 0.0

        for (i in input.servers + 1..input.rowB) {
            avgUsersQueue += (i - input.servers) * finiteCapacityMNRequisitions(
                i,
                input.servers,
                noRequisitions,
                input.requisition,
                input.processing
            )
        }
        val avgResponseTime = avgUsers / (input.requisition * (1 - bRequisitions))
        val avgWaitingTime = avgUsersQueue / (input.requisition * (1 - bRequisitions))
        var output = Output(
            trafficIntensity = round(trafficIntensity*100),
            probabilityOFNUser = round(nRequisitions*100),
            noUserProbability = round(noRequisitions *100),
            utilization = round(utilization *100),
            receivingRate = receivingRate,
            lossRate = lossRate,
            averageUsers = round(avgUsers *100),
            averageUsersQueueWaiting = round(avgUsersQueue *100),
            averageResponseTime = avgResponseTime,
            averageOfWaitingRequests = avgWaitingTime
        )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

    fun getFinitePopulationServerQueue(
        input: InputDto
    ): Output {
        val trafficIntensity = getTrafficIntensity(input.requisition, input.processing, 1)
        var noRequisitions = 0.0
        for (i in 0..input.population) {
            noRequisitions += ((input.requisition / input.processing).pow(i)) * (factorial(input.population) / factorial(input.population - i))
        }
        noRequisitions = 1 / noRequisitions
        var nRequisitions = 0.0
        if (input.users <= input.population) {
            nRequisitions = (input.requisition / input.processing).pow(input.users) * (factorial(input.population) / factorial(input.population - input.users)) * noRequisitions
        }
        val avgUsersQueue = input.population - ((input.requisition - input.processing) / input.requisition) * (1 - noRequisitions)
        val avgUsers = avgUsersQueue + (1 - noRequisitions)
        val utilization = trafficIntensity * (input.population - avgUsers)
        val avgWaitingTime = avgUsersQueue / (input.requisition * (input.population - avgUsers))
        val avgResponseTime = avgWaitingTime + 1.0 / input.processing
        val receivingRate = input.processing * (1 - noRequisitions)
        var output = Output(
            trafficIntensity = round(trafficIntensity *100),
            probabilityOfUser = round(nRequisitions *100),
            noUserProbability = round(noRequisitions *100),
            utilization = round(utilization *100),
            averageUsers = round(avgUsers *100),
            averageUsersQueueWaiting = round(avgUsersQueue *100),
            averageOfWaitingRequests = avgWaitingTime,
            averageResponseTime = avgResponseTime,
            receivingRate = receivingRate
        )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

    fun getFinitePopulationInfiniteServersQueue(
        input: InputDto
    ): Output {
        var trafficIntensity = (input.population * input.requisition) / (1 + input.requisition / input.processing)
        if (trafficIntensity >= 1.0) {
            trafficIntensity = 1.0
        } else if (trafficIntensity <= 0.0) {
            trafficIntensity = 0.0
        }
        var avgUser = (input.population * (input.requisition /input.processing))
        avgUser /= (1.0 + input.requisition / input.processing)
        var nRequisitions = 0.0
        if (input.users <= input.population) {
            nRequisitions = ((input.requisition / input.processing).pow(input.users.toDouble()) * (input.population / input.users)) / (1.0 + (input.requisition / input.processing)).pow(
                input.population.toDouble()
            )
        }
        var noRequisitions: Double = 1.0 + (input.requisition / input.processing)
        noRequisitions = 1.0 / (noRequisitions.pow(input.population.toDouble()))

        var output = Output(
            trafficIntensity = round(trafficIntensity *100),
            averageUsers = round(avgUser * 100),
            probabilityOFNUser = round(nRequisitions *100),
            noUserProbability = round(noRequisitions *100)
        )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

    fun getFiniteCapacityPopulationServersQueue(
        input: InputDto
    ): Output {
        val trafficIntensity = getTrafficIntensity(input.requisition, input.processing, input.servers)
        val nRequisitions = getFinitePopulationServersQueueNRequisitions(
            input.users,
            input.population,
            input.servers,
            trafficIntensity,
            input.noRequisitions
        )
        val utilization = 1 - input.noRequisitions
        var avgUsers = 0.0
        for (i in 0..input.servers) {
            avgUsers += i * getFinitePopulationServersQueueNRequisitions(
                i,
                input.population,
                input.servers,
                trafficIntensity,
                input.noRequisitions
            )
        }
        val receivingRate = input.requisition * (input.population - avgUsers)
        val avgUsersQueue = avgUsers / receivingRate
        val avgResponseTime = avgUsers / input.requisition * (input.population - avgUsers - (input.population - input.rowB) * getFinitePopulationServersQueueNRequisitions(
            input.rowB,
            input.population,
            input.servers,
            trafficIntensity,
            input.noRequisitions
        ))
        var output =  Output(
            trafficIntensity = round(trafficIntensity *100),
            probabilityOfUser = round(nRequisitions *100),
            noUserProbability = round(input.noRequisitions *100),
            utilization = round(utilization *100),
            averageUsers = round(avgUsers *100),
            receivingRate = receivingRate,
            averageUsersQueueWaiting = round(avgUsersQueue *100),
            averageResponseTime = avgResponseTime
        )
        val user= userRepository.findById(input.user)
        output = outputRepository.save(output)
        val input = inputRepository.save(inputMapper.toEntity(input))
        if(user.isEmpty)
            throw Exception("User not found")

        searchRepository.save(Search(user.get(), input, output ))

        return output
    }

    private fun getFinitePopulationServersQueueNRequisitions(
        n: Int, population: Int, servers: Int,
        trafficIntensity: Double, noRequisitions: Double
    ): Double {
        try {
            return if (population <= servers - 1) {
                (population / n) * trafficIntensity * noRequisitions
            } else {
                val result = factorial(n) / (factorial(servers) * servers.toDouble().pow(n - servers.toDouble()))
                return result * (population / n) * trafficIntensity * noRequisitions
            }
        } catch (e: Exception) {
            return 0.0
        }
    }

    fun getTrafficIntensity(requisition: Double, processing: Double, servers: Int): Double {
        var trafficIntensity: Double = requisition.div(processing * servers)

        if (trafficIntensity < 0.0) {
            trafficIntensity = 0.0
        } else if (trafficIntensity > 1.0) {
            trafficIntensity = 1.0
        }
        return trafficIntensity
    }

    fun finiteCapacityNRequisitions(n: Int, rowB: Int, noRequisitions: Double, trafficIntensity: Double): Double {
        return if (n <= rowB) {
            noRequisitions * trafficIntensity.pow(n)
        } else
            0.0

    }

    fun finiteCapacityMNRequisitions(
        n: Int,
        servers: Int,
        noRequisitions: Double,
        requisition: Double,
        processing: Double
    ): Double {
        return if (n < servers) {
            (requisition / processing).pow(n) * (1.0 / factorial(n) * noRequisitions)

        } else {
            (servers.toDouble()
                .pow(servers.toDouble()) / factorial(servers)) * (requisition / processing).pow(n) * noRequisitions
        }
    }

    fun factorial(n: Int): Int {
        var result = 1
        for (i in n downTo 1) {
            result *= i
        }
        return result
    }
}