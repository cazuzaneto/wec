package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.model.Car
import tech.jaya.wec.repository.CarDao

@Service
class CarService(private val carDao: CarDao) {

    fun findAll(): List<Car> {
        return carDao.findAll()
    }

    fun findById(id: Long): Car? {
        return carDao.findById(id)
    }

    fun save(car: Car): Car {
        return carDao.save(car)
    }

    fun deleteById(id: Long) {
        carDao.deleteById(id)
    }
}
