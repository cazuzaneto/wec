package tech.jaya.wec.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import tech.jaya.wec.dao.DriverDao
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
class DriverServiceTest {

    @InjectMockKs
    private lateinit var driverService: DriverService

    @MockK
    private lateinit var driverDao: DriverDao

    @MockK
    private lateinit var carService: CarService

    private var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `should return all drivers`() {
        val drivers = List(10) { generator.generateDriverWithId() }
        every { driverDao.findAll() } returns drivers

        val result = driverService.findAll()

        assertNotNull(result)
        assertEquals(drivers, result)
        verify { driverDao.findAll() }
    }

    @Test
    fun `should return a driver by id`() {
        val driver = generator.generateDriverWithId()
        val id = requireNotNull(driver.id) { "Driver id is null" }
        every { driverDao.findById(id) } returns driver
        val result = driverService.findById(id)

        assertNotNull(result)
        assertEquals(driver, result)
        verify { driverDao.findById(id) }
    }

    @Test
    fun `should save a driver`() {
        val driver = generator.generateDriverWithId()
        val carId = requireNotNull(driver.car?.id) { "Car id is null" }
        every { carService.findById(carId) } returns driver.car
        every { driverDao.save(driver) } returns driver

        val result = driverService.save(driver)

        assertNotNull(result)
        assertEquals(driver, result)
        verify { driverDao.save(driver) }
    }

    @Test
    fun `should delete a driver by id`() {
        val id = 1L
        every { driverDao.deleteById(id) } returns Unit

        driverService.deleteById(id)

        verify { driverDao.deleteById(id) }
    }
}
