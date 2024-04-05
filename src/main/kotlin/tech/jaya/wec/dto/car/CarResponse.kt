package tech.jaya.wec.dto.car

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Car

data class CarResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("licensePlate") val licensePlate: String,
    @JsonProperty("model") val model: String,
    @JsonProperty("color") val color: String
)

fun Car.toResponse(): CarResponse {
    val id = this.id ?: throw IllegalArgumentException("Car must have an id")
    return CarResponse(id = id, licensePlate = this.licensePlate, model = this.model, color = this.color)
}
