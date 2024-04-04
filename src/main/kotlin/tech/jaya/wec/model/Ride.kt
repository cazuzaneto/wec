package tech.jaya.wec.model

/**
 * A data class representing a Ride in the system.
 *
 * @property id The unique identifier of the ride. It's nullable because it can be absent when a Ride object is created before being saved to the database.
 * @property pickup The pickup address for the ride. It's a non-nullable Address object.
 * @property dropOff The drop-off address for the ride. It's a non-nullable Address object.
 * @property status The status of the ride. It's a non-nullable Status object.
 */
data class Ride(
    val id: Long? = null,
    val pickup: Address,
    val dropOff: Address,
    var status: Status,
    var driver: Driver? = null,
    var passenger: Passenger
)