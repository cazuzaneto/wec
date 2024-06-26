package tech.jaya.wec.testutils

import tech.jaya.wec.model.Address
import tech.jaya.wec.model.Driver
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.model.Ride
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import java.time.LocalDateTime

class TestEntityGenerator {

    private var factory: PodamFactory = PodamFactoryImpl()

    fun generateDriver(): Driver {
        return factory.manufacturePojo(Driver::class.java).let {
            it.copy(id = null, activationDate = LocalDateTime.now())
                .copy(car = it.car!!.copy(id = null))
        }

    }

    fun generateAddress(): Address {
        return factory.manufacturePojo(Address::class.java).copy(id = null)
    }

    fun generateRide(): Ride {
        return factory.manufacturePojo(Ride::class.java).copy(id = null)
    }

    fun generatePassenger(): Passenger {
        return factory.manufacturePojo(Passenger::class.java).copy(id = null)
    }
}
