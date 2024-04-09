package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.dao.PassengerDao

@Service
class PassengerService(private val passengerDao: PassengerDao) {

    fun findById(id: Long): Passenger? {
        return passengerDao.findById(id)
    }

    fun findAll(): List<Passenger> {
        return passengerDao.findAll()
    }

    fun save(passenger: Passenger): Passenger {
        return passengerDao.save(passenger)
    }

    fun deleteById(id: Long) {
        passengerDao.deleteById(id)
    }
}
