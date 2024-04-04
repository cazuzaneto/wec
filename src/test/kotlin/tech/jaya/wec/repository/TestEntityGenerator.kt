package tech.jaya.wec.repository

import tech.jaya.wec.model.Address
import tech.jaya.wec.model.Car
import tech.jaya.wec.model.Driver
import uk.co.jemos.podam.api.DefaultClassInfoStrategy
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import java.util.stream.IntStream


class TestEntityGenerator {

    private var factory: PodamFactory = PodamFactoryImpl();

    fun generateDriver(): Driver {
        return factory.manufacturePojo(Driver::class.java).copy(id = null)
    }

    fun generateDrivers(count: Int): List<Driver> {
        return IntStream.range(0, count)
            .mapToObj { generateDriver() }
            .toList()

    }

    fun generateCar(): Car {
        return factory.manufacturePojo(Car::class.java).copy(id = null)
    }

    fun generateCars(count: Int): List<Car> {
        return IntStream.range(0, count)
            .mapToObj { generateCar() }
            .toList()
    }

    fun generateAddress(): Address {
        return factory.manufacturePojo(Address::class.java).copy(id = null)
    }

    fun generateAddresses(count: Int): List<Address> {
        return IntStream.range(0, count)
            .mapToObj { generateAddress() }
            .toList()
    }
}

