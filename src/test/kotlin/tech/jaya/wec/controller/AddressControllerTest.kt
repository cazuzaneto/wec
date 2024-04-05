package tech.jaya.wec.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tech.jaya.wec.dto.address.AddressRequest
import tech.jaya.wec.service.AddressService
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
@WebMvcTest(AddressController::class)
class AddressControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var addressService: AddressService

    private var generator: TestEntityGenerator = TestEntityGenerator()

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `should return all addresses`() {
        val sizeResponse = 10
        val addresses = List(sizeResponse) { generator.generateAddressWithId() }
        every { addressService.findAll() } returns addresses

        mockMvc.perform(
            get("/addresses")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(sizeResponse)))
            .andExpect(jsonPath("$[0].id").value(addresses[0].id))
            .andExpect(jsonPath("$[0].text").value(addresses[0].text))
    }

    @Test
    fun `should return address by id`() {
        val address = generator.generateAddressWithId()
        val id = 1L
        every { addressService.findById(id) } returns address

        mockMvc.perform(
            get("/addresses/$id")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(address.id))
            .andExpect(jsonPath("$.text").value(address.text))
    }

    @Test
    fun `should return not found when address does not exist`() {
        val id = 1L
        every { addressService.findById(id) } returns null

        mockMvc.perform(
            get("/addresses/$id")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should create a new address`() {
        val address = generator.generateAddressWithId()
        val request = AddressRequest(address)
        every { addressService.save(any()) } returns address

        mockMvc.perform(
            post("/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(address.id))
            .andExpect(jsonPath("$.text").value(address.text))
    }

    @Test
    fun `should delete an address`() {
        val id = 1L
        every { addressService.deleteById(id) } just runs

        mockMvc.perform(
            delete("/addresses/$id")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
    }
}
