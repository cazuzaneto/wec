package tech.jaya.wec.dto.driver

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Driver

data class DriverRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("available") val available: Boolean,
    @JsonProperty("carId") val carId: Long?
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
