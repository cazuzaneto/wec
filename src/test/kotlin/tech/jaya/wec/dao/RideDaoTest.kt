package tech.jaya.wec.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.model.Ride
import tech.jaya.wec.model.Status
import tech.jaya.wec.testutils.TestEntityGenerator

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RideDaoTest {

    @Autowired
    private lateinit var rideDao: RideDao

    @Autowired
    private lateinit var driverDao: DriverDao

    @Autowired
    private lateinit var passengerDao: PassengerDao

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
        val ride = newPersistedRide()
        val result = rideDao.findById(ride.id!!)
        assertEquals(ride, result)
    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = rideDao.findById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update ride when id is not null and change passenger`() {
        val savedRide = newPersistedRide()
        val newPassenger = passengerDao.save(generator.generatePassenger())
        val updatedResult =
            rideDao.save(savedRide.copy(status = Status.CANCELLED, passenger = newPassenger))
        val expectedRide = rideDao.findById(savedRide.id!!)
        assertEquals(expectedRide, updatedResult)
    }

    @Test
    fun `save should update ride when id is not null and change driver`() {
        val savedRide = newPersistedRide()
        val driver = driverDao.saveDriver(generator.generateDriver())
        val updatedRide =
            rideDao.save(savedRide.copy(status = Status.CANCELLED, driver = driver))
        val expectedRide = rideDao.findById(savedRide.id!!)
        assertEquals(expectedRide!!.id, updatedRide.id)
        assertEquals(expectedRide.driver!!.id, updatedRide.id)
        assertEquals(Status.CANCELLED, updatedRide.status)
    }

    @Test
    fun `save should insert new ride when id is null`() {
        val totalItems = rideDao.findAll().size
        newPersistedRide()
        assertEquals(rideDao.findAll().size, totalItems + 1)
    }

    @Test
    fun `not save should throw IllegalArgumentException when passenger id is null`() {
        val ride =
            generator.generateRide().copy(passenger = Passenger(id = null, name = "Some name", email = "test@test.com"))
        assertThrows<IllegalArgumentException> {
            rideDao.save(ride)
        }
    }

    @Test
    fun `deleteById should delete ride`() {
        val savedRide = newPersistedRide()
        val totalItems = rideDao.findAll().size
        rideDao.deleteById(savedRide.id!!)
        assertEquals(rideDao.findAll().size, totalItems - 1)
    }

    private fun newPersistedRide(): Ride {
        val addressPickUp = generator.generateAddress()
        val addressDropOff = generator.generateAddress()
        val passenger = passengerDao.save(generator.generatePassenger())
        val ride =
            generator.generateRide().copy(passenger = passenger, pickup = addressPickUp, dropOff = addressDropOff)
        val savedRide = rideDao.save(ride.copy(driver = null))
        return savedRide
    }
}
