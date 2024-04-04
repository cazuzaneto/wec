package tech.jaya.wec.model

/**
 * This data class represents an Address entity.
 *
 * @property id the unique identifier of the address. It's nullable to allow the creation of an address without an id.
 * @property text the text of the address.
 */
data class Address(
    val id: Long? = null,
    val text: String,
)
