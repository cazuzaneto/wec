package tech.jaya.wec.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import tech.jaya.wec.model.Car

@ExtendWith(MockitoExtension::class)
class CarDaoTest {

    @Mock
    private lateinit var jdbcTemplate: JdbcTemplate

    @InjectMocks
    private lateinit var carDao: CarDao

    private lateinit var testCar: Car

    @BeforeEach
    fun setUp() {
        testCar = Car(id = 1L, licensePlate = "ABC123", model = "TestModel", color = "Blue")
    }

    @Test
    fun `Should return list of cars when findAll is called`() {
        `when`(jdbcTemplate.query(any(String::class.java), any(RowMapper::class.java))).thenReturn(listOf(testCar))

        val result = carDao.findAll()

        assert(result.size == 1)
        assert(result[0].licensePlate == testCar.licensePlate)
    }

    @Test
    fun `Should return car when findById is called with valid id`() {
        `when`(jdbcTemplate.queryForObject(any(String::class.java), any(RowMapper::class.java), anyLong())).thenReturn(
            testCar
        )

        val result = carDao.findById(1L)

        assert(result != null)
        assert(result?.licensePlate == testCar.licensePlate)
    }

    @Test
    fun `Should return null when findById is called with invalid id`() {
        `when`(jdbcTemplate.queryForObject(any(String::class.java), any(RowMapper::class.java), anyLong())).thenThrow(
            EmptyResultDataAccessException::class.java
        )

        val result = carDao.findById(-1L)

        assert(result == null)
    }

    @Test
    fun `Should call update when save is called with existing car`() {
        carDao.save(testCar)

        verify(jdbcTemplate).update(any(String::class.java), any(), any(), any(), anyLong())
    }

    @Test
    fun `Should call update with prepared statement when save is called with new car`() {
        val newCar = Car(licensePlate = "ABC123", model = "TestModel", color = "Blue")

        `when`(jdbcTemplate.update(any<PreparedStatementCreator>(), any<GeneratedKeyHolder>())).thenAnswer {
            val keyHolder = it.getArgument(1, GeneratedKeyHolder::class.java)
            keyHolder.keyList.add(mapOf("GENERATED_KEY" to 1L))
            1
        }

        val savedCar = carDao.save(newCar)

        assertNotNull(savedCar.id)
        assertEquals(1L, savedCar.id)
    }


    @Test
    fun `Should call update when deleteById is called`() {
        carDao.deleteById(1L)

        verify(jdbcTemplate).update(any(String::class.java), anyLong())
    }
}
