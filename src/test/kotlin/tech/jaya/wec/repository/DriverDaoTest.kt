package tech.jaya.wec.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import tech.jaya.wec.model.Car
import tech.jaya.wec.model.Driver

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DriverDaoTest {

    @Autowired
    @Qualifier("driverDao")
    private lateinit var driverDao: Dao<Driver>

    @Autowired
    @Qualifier("carDao")
    private lateinit var carDao: Dao<Car>
    private final var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `findAll should return a list of drivers`() {
        val totalDrivers = 10
        (1..totalDrivers).forEach { _ -> newPersistedDriver() }
        val result = driverDao.findAll()
        assertEquals(totalDrivers, result.size)
    }

    @Test
    fun `findAll should return an empty list when there are no drivers`() {
        val result = driverDao.findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findById should return a driver when found`() {
        val driver = newPersistedDriver()
        val result = driverDao.findById(driver.id!!)
        assertEquals(driver, result)
    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = driverDao.findById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update driver when id is not null`() {
        val savedDriver = newPersistedDriver()
        val updatedResult = driverDao.save(savedDriver.copy(name = "New Name"))
        val expectedDriver = driverDao.findById(savedDriver.id!!)
        assertEquals(expectedDriver, updatedResult)
    }

    @Test
    fun `save should insert new driver when id is null`() {
        val totalItems = driverDao.findAll().size
        newPersistedDriver()
        assertEquals(driverDao.findAll().size, totalItems + 1)
    }

    @Test
    fun `deleteById should delete driver`() {
        val savedDriver = newPersistedDriver()
        val totalItems = driverDao.findAll().size
        driverDao.deleteById(savedDriver.id!!)
        assertEquals(driverDao.findAll().size, totalItems - 1)
    }

    private fun newPersistedDriver(): Driver {
        val carSaved = carDao.save(generator.generateCar())
        val savedDriver = driverDao.save(generator.generateDriver().copy(car = carSaved))
        return savedDriver
    }
}
