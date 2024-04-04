package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.model.Car
import tech.jaya.wec.service.CarService

@RestController
@RequestMapping("/cars")
class CarController(private val carService: CarService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<Car>> {
        val cars = carService.findAll()
        return ResponseEntity.ok(cars)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Car> {
        val car = carService.findById(id)
        return if (car != null) ResponseEntity.ok(car) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody car: Car): ResponseEntity<Car> {
        val savedCar = carService.save(car)
        return ResponseEntity.ok(savedCar)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        carService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
