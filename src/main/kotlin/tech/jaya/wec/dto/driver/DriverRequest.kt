package tech.jaya.wec.dto.driver

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.dto.car.CarRequest
import tech.jaya.wec.model.Driver

data class DriverRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("available") val available: Boolean,
    @JsonProperty("car") val car: CarRequest
) {

    fun toEntity(): Driver {
        return Driver(
            name = this.name,
            available = this.available,
            car = this.car.toEntity()
        )
    }
}
