package tech.jaya.wec.dao

import org.junit.jupiter.api.Assertions.assertEquals
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
class PassengerDaoTest {

    @Autowired
    private lateinit var passengerDao: PassengerDao
    private final var generator: TestEntityGenerator = TestEntityGenerator()

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
}
