package tech.jaya.wec.dto.driver

import tech.jaya.wec.model.Driver

data class DriverResponse(
    val id: Long,
    val name: String,
    val available: Boolean,
    val carId: Long?
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
