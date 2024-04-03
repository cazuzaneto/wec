package tech.jaya.wec.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import tech.jaya.wec.model.Address

class AddressDaoTest {

    private lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var addressDao: AddressDao

    private val address = Address(id = 1L, text = "123 Test St")

    @BeforeEach
    fun setup() {
        jdbcTemplate = mockk()
        addressDao = AddressDao(jdbcTemplate)
    }

    @Test
    fun `findAll should return a list of addresses`() {
        every { jdbcTemplate.query(any<String>(), any<RowMapper<Address>>()) } returns listOf(address)

        val result = addressDao.findAll()

        assertEquals(listOf(address), result)
        verify(exactly = 1) { jdbcTemplate.query(any<String>(), any<RowMapper<Address>>()) }
    }

    @Test
    fun `findById should return an address when found`() {
        every { jdbcTemplate.queryForObject(any<String>(), any<RowMapper<Address>>(), eq(1L)) } returns address

        val result = addressDao.findById(1L)

        assertEquals(address, result)
    }

    @Test
    fun `findById should return null when not found`() {
        every {
            jdbcTemplate.queryForObject(
                any<String>(),
                any<RowMapper<Address>>(),
                eq(1L)
            )
        } throws EmptyResultDataAccessException(0)

        val result = addressDao.findById(1L)

        assertNull(result)
    }

    @Test
    fun `save should return address with new id when id is null`() {
        val addressWithoutId = Address(text = "123 Test St")
        val expectedId = 1L

        val keyHolderMock = mockk<GeneratedKeyHolder>(relaxed = true)

        every {
            jdbcTemplate.update(any<PreparedStatementCreator>(), any<GeneratedKeyHolder>())
        } answers {
            val keyHolderArg = arg<GeneratedKeyHolder>(1)
            val keyMap = mapOf<String, Any>("id" to expectedId)
            keyHolderArg.keyList.add(keyMap)
            1
        }

        every { keyHolderMock.key } returns expectedId

        val result = addressDao.save(addressWithoutId)

        assertEquals(expectedId, result.id)
    }
}
