package tech.jaya.wec.model

/**
 * An enum class representing the status of a Ride in the system.
 *
 * @property REQUESTED The ride has been requested but not yet accepted by a driver.
 * @property IN_PROGRESS The ride has been accepted by a driver and is currently in progress.
 * @property COMPLETED The ride has been completed.
 * @property CANCELLED The ride has been cancelled.
 */
enum class Status {
    REQUESTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}