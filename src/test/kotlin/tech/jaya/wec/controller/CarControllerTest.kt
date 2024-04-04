package tech.jaya.wec.controller

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tech.jaya.wec.service.CarService
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
@WebMvcTest(CarController::class)
class CarControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var carService: CarService

    private var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `should return all cars`() {
        val cars = generator.generateCars(2)
        every { carService.findAll() } returns cars

        mockMvc.perform(
            get("/cars")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return car by id`() {
        val car = generator.generateCar()
        every { carService.findById(1L) } returns car

        mockMvc.perform(
            get("/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return not found when car does not exist`() {
        every { carService.findById(1L) } returns null

        mockMvc.perform(
            get("/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should create a new car`() {
        val car = generator.generateCar()
        every { carService.save(any()) } returns car

        mockMvc.perform(
            post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                    "licensePlate": "${car.licensePlate}",
                    "model": "${car.model}",
                    "color": "${car.color}"
                }"""
                )
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should delete a car`() {
        every { carService.deleteById(1L) } just runs

        mockMvc.perform(
            delete("/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
    }
}
