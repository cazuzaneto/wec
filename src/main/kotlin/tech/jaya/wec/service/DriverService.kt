package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.model.Driver
import tech.jaya.wec.repository.DriverDao

@Service
class DriverService(private val driverDao: DriverDao, private val carService: CarService) {

    fun findAll(): List<Driver> {
        return driverDao.findAll()
    }

    fun findById(id: Long): Driver? {
        return driverDao.findById(id)
    }

    fun save(driver: Driver, carId: Long?): Driver {
        val car = carId?.let { carService.findById(it) } ?: throw IllegalArgumentException("Car not found")
        driver.car = car
        return driverDao.save(driver)
    }

    fun deleteById(id: Long) {
        driverDao.deleteById(id)
    }
}
