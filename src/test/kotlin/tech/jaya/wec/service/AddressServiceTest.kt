package tech.jaya.wec.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import tech.jaya.wec.dao.AddressDao
import tech.jaya.wec.testutils.TestEntityGenerator

@ExtendWith(SpringExtension::class)
class AddressServiceTest {

    @InjectMockKs
    private lateinit var addressService: AddressService

    @MockK
    private lateinit var addressDao: AddressDao

    private var generator: TestEntityGenerator = TestEntityGenerator()

    @Test
    fun `should return all addresses`() {
        val addresses = List(10) { generator.generateAddressWithId() }
        every { addressDao.findAll() } returns addresses

        val result = addressService.findAll()

        assertNotNull(result)
        assertEquals(addresses, result)
    }

    @Test
    fun `should return address by id`() {
        val address = generator.generateAddressWithId()
        val addressId = requireNotNull(address.id) { "Address id is null" }
        every { addressDao.findById(addressId) } returns address

        val result = addressService.findById(addressId)

        assertNotNull(result)
        assertEquals(address, result)
    }

    @Test
    fun `should save a new address`() {
        val address = generator.generateAddress()
        val savedAddress = generator.generateAddressWithId()
        every { addressDao.save(address) } returns savedAddress

        val result = addressService.save(address)

        assertNotNull(result)
        assertEquals(savedAddress.text, result.text)
        assertNotNull(result.id)
    }

    @Test
    fun `should delete an address`() {
        every { addressDao.deleteById(1L) } returns Unit

        addressService.deleteById(1L)
    }
}
