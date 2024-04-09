package tech.jaya.wec.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This data class represents a Car entity.
 *
 * @property id the unique identifier of the car. It's nullable to allow the creation of a car without an id.
 * @property licensePlate the license plate of the car.
 * @property model the model of the car.
 * @property color the color of the car.
 */
data class Car(
    @set:JsonIgnore
    @get:JsonProperty("id")
    var id: Long? = null,
    val licensePlate: String,
    val model: String,
    val color: String
)