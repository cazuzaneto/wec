package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.dao.DriverDao
import tech.jaya.wec.model.Driver

@Service
class DriverService(private val driverDao: DriverDao) {

    fun findAll(): List<Driver> {
        return driverDao.findAll()
    }

    fun findById(id: Long): Driver? {
        return driverDao.findById(id)
    }

    fun save(driver: Driver): Driver {
        return driverDao.save(driver)
    }

    fun deleteById(id: Long) {
        driverDao.deleteById(id)
    }
}
