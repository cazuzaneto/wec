package tech.jaya.wec.dto.car

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Car

data class CarRequest(
    @JsonProperty("licensePlate") val licensePlate: String,
    @JsonProperty("model") val model: String,
    @JsonProperty("color") val color: String
) {

    constructor(car: Car) : this(licensePlate = car.licensePlate, model = car.model, color = car.color)

    fun toEntity(): Car {
        return Car(
            licensePlate = this.licensePlate,
            model = this.model,
            color = this.color
        )
    }
}
