package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.dto.passenger.PassengerRequest
import tech.jaya.wec.dto.passenger.PassengerResponse
import tech.jaya.wec.dto.passenger.toResponse
import tech.jaya.wec.service.PassengerService

@RestController
@RequestMapping("/passengers")
class PassengerController(private val passengerService: PassengerService) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<PassengerResponse> {
        return passengerService.findById(id)?.toResponse()?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound()
            .build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<PassengerResponse>> {
        val passengers = passengerService.findAll().map { it.toResponse() }
        return ResponseEntity.ok(passengers)
    }

    @PostMapping
    fun save(@RequestBody passengerRequest: PassengerRequest): ResponseEntity<PassengerResponse> {
        val passenger = passengerService.save(passengerRequest.toEntity())
        return ResponseEntity.ok(passenger.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        passengerService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
