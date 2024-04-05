package tech.jaya.wec.dto.driver

import tech.jaya.wec.model.Driver

data class DriverRequest(
    val name: String,
    val available: Boolean,
    val carId: Long?
) {

    constructor(driver: Driver) : this(
        name = driver.name,
        available = driver.available,
        carId = driver.car?.id
    )

    fun toEntity(): Driver {
        return Driver(
            name = this.name,
            available = this.available,
            car = null
        )
    }
}
