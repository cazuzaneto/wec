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
import tech.jaya.wec.model.Ride
import tech.jaya.wec.model.Status

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RideDaoTest {

    @Autowired
    @Qualifier("rideDao")
    private lateinit var rideDao: Dao<Ride>

    @Autowired
    private lateinit var addressDao: AddressDao
    private final var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `findAll should return a list of rides`() {
        val totalRides = 10
        (1..totalRides).forEach { _ -> newPersistedRide() }
        val result = rideDao.findAll()
        assertEquals(totalRides, result.size)
    }

    @Test
    fun `findAll should return an empty list when there are no rides`() {
        val result = rideDao.findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findById should return a rides when found`() {
        val driver = newPersistedRide()
        val result = rideDao.findById(driver.id!!)
        assertEquals(driver, result)

    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = rideDao.findById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update driver when id is not null`() {
        val savedDriver = newPersistedRide()
        val updatedResult = rideDao.save(savedDriver.copy(status = Status.CANCELLED))
        val expectedDriver = rideDao.findById(savedDriver.id!!)
        assertEquals(expectedDriver, updatedResult)
    }

    @Test
    fun `save should insert new ride when id is null`() {
        val totalItems = rideDao.findAll().size
        newPersistedRide()
        assertEquals(rideDao.findAll().size, totalItems + 1)
    }

    @Test
    fun `deleteById should delete ride`() {
        val savedDriver = newPersistedRide()
        val totalItems = rideDao.findAll().size
        rideDao.deleteById(savedDriver.id!!)
        assertEquals(rideDao.findAll().size, totalItems - 1)
    }

    private fun newPersistedRide(): Ride {
        val addressPickUp = addressDao.save(generator.generateAddress())
        val addressDropOff = addressDao.save(generator.generateAddress())
        val savedRide = rideDao.save(generator.generateRide().copy(pickup = addressPickUp, dropOff = addressDropOff))
        return savedRide
    }
}
