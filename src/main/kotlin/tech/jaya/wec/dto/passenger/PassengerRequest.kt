package tech.jaya.wec.dto.passenger

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Passenger

data class PassengerRequest @JsonCreator constructor(
    @JsonProperty("name") val name: String,
) {

    constructor(passenger: Passenger) : this(name = passenger.name)

    fun toEntity(): Passenger {
        return Passenger(name = this.name)
    }
}
