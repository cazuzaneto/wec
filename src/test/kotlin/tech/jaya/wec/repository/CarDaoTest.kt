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

@SpringBootTest(properties = ["spring.profiles.active=test"])
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarDaoTest {

    @Autowired
    @Qualifier("carDao")
    private lateinit var carDao: Dao<Car>
    private final var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `findAll should return a list of cars`() {
        val totalCars = 10
        (1..totalCars).forEach { _ -> carDao.save(generator.generateCar()) }
        val result = carDao.findAll()
        assertEquals(totalCars, result.size)
    }

    @Test
    fun `findAll should return an empty list when there are no cars`() {
        val result = carDao.findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findById should return a car when found`() {
        val savedCar = carDao.save(generator.generateCar())
        val foundedCar = carDao.findById(savedCar.id!!)
        assertEquals(savedCar, foundedCar)
    }

    @Test
    fun `findById should return null when not found`() {
        val expectedId = 2L
        val result = carDao.findById(expectedId)
        assertEquals(null, result)
    }

    @Test
    fun `save should update car when id is not null`() {
        val savedCar = carDao.save(generator.generateCar())
        val updatedResult = carDao.save(savedCar.copy(model = "New Model"))
        val expectedCar = carDao.findById(savedCar.id!!)
        assertEquals(expectedCar, updatedResult)
    }

    @Test
    fun `save should insert new car when id is null`() {
        val totalItems = carDao.findAll().size
        carDao.save(generator.generateCar())
        assertEquals(carDao.findAll().size, totalItems + 1)
    }

    @Test
    fun `deleteById should delete car`() {
        val savedCar = carDao.save(generator.generateCar())
        val totalItems = carDao.findAll().size
        carDao.deleteById(savedCar.id!!)
        assertEquals(carDao.findAll().size, totalItems - 1)
    }
}
