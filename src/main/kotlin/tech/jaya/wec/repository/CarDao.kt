package tech.jaya.wec.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tech.jaya.wec.model.Car
import tech.jaya.wec.repository.exception.EntityNotFoundException
import java.util.ResourceBundle

/**
 * This class is responsible for performing CRUD operations on the Car entity.
 *
 * @property jdbcTemplate used to interact with the database.
 */
@Repository
class CarDao(private val jdbcTemplate: JdbcTemplate) : Dao<Car> {

    private val queries = ResourceBundle.getBundle("sql-queries")

    private val simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("cars")
        .usingGeneratedKeyColumns("id")

    private val rowMapper = RowMapper { rs, _ ->
        Car(
            id = rs.getLong("id"),
            licensePlate = rs.getString("license_plate"),
            model = rs.getString("model"),
            color = rs.getString("color")
        )
    }

    /**
     * Retrieves all cars from the database.
     *
     * @return a list of all cars.
     */
    override fun findAll(): List<Car> {
        val sql = queries.getString("CarDao.findAll")
        return jdbcTemplate.query(sql, rowMapper)
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id the ID of the car to retrieve.
     * @return the car if found, null otherwise.
     */
    override fun findById(id: Long): Car? {
        return try {
            val sql = queries.getString("CarDao.findById")
            jdbcTemplate.queryForObject(sql, rowMapper, id)
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
    override fun save(entity: Car): Car {
        return entity.id?.let { update(entity) } ?: insert(entity)
    }

    /**
     * Updates an existing car in the database.
     *
     * @param car the car to update.
     * @return the updated car.
     * @throws EntityNotFoundException if the car does not exist.
     */
    private fun update(car: Car): Car {
        val existingId = car.id!!
        findById(existingId) ?: throw EntityNotFoundException("Car with id $existingId not found")
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
    private fun insert(car: Car): Car {
        val parameters = HashMap<String, Any>(3)
        parameters["license_plate"] = car.licensePlate
        parameters["model"] = car.model
        parameters["color"] = car.color

        val newId = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()

        return car.copy(id = newId)
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id the ID of the car to delete.
     */
    override fun deleteById(id: Long) {
        findById(id) ?: throw EntityNotFoundException("Car with id $id not found")
        val sql = queries.getString("CarDao.deleteById")
        jdbcTemplate.update(sql, id)
    }
}
