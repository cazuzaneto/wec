package tech.jaya.wec.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AddressDaoTest {

    @Autowired
    private lateinit var addressDao: AddressDao
    private final var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `findAll should return a list of cars`() {
        val totalAddresses = 10
        (1..totalAddresses).forEach { _ -> addressDao.save(generator.generateAddress()) }
        val result = addressDao.findAll()
        assertEquals(totalAddresses, result.size)
    }

    @Test
    fun `findAll should return an empty list when there are no addresses`() {
        val result = addressDao.findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findById should return a car when found`() {
        val savedAddress = addressDao.save(generator.generateAddress())
        val foundedCar = addressDao.findById(savedAddress.id!!)
        assertEquals(savedAddress, foundedCar)
    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = addressDao.findById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update addresses when id is not null`() {
        val savedAddress = addressDao.save(generator.generateAddress())
        val updatedResult = addressDao.save(savedAddress.copy(text = "New Text"))
        val expectedAddress = addressDao.findById(savedAddress.id!!)
        assertEquals(expectedAddress, updatedResult)
    }

    @Test
    fun `save should insert new addresses when id is null`() {
        val totalItems = addressDao.findAll().size
        addressDao.save(generator.generateAddress())
        assertEquals(addressDao.findAll().size, totalItems + 1)
    }

    @Test
    fun `deleteById should delete addresses`() {
        val savedCar = addressDao.save(generator.generateAddress())
        val totalItems = addressDao.findAll().size
        addressDao.deleteById(savedCar.id!!)
        assertEquals(addressDao.findAll().size, totalItems - 1)
    }
}
