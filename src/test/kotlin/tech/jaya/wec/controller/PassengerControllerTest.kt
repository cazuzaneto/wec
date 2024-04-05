package tech.jaya.wec.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
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
import tech.jaya.wec.dto.passenger.PassengerRequest
import tech.jaya.wec.service.PassengerService
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
@WebMvcTest(PassengerController::class)
class PassengerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var passengerService: PassengerService
    private val generator: TestEntityGenerator = TestEntityGenerator()
    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `should return passenger by id`() {
        val id = 1L
        val passenger = generator.generatePassengerWithId()
        every { passengerService.findById(id) } returns passenger

        mockMvc.perform(get("/passengers/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(passenger.id))
            .andExpect(jsonPath("$.name").value(passenger.name))
    }

    @Test
    fun `should return all passengers`() {
        val passengers = listOf(
            generator.generatePassengerWithId(),
            generator.generatePassengerWithId()
        )
        every { passengerService.findAll() } returns passengers

        mockMvc.perform(get("/passengers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].id").value(passengers[0].id))
            .andExpect(jsonPath("$.[0].name").value(passengers[0].name))
            .andExpect(jsonPath("$.[1].id").value(passengers[1].id))
            .andExpect(jsonPath("$.[1].name").value(passengers[1].name))
    }

    @Test
    fun `should save passenger`() {
        val passenger = generator.generatePassengerWithId()
        val request = PassengerRequest(passenger)
        every { passengerService.save(request.toEntity()) } returns passenger

        mockMvc.perform(
            post("/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(request)
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(passenger.id))
            .andExpect(jsonPath("$.name").value(passenger.name))
    }

    @Test
    fun `should delete passenger by id`() {
        val id = 1L
        every { passengerService.deleteById(id) } just runs

        mockMvc.perform(delete("/passengers/$id"))
            .andExpect(status().isNoContent)
    }
}
