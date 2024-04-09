package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.service.PassengerService

@RestController
@RequestMapping("/passengers")
class PassengerController(private val passengerService: PassengerService) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Passenger> {
        return passengerService.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound()
            .build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<Passenger>> {
        return passengerService.findAll().let {
            ResponseEntity.ok(it)
        }
    }

    @PostMapping
    fun save(@RequestBody passengerRequest: Passenger): ResponseEntity<Passenger> {
        return passengerService.save(passengerRequest).let {
            ResponseEntity.ok(it)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        passengerService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
