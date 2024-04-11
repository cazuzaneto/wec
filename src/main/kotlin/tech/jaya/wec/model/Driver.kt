package tech.jaya.wec.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

/**
 * This data class represents a Driver entity.
 *
 * @property id the unique identifier of the driver. It's nullable to allow the creation of a driver without an id.
 * @property name the name of the driver.
 * @property available a boolean indicating whether the driver is available or not.
 * @property car the car associated with the driver. It's nullable because a driver might not have a car associated.
 */
data class Driver(
    @set:JsonIgnore
    @get:JsonProperty("id")
    var id: Long? = null,
    val name: String,
    val available: Boolean,
    @set:JsonIgnore
    var activationDate: LocalDateTime?,
    var car: Car? = null
)