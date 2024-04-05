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
import tech.jaya.wec.repository.PassengerDao
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
class PassengerServiceTest {

    @InjectMockKs
    private lateinit var passengerService: PassengerService

    @MockK
    private lateinit var passengerDao: PassengerDao

    private var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `should return a passenger by id`() {
        val passenger = generator.generatePassengerWithId()
        val id = requireNotNull(passenger.id) { "Passenger id is null" }
        every { passengerDao.findById(id) } returns passenger

        val result = passengerService.findById(id)

        assertNotNull(result)
        assertEquals(passenger, result)
        verify { passengerDao.findById(id) }
    }

    @Test
    fun `should return all passengers`() {
        val passengers = List(10) { generator.generatePassengerWithId() }
        every { passengerDao.findAll() } returns passengers

        val result = passengerService.findAll()

        assertNotNull(result)
        assertEquals(passengers, result)
        verify { passengerDao.findAll() }
    }

    @Test
    fun `should save a passenger`() {
        val passenger = generator.generatePassengerWithId()

        every { passengerDao.save(passenger) } returns passenger

        val result = passengerService.save(passenger)

        assertNotNull(result)
        assertEquals(passenger, result)
        verify { passengerDao.save(passenger) }
    }

    @Test
    fun `should delete a passenger by id`() {
        val id = 1L
        every { passengerDao.deleteById(id) } returns Unit

        passengerService.deleteById(id)

        verify { passengerDao.deleteById(id) }
    }
}
