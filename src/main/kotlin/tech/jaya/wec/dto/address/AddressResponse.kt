package tech.jaya.wec.dto.address

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Address

data class AddressResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("text") val text: String,
)

fun Address.toResponse(): AddressResponse {
    val id = this.id ?: throw IllegalArgumentException("Address must have an id")
    return AddressResponse(id = id, text = this.text)
}
