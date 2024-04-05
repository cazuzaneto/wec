package tech.jaya.wec.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
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
import tech.jaya.wec.dto.driver.DriverRequest
import tech.jaya.wec.service.CarService
import tech.jaya.wec.service.DriverService
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
@WebMvcTest(DriverController::class)
class DriverControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var carService: CarService

    @MockkBean
    private lateinit var driverService: DriverService

    private var generator: TestEntityGenerator = TestEntityGenerator()
    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `should return all drivers`() {
        val sizeResponse = 3
        val drivers = List(sizeResponse) { generator.generateDriverWithId() }
        every { driverService.findAll() } returns drivers

        mockMvc.perform(get("/drivers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(sizeResponse)))
            .andExpect(jsonPath("$[0].id").value(drivers[0].id))
            .andExpect(jsonPath("$[0].name").value(drivers[0].name))
            .andExpect(jsonPath("$[0].available").value(drivers[0].available))
            .andExpect(jsonPath("$[0].carId").value(drivers[0].car?.id))

        verify { driverService.findAll() }
    }

    @Test
    fun `should return a driver by id`() {
        val driver = generator.generateDriverWithId()
        val id = requireNotNull(driver.id) { "Driver id is null" }
        every { driverService.findById(id) } returns driver

        mockMvc.perform(get("/drivers/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(driver.id))
            .andExpect(jsonPath("$.name").value(driver.name))
            .andExpect(jsonPath("$.available").value(driver.available))
            .andExpect(jsonPath("$.carId").value(driver.car?.id))

        verify { driverService.findById(id) }
    }

    @Test
    fun `should save a new driver`() {
        val driver = generator.generateDriverWithId()
        val request = DriverRequest(driver)
        every { driverService.save(request.toEntity(), request.carId) } returns driver

        mockMvc.perform(
            post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(driver.id))
            .andExpect(jsonPath("$.name").value(driver.name))
            .andExpect(jsonPath("$.available").value(driver.available))
            .andExpect(jsonPath("$.carId").value(driver.car?.id))

        verify { driverService.save(request.toEntity(), request.carId) }
    }

    @Test
    fun `should delete a driver by id`() {
        val id = 1L
        every { driverService.deleteById(id) } returns Unit

        mockMvc.perform(delete("/drivers/$id"))
            .andExpect(status().isNoContent)

        verify { driverService.deleteById(id) }
    }
}
