package tech.jaya.wec.dao

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import tech.jaya.wec.dao.exception.EntityNotFoundException
import tech.jaya.wec.model.Car
import tech.jaya.wec.model.Driver
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement
import java.util.ResourceBundle


/**
 * This class is responsible for performing CRUD operations on the Driver entity.
 *
 * @property jdbcTemplate used to interact with the database.
 */
@Repository
class DriverDao(private val jdbcTemplate: JdbcTemplate) {

    private val queries = ResourceBundle.getBundle("sql-queries")

    private val driverMap = RowMapper { rs, _ ->
        val carId = rs.getObject("car_id")
        val car = carId?.let {
            Car(
                id = rs.getLong("car_id"),
                licensePlate = rs.getString("license_plate") ?: "",
                model = rs.getString("model") ?: "",
                color = rs.getString("color") ?: ""
            )
        }

        Driver(
            id = rs.getLong("id"),
            name = rs.getString("name") ?: "",
            available = rs.getBoolean("available"),
            activationDate = rs.getTimestamp("activation_date").toLocalDateTime(),
            car = car
        )
    }

    private val carJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("cars")
        .usingGeneratedKeyColumns("id")

    private val carMapper = RowMapper { rs, _ ->
        Car(
            id = rs.getLong("id"),
            licensePlate = rs.getString("license_plate"),
            model = rs.getString("model"),
            color = rs.getString("color")
        )
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id the ID of the car to retrieve.
     * @return the car if found, null otherwise.
     */
    fun findCarById(id: Long): Car? {
        return try {
            val sql = queries.getString("CarDao.findById")
            jdbcTemplate.queryForObject(sql, carMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Saves a car to the database. If the car already exists, it is updated.
     *
     * @param entity the car to save.
     * @return the saved car.
     */
    fun saveCar(entity: Car): Car {
        return entity.id?.let { updateCar(entity) } ?: insertCar(entity)
    }

    /**
     * Updates an existing car in the database.
     *
     * @param car the car to update.
     * @return the updated car.
     * @throws EntityNotFoundException if the car does not exist.
     */
    private fun updateCar(car: Car): Car {
        val existingId = car.id!!
        findCarById(existingId) ?: throw EntityNotFoundException("Car with id $existingId not found")
        val sql = queries.getString("CarDao.save.update")
        jdbcTemplate.update(
            sql,
            car.licensePlate, car.model, car.color, car.id
        )
        return car
    }

    /**
     * Inserts a new car into the database.
     *
     * @param car the car to insert.
     * @return the inserted car with its new ID.
     */
    private fun insertCar(car: Car): Car {
        val parameters = HashMap<String, Any>(3)
        parameters["license_plate"] = car.licensePlate
        parameters["model"] = car.model
        parameters["color"] = car.color

        val newId = carJdbcInsert.executeAndReturnKey(parameters).toLong()

        return car.copy(id = newId)
    }

    /**
     * Retrieves all drivers from the database.
     *
     * @return a list of all drivers.
     */
    fun findAllDrivers(): List<Driver> {
        val sql = queries.getString("DriverDao.findAll")
        return jdbcTemplate.query(sql, driverMap)
    }

    /**
     * Retrieve first driver available from the database.
     *
     * @return a list of all drivers.
     */
    fun firstDriverAvailable(): Driver? {
        return try {
            val sql = queries.getString("DriverDao.firstAvailable")
            jdbcTemplate.queryForObject(sql, driverMap)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Retrieves a driver by its ID.
     *
     * @param id the ID of the driver to retrieve.
     * @return the driver if found, null otherwise.
     */
    fun findDriverById(id: Long): Driver? {
        return try {
            val sql = queries.getString("DriverDao.findById")
            jdbcTemplate.queryForObject(sql, driverMap, id)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Saves a driver to the database. If the driver already exists, it is updated.
     *
     * @param entity the driver to save.
     * @return the saved driver.
     */
    @Transactional
    fun saveDriver(entity: Driver): Driver = entity.id?.let {
        updateDriver(entity)
    } ?: run {
        insertDriver(entity)
    }

    /**
     * Updates an existing driver in the database.
     *
     * @param driver the driver to update.
     * @return the updated driver.
     * @throws EntityNotFoundException if the driver does not exist.
     */
    private fun updateDriver(driver: Driver): Driver {
        val existingId = driver.id!!
        findDriverById(existingId) ?: throw EntityNotFoundException("Driver with id $existingId not found")
        val sql = queries.getString("DriverDao.save.update")
        jdbcTemplate.update(
            sql,
            driver.name,
            driver.available,
            driver.car?.id,
            existingId
        )
        return driver
    }

    /**
     * Inserts a new driver into the database.
     *
     * @param driver the driver to insert.
     * @return the inserted driver with its new ID.
     */
    private fun insertDriver(driver: Driver): Driver {
        val sql = queries.getString("DriverDao.save")
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val car = driver.car?.let { saveCar(it) }

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, driver.name)
            ps.setBoolean(2, driver.available)
            car?.id!!.let { ps.setLong(3, it) }
            ps
        }, keyHolder)

        return keyHolder.keyList[0].let {
            driver.copy(id = it["id"] as Long?)
        }
    }

    /**
     * Deletes a driver by its ID.
     *
     * @param id the ID of the driver to delete.
     */
    fun deleteDriverById(id: Long) {
        findDriverById(id) ?: throw EntityNotFoundException("Driver with id $id not found")
        val sql = queries.getString("DriverDao.deleteById")
        jdbcTemplate.update(sql, id)
    }

    fun setDriverToUnavailable(driver: Driver?) {
        val sql = queries.getString("DriverDao.setDriverToUnavailable")
        jdbcTemplate.update(sql, driver!!.id!!)
    }
}
