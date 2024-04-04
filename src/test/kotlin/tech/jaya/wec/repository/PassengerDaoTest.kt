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
import tech.jaya.wec.model.Passenger

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PassengerDaoTest {

    @Autowired
    @Qualifier("passengerDao")
    private lateinit var passengerDao: Dao<Passenger>
    private final var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `findAll should return a list of passenger`() {
        val totalPassenger = 10
        (1..totalPassenger).forEach { _ -> passengerDao.save(generator.generatePassenger()) }
        val result = passengerDao.findAll()
        assertEquals(totalPassenger, result.size)
    }

    @Test
    fun `findAll should return an empty list when there are no passenger`() {
        val result = passengerDao.findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findById should return a passenger when found`() {
        val savedPassenger = passengerDao.save(generator.generatePassenger())
        val foundedPassenger = passengerDao.findById(savedPassenger.id!!)
        assertEquals(savedPassenger, foundedPassenger)
    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = passengerDao.findById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update passenger when id is not null`() {
        val savedPassenger = passengerDao.save(generator.generatePassenger())
        val updatedResult = passengerDao.save(savedPassenger.copy(name = "New Name"))
        val expectedPassenger = passengerDao.findById(savedPassenger.id!!)
        assertEquals(expectedPassenger, updatedResult)
    }

    @Test
    fun `save should insert new passenger when id is null`() {
        val totalItems = passengerDao.findAll().size
        passengerDao.save(generator.generatePassenger())
        assertEquals(passengerDao.findAll().size, totalItems + 1)
    }

    @Test
    fun `deleteById should delete passenger`() {
        val savedPassenger = passengerDao.save(generator.generatePassenger())
        val totalItems = passengerDao.findAll().size
        passengerDao.deleteById(savedPassenger.id!!)
        assertEquals(passengerDao.findAll().size, totalItems - 1)
    }
}
