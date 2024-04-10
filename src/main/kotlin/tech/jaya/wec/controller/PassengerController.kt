package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.dao.PassengerDao
import tech.jaya.wec.model.Passenger

@RestController
@RequestMapping("/passengers")
class PassengerController(private val passengerDao: PassengerDao) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Passenger> {
        return passengerDao.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound()
            .build()
    }

    @PostMapping
    fun save(@RequestBody passengerRequest: Passenger): ResponseEntity<Passenger> {
        return passengerDao.save(passengerRequest).let {
            ResponseEntity.ok(it)
        }
    }
}
