package tech.jaya.wec.dto.driver

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Driver

data class DriverResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("available") val available: Boolean,
    @JsonProperty("carId") val carId: Long?
) {
    constructor(driver: Driver) : this(
        id = driver.id ?: throw IllegalArgumentException("Driver id cannot be null"),
        name = driver.name,
        available = driver.available,
        carId = driver.car?.id
    )
}

fun Driver.toResponse(): DriverResponse {
    this.id ?: throw IllegalArgumentException("Driver must have an id")
    return DriverResponse(this)
}
