package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.dto.driver.DriverRequest
import tech.jaya.wec.dto.driver.DriverResponse
import tech.jaya.wec.dto.driver.toResponse
import tech.jaya.wec.service.DriverService

@RestController
@RequestMapping("/drivers")
class DriverController(private val driverService: DriverService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<DriverResponse>> {
        val drivers = driverService.findAll().map { it.toResponse() }
        return ResponseEntity.ok(drivers)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<DriverResponse> {
        val driver = driverService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(DriverResponse(driver))
    }

    @PostMapping
    fun save(@RequestBody driverRequest: DriverRequest): ResponseEntity<DriverResponse> {
        val driver = driverService.save(driverRequest.toEntity())
        return ResponseEntity.ok(driver.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Unit> {
        driverService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
