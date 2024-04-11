package tech.jaya.wec.dao

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tech.jaya.wec.dao.exception.EntityNotFoundException
import tech.jaya.wec.model.Address
import tech.jaya.wec.model.Car
import tech.jaya.wec.model.Driver
import tech.jaya.wec.model.Passenger
import tech.jaya.wec.model.Ride
import tech.jaya.wec.model.Status
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.Properties

/**
 * This class is responsible for performing CRUD operations on Ride entities in the database.
 *
 * @property jdbcTemplate The JdbcTemplate to be used for executing SQL queries.
 * @property queries The Properties object containing the SQL queries.
 * @property rideTable The SimpleJdbcInsert to be used for inserting new Ride entities.
 * @property rideMapper The RowMapper for mapping SQL result sets to Ride entities.
 */
@Repository
class RideDao(private val jdbcTemplate: JdbcTemplate) {

    private val queries: Properties = PropertiesLoaderUtils.loadProperties(ClassPathResource("sql-queries.properties"))

    private val rideTable: SimpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("rides")
        .usingGeneratedKeyColumns("id")

    private val addressTable = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("addresses")
        .usingGeneratedKeyColumns("id")

    private val rideMapper = RowMapper<Ride> { rs: ResultSet, _: Int ->
        val driverId = rs.getObject("driver_id") as Long?
        val driverName = rs.getObject("driver_name") as String?
        val driverAvailable = rs.getObject("driver_available") as Boolean?
        val activationDate = rs.getObject("activation_date") as Timestamp?
        val carId = rs.getObject("car_id") as Long?
        val carLicensePlate = rs.getObject("car_license_plate") as String?
        val carModel = rs.getObject("car_model") as String?
        val carColor = rs.getObject("car_color") as String?

        val car = carId?.let {
            Car(
                id = it,
                licensePlate = carLicensePlate!!,
                model = carModel!!,
                color = carColor!!
            )
        }

        val driver = driverId?.let {
            Driver(
                id = it,
                name = driverName!!,
                available = driverAvailable!!,
                activationDate = activationDate!!.toLocalDateTime(),
                car = car
            )
        }

        Ride(
            id = rs.getLong("ride_id"),
            pickup = Address(rs.getLong("pickup_id"), rs.getString("pickup_text")),
            dropOff = Address(rs.getLong("dropoff_id"), rs.getString("dropoff_text")),
            status = Status.valueOf(rs.getString("ride_status")),
            driver = driver,
            passenger = Passenger(
                id = rs.getLong("passenger_id"),
                name = rs.getString("passenger_name"),
                email = rs.getString("passenger_email")
            )
        )
    }

    private val addressMapper = RowMapper { rs, _ ->
        Address(
            id = rs.getLong("id"),
            text = rs.getString("text")
        )
    }

    /**
     * Saves an address to the database. If the address already exists, it is updated.
     *
     * @param entity the address to save.
     * @return the saved address.
     */
    fun saveAddress(entity: Address): Address {
        return entity.id?.let {
            updateAddress(entity)
        } ?: insertAddress(entity)
    }

    /**
     * Inserts a new address into the database.
     *
     * @param address the address to insert.
     * @return the inserted address with its new ID.
     */
    private fun insertAddress(address: Address): Address {
        val parameters = HashMap<String, Any>(1)
        parameters["text"] = address.text

        val newId = addressTable.executeAndReturnKey(parameters).toLong()
        return address.copy(id = newId)
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param id the ID of the address to retrieve.
     * @return the address if found, null otherwise.
     */
    fun findAddressById(id: Long): Address? {
        return try {
            val sql = queries.getProperty("AddressDao.findById")
            jdbcTemplate.queryForObject(sql, addressMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Updates an existing address in the database.
     *
     * @param address the address to update.
     * @return the updated address.
     * @throws EntityNotFoundException if the address does not exist.
     */
    private fun updateAddress(address: Address): Address {
        val existingId = address.id!!
        findAddressById(existingId) ?: throw EntityNotFoundException("Address with id $existingId not found")
        val sql = queries.getProperty("AddressDao.save.update")
        jdbcTemplate.update(sql, address.text, address.id)
        return address
    }

    /**
     * Finds a Ride entity by its ID.
     *
     * @param id the ID of the Ride entity to find.
     * @return the Ride entity if found, null otherwise.
     */
    fun findById(id: Long): Ride? {
        return try {
            val sql = queries.getProperty("ride.findById")
            jdbcTemplate.queryForObject(sql, rideMapper, id)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Finds all Ride entities.
     *
     * @return a list of all Ride entities.
     */
    fun findAll(): List<Ride> {
        val sql = queries.getProperty("ride.findAll")
        return jdbcTemplate.query(sql, rideMapper)
    }

    /**
     * Saves a Ride entity to the database. If the entity already exists, it is updated.
     *
     * @param entity the Ride entity to save.
     * @return the saved Ride entity.
     */
    fun save(entity: Ride): Ride = entity.id?.let {
        update(entity)
    } ?: run {
        insert(entity)
    }

    private fun insert(ride: Ride): Ride {
        val passengerId = ride.passenger.id ?: throw IllegalArgumentException("Passenger Id is missing")
        val parameters = HashMap<String, Any>(5)

        val pickupAddress = insertAddress(ride.pickup)
        val dropOffAddress = insertAddress(ride.dropOff)

        ride.run {
            parameters["status"] = ride.status.name
            parameters["passenger_id"] = passengerId
            parameters["pickup_id"] = pickupAddress.id!!
            parameters["dropoff_id"] = dropOffAddress.id!!
        }

        ride.driver?.id?.let { parameters["driver_id"] = it }

        val newId = rideTable.executeAndReturnKey(parameters).toLong()

        return ride.copy(id = newId, pickup = pickupAddress, dropOff = dropOffAddress)
    }

    private fun update(ride: Ride): Ride {
        ride.id?.let { findById(it) }
            ?: throw EntityNotFoundException("Ride with id ${ride.id} not found")

        val sql = queries.getProperty("ride.save.update")
        jdbcTemplate.update(
            sql,
            ride.pickup.id,
            ride.dropOff.id,
            ride.status.name,
            ride.driver?.id,
            ride.passenger.id,
            ride.id
        )
        return ride
    }

    /**
     * Deletes a Ride entity by its ID.
     *
     * @param id the ID of the Ride entity to delete.
     */
    fun deleteById(id: Long) {
        findById(id) ?: throw EntityNotFoundException("Ride with id $id not found")
        val sql = queries.getProperty("ride.delete")
        jdbcTemplate.update(sql, id)
    }
}
