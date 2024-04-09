package tech.jaya.wec.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class representing a Passenger in the system.
 *
 * @property id The unique identifier of the passenger. It's nullable because it can be absent when a Passenger object is created before being saved to the database.
 * @property name The name of the passenger. It's a non-nullable String.
 */
data class Passenger(

    @set:JsonIgnore
    @get:JsonProperty("id")
    var id: Long? = null,
    val name: String
)
