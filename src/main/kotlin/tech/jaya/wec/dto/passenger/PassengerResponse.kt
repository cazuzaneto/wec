package tech.jaya.wec.dto.passenger

import tech.jaya.wec.model.Passenger

data class PassengerResponse(
    val id: Long,
    val name: String
)

fun Passenger.toResponse(): PassengerResponse {
    val id = this.id ?: throw IllegalArgumentException("Passenger must have an id")
    return PassengerResponse(id = id, name = this.name)
}
