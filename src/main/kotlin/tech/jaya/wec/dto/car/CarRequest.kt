package tech.jaya.wec.dto.car

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Car

data class CarRequest(
    @JsonProperty("licensePlate") val licensePlate: String,
    @JsonProperty("model") val model: String,
    @JsonProperty("color") val color: String
) {

    fun toEntity(): Car {
        return Car(
            licensePlate = this.licensePlate,
            model = this.model,
            color = this.color
        )
    }
}
