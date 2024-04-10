package tech.jaya.wec.dao

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tech.jaya.wec.dao.exception.EntityNotFoundException
import tech.jaya.wec.model.Passenger
import java.sql.ResultSet
import java.util.ResourceBundle

/**
 * This class is responsible for performing CRUD operations on Passenger entities in the database.
 *
 * @property jdbcTemplate The JdbcTemplate to be used for executing SQL queries.
 * @property queries The ResourceBundle containing the SQL queries.
 * @property simpleJdbcInsert The SimpleJdbcInsert to be used for inserting new Passenger entities.
 * @property rowMapper The RowMapper for mapping SQL result sets to Passenger entities.
 */
@Repository
class PassengerDao(private val jdbcTemplate: JdbcTemplate) {

    private val queries = ResourceBundle.getBundle("sql-queries")
    private val simpleJdbcInsert: SimpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("passengers")
        .usingGeneratedKeyColumns("id")

    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        Passenger(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            email = rs.getString("email")
        )
    }

    /**
     * Finds a Passenger entity by its ID.
     *
     * @param id the ID of the Passenger entity to find.
     * @return the Passenger entity if found, null otherwise.
     */
    fun findById(id: Long): Passenger? {
        return try {
            val sql = queries.getString("passenger.findById")
            return jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Finds a Passenger entity by its email.
     *
     * @param email the ID of the Passenger entity to find.
     * @return the Passenger entity if found, null otherwise.
     */
     fun findByEmail(email: String): Passenger? {
        return try {
            val sql = queries.getString("passenger.findByEmail")
            return jdbcTemplate.queryForObject(sql, rowMapper, email)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Saves a Passenger entity to the database. If the entity already exists, it is updated.
     *
     * @param entity the Passenger entity to save.
     * @return the saved Passenger entity.
     */
    fun save(entity: Passenger): Passenger = entity.id?.let {
        update(entity)
    } ?: run {
        insert(entity)
    }

    private fun insert(passenger: Passenger): Passenger {
        val parameters = HashMap<String, Any>(1)
        parameters["name"] = passenger.name
        parameters["email"] = passenger.email

        val newId = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()

        return passenger.copy(id = newId)
    }

    private fun update(entity: Passenger): Passenger {
        entity.id?.let { findById(it) }
            ?: throw EntityNotFoundException("Passenger with id ${entity.id} not found")

        val sql = queries.getString("passenger.save.update")
        jdbcTemplate.update(sql, entity.name, entity.id)
        return entity
    }
}
