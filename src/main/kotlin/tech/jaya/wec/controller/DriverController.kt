package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.dao.DriverDao
import tech.jaya.wec.model.Driver

@RestController
@RequestMapping("/drivers")
class DriverController(private val driverDao: DriverDao) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Driver> {
        return driverDao.findDriverById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody driverRequest: Driver): ResponseEntity<Driver> {
        return driverDao.saveDriver(driverRequest).let {
            ResponseEntity.ok(it)
        }
    }
}
