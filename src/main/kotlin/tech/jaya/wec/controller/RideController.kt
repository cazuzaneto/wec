package tech.jaya.wec.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.model.Address
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.model.Ride
import tech.jaya.wec.model.Status
import tech.jaya.wec.service.RideWorkFlow

@RestController
class RideController(private val rideWorkFlow: RideWorkFlow) {

    @PostMapping("/request-driver")
    fun requestDriver(@RequestBody request: HashMap<String, String>): Ride {
        return request.run {
            rideWorkFlow.start(newRide(this))
        }
    }

    private fun newRide(request: HashMap<String, String>): Ride {
        // TODO substituir para o objeto ride
        // TODO Criar a serialização e deserialização parte

        val name = request["name"] ?: throw Exception("No name found")
        val email = request["email"] ?: throw Exception("No email found")
        val pickup = request["pickup"] ?: throw Exception("No pickup found")
        val dropOff = request["dropoff"] ?: throw Exception("No dropoff found")
        return Ride(
            pickup = Address(text = pickup),
            dropOff = Address(text = dropOff),
            passenger = Passenger(name = name, email = email),
            status = Status.REQUESTED,
        )
    }
}