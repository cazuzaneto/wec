package tech.jaya.wec.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.wec.dto.address.AddressRequest
import tech.jaya.wec.dto.address.AddressResponse
import tech.jaya.wec.dto.address.toResponse
import tech.jaya.wec.model.Address
import tech.jaya.wec.service.AddressService

@RestController
@RequestMapping("/addresses")
class AddressController(private val addressService: AddressService) {

    @GetMapping
    fun getAllAddresses(): ResponseEntity<List<AddressResponse>> {
        val addresses = addressService.findAll()
        return ResponseEntity.ok(addresses.map(Address::toResponse))
    }

    @GetMapping("/{id}")
    fun getAddressById(@PathVariable id: Long): ResponseEntity<AddressResponse> {
        return addressService.findById(id)?.run {
            ResponseEntity.ok(this.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createAddress(@RequestBody request: AddressRequest): ResponseEntity<AddressResponse> {
        return request.toEntity().let { addressService.save(it) }
            .toResponse().let { ResponseEntity.ok(it) }
    }

    @DeleteMapping("/{id}")
    fun deleteAddress(@PathVariable id: Long): ResponseEntity<Unit> {
        addressService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
