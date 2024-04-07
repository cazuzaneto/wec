package tech.jaya.wec.dto.driver

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.dto.car.CarResponse
import tech.jaya.wec.dto.car.toResponse
import tech.jaya.wec.model.Driver

data class DriverResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("available") val available: Boolean,
    @JsonProperty("car") val car: CarResponse?
) {
    constructor(driver: Driver) : this(
        id = driver.id ?: throw IllegalArgumentException("Driver id cannot be null"),
        name = driver.name,
        available = driver.available,
        car = driver.car?.toResponse()
    )
}

fun Driver.toResponse(): DriverResponse {
    this.id ?: throw IllegalArgumentException("Driver must have an id")
    return DriverResponse(this)
}
