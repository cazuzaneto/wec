package tech.jaya.wec.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import tech.jaya.wec.testutils.TestEntityGenerator

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DriverDaoTest {

    @Autowired
    private lateinit var driverDao: DriverDao

    private final var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `findAll should return a list of drivers`() {
        val totalDrivers = 10
        (1..totalDrivers).forEach { _ ->
            driverDao.saveDriver(generator.generateDriver())
        }
        val result = driverDao.findAllDrivers()
        assertEquals(totalDrivers, result.size)
    }

    @Test
    fun `findAll should return an empty list when there are no drivers`() {
        val result = driverDao.findAllDrivers()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findById should return a driver when found`() {
        val driver = driverDao.saveDriver(generator.generateDriver())
        val result = driverDao.findDriverById(driver.id!!)
        assertEquals(driver.id, result!!.id)
        assertEquals(driver.name, result.name)
    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = driverDao.findDriverById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update driver when id is not null`() {
        val savedDriver = driverDao.saveDriver(generator.generateDriver())
        val updatedResult = driverDao.saveDriver(savedDriver.copy(name = "New Name"))
        val expectedDriver = driverDao.findDriverById(savedDriver.id!!)
        assertEquals(expectedDriver!!.id, updatedResult.id)
        assertEquals(expectedDriver.name, updatedResult.name)
    }

    @Test
    fun `save should insert new driver when id is null`() {
        val totalItems = driverDao.findAllDrivers().size
        driverDao.saveDriver(generator.generateDriver().copy())
        assertEquals(driverDao.findAllDrivers().size, totalItems + 1)
    }

    @Test
    fun `deleteById should delete driver`() {
        val savedDriver = driverDao.saveDriver(generator.generateDriver())
        val totalItems = driverDao.findAllDrivers().size
        driverDao.deleteDriverById(savedDriver.id!!)
        assertEquals(driverDao.findAllDrivers().size, totalItems - 1)
    }
}
