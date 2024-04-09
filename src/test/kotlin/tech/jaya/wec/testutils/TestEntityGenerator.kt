package tech.jaya.wec.testutils

import tech.jaya.wec.model.Address
import tech.jaya.wec.model.Car
import tech.jaya.wec.model.Driver
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.model.Ride
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

class TestEntityGenerator {

    private var factory: PodamFactory = PodamFactoryImpl()

    fun generateDriver(): Driver {
        return factory.manufacturePojo(Driver::class.java).copy(id = null)
    }

    fun generateCar(): Car {
        return factory.manufacturePojo(Car::class.java).copy(id = null)
    }

    fun generateAddress(): Address {
        return factory.manufacturePojo(Address::class.java).copy(id = null)
    }

    fun generateAddressWithId(): Address {
        return factory.manufacturePojo(Address::class.java)
    }

    fun generateRide(): Ride {
        return factory.manufacturePojo(Ride::class.java).copy(id = null)
    }

    fun generatePassenger(): Passenger {
        return factory.manufacturePojo(Passenger::class.java).copy(id = null)
    }

    fun generatePassengerWithId(): Passenger {
        return factory.manufacturePojo(Passenger::class.java)
    }

    fun generateCars(quantity: Int): List<Car> {
        return List(quantity) { generateCar() }
    }

    fun generateDriverWithId(): Driver {
        return factory.manufacturePojo(Driver::class.java)
    }
}
