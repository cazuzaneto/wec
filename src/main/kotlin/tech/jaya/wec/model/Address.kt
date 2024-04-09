package tech.jaya.wec.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This data class represents an Address entity.
 *
 * @property id the unique identifier of the address. It's nullable to allow the creation of an address without an id.
 * @property text the text of the address.
 */
data class Address(
    @set:JsonIgnore
    @get:JsonProperty("id")
    var id: Long? = null,
    val text: String,
)
