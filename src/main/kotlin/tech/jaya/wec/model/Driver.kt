package tech.jaya.wec.model

/**
 * This data class represents a Driver entity.
 *
 * @property id the unique identifier of the driver. It's nullable to allow the creation of a driver without an id.
 * @property name the name of the driver.
 * @property available a boolean indicating whether the driver is available or not.
 * @property car the car associated with the driver. It's nullable because a driver might not have a car associated.
 */
data class Driver(
    val id: Long? = null,
    val name: String,
    var available: Boolean,
    val car: Car? = null
)
