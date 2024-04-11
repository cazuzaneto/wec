package tech.jaya.wec.dao

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
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

    private val rowMapper = RowMapper { rs, _ ->
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

    /**
     * Retrieves all drivers from the database.
     *
     * @return a list of all drivers.
     */
    fun findAll(): List<Driver> {
        val sql = queries.getString("DriverDao.findAll")
        return jdbcTemplate.query(sql, rowMapper)
    }

    /**
     * Retrieve first driver available from the database.
     *
     * @return a list of all drivers.
     */
    fun firstAvailable(): Driver? {
        return try {
            val sql = queries.getString("DriverDao.firstAvailable")
            jdbcTemplate.queryForObject(sql, rowMapper)
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
    fun findById(id: Long): Driver? {
        return try {
            val sql = queries.getString("DriverDao.findById")
            jdbcTemplate.queryForObject(sql, rowMapper, id)
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
    fun save(entity: Driver): Driver = entity.id?.let {
        update(entity)
    } ?: run {
        insert(entity)
    }

    /**
     * Updates an existing driver in the database.
     *
     * @param driver the driver to update.
     * @return the updated driver.
     * @throws EntityNotFoundException if the driver does not exist.
     */
    private fun update(driver: Driver): Driver {
        val existingId = driver.id!!
        findById(existingId) ?: throw EntityNotFoundException("Driver with id $existingId not found")
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
    private fun insert(driver: Driver): Driver {
        val sql = queries.getString("DriverDao.save")
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, driver.name)
            ps.setBoolean(2, driver.available)
            ps.setLong(3, driver.car?.id!!)
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
    fun deleteById(id: Long) {
        findById(id) ?: throw EntityNotFoundException("Driver with id $id not found")
        val sql = queries.getString("DriverDao.deleteById")
        jdbcTemplate.update(sql, id)
    }

    fun setDriverToUnavailable(driver: Driver?) {
        val sql = queries.getString("DriverDao.setDriverToUnavailable")
        jdbcTemplate.update(sql, driver!!.id!!)
    }
}
