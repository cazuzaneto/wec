package tech.jaya.wec.service

import org.springframework.stereotype.Service
import tech.jaya.wec.dao.AddressDao
import tech.jaya.wec.dao.DriverDao
import tech.jaya.wec.dao.PassengerDao
import tech.jaya.wec.dao.RideDao
import tech.jaya.wec.model.Ride

@Service
class RideWorkFlow(
    private val rideDao: RideDao,
    private val passengerDao: PassengerDao,
    private val driverDao: DriverDao,
    private val addressDao: AddressDao) {

    // TODO propagar a transaction e fazer fluxos de rollback

    fun start(ride: Ride) : Ride {
        val passenger = passengerDao.findByEmail(ride.passenger.email)
        val driver = driverDao.firstAvailable()
        val pickupAddress = addressDao.save(ride.pickup)

        driverDao.setDriverToUnavailable(driver)

        return rideDao.save(
            ride.copy(
                passenger = passenger!!,
                driver = driver!!,
                pickup = pickupAddress
            )
        )
    }


}