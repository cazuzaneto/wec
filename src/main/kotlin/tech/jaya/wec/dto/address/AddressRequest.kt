package tech.jaya.wec.dto.address

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.wec.model.Address

data class AddressRequest @JsonCreator constructor(
    @JsonProperty("text") val text: String,
) {

    constructor(address: Address) : this(text = address.text)

    fun toEntity(): Address {
        return Address(text = this.text)
    }
}
