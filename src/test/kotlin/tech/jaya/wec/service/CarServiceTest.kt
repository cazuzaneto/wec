package tech.jaya.wec.service

import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import tech.jaya.wec.repository.CarDao
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
class CarServiceTest {

    @InjectMockKs
    private lateinit var carService: CarService

    @MockK
    private lateinit var carDao: CarDao

    private var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `should return all cars`() {
        val cars = generator.generateCars(2)
        every { carDao.findAll() } returns cars

        val result = carService.findAll()

        assertNotNull(result)
        assertEquals(2, result.size)
    }

    @Test
    fun `should return car by id`() {
        val car = generator.generateCar()
        every { carDao.findById(1L) } returns car

        val result = carService.findById(1L)

        assertNotNull(result)
        assertEquals(car, result)
    }

    @Test
    fun `should save car`() {
        val car = generator.generateCar()
        every { carDao.save(car) } returns car

        val result = carService.save(car)

        assertNotNull(result)
        assertEquals(car, result)
    }

    @Test
    fun `should delete car by id`() {
        val id = 1L

        every { carDao.deleteById(id) } just Runs

        carService.deleteById(id)

        verify { carDao.deleteById(id) }
    }
}
