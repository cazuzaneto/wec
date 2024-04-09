package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.dao.CarDao
import tech.jaya.wec.dao.DriverDao
import tech.jaya.wec.model.Driver

@Service
class DriverService(private val driverDao: DriverDao, private val carDao: CarDao) {

    fun findAll(): List<Driver> {
        return driverDao.findAll()
    }

    fun findById(id: Long): Driver? {
        return driverDao.findById(id)
    }

    fun save(driver: Driver): Driver {
        val newCar = carDao.save(driver.car!!)
        return driverDao.save(driver.copy(car = newCar))
    }

    fun deleteById(id: Long) {
        driverDao.deleteById(id)
    }
}
