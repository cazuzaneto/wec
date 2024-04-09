package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.model.Address
import tech.jaya.wec.dao.AddressDao

@Service
class AddressService(private val addressDao: AddressDao) {

    fun findAll(): List<Address> {
        return addressDao.findAll()
    }

    fun findById(id: Long): Address? {
        return addressDao.findById(id)
    }

    fun save(address: Address): Address {
        return addressDao.save(address)
    }

    fun deleteById(id: Long) {
        addressDao.deleteById(id)
    }
}
