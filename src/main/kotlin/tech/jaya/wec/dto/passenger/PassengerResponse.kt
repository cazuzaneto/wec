package tech.jaya.wec.dto.passenger

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Passenger

data class PassengerResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String
)

fun Passenger.toResponse(): PassengerResponse {
    val id = this.id ?: throw IllegalArgumentException("Passenger must have an id")
    return PassengerResponse(id = id, name = this.name)
}
