/**
 * This class is responsible for performing CRUD operations on Passenger entities in the database.
 *
 * @property jdbcTemplate The JdbcTemplate to be used for executing SQL queries.
 * @property queries The ResourceBundle containing the SQL queries.
 * @property simpleJdbcInsert The SimpleJdbcInsert to be used for inserting new Passenger entities.
 * @property rowMapper The RowMapper for mapping SQL result sets to Passenger entities.
 */
package tech.jaya.wec.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tech.jaya.wec.model.Passenger
import java.sql.ResultSet
import java.util.ResourceBundle

@Repository
class PassengerDao(private val jdbcTemplate: JdbcTemplate) : Dao<Passenger> {

    private val queries = ResourceBundle.getBundle("sql-queries")
    private val simpleJdbcInsert: SimpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("passengers")
        .usingGeneratedKeyColumns("id")

    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        Passenger(
            id = rs.getLong("id"),
            name = rs.getString("name")
        )
    }

    /**
     * Finds a Passenger entity by its ID.
     *
     * @param id the ID of the Passenger entity to find.
     * @return the Passenger entity if found, null otherwise.
     */
    override fun findById(id: Long): Passenger? {
        return try {
            val sql = queries.getString("passenger.findById")
            return jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }


    /**
     * Finds all Passenger entities.
     *
     * @return a list of all Passenger entities.
     */
    override fun findAll(): List<Passenger> {
        val sql = queries.getString("passenger.findAll")
        return jdbcTemplate.query(sql, rowMapper)
    }


    /**
     * Saves a Passenger entity to the database. If the entity already exists, it is updated.
     *
     * @param entity the Passenger entity to save.
     * @return the saved Passenger entity.
     */
    override fun save(entity: Passenger): Passenger = entity.id?.let {
        update(entity)
    } ?: run {
        insert(entity)
    }

    private fun insert(passenger: Passenger): Passenger {
        val parameters = HashMap<String, Any>(1)
        parameters["name"] = passenger.name

        val newId = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()

        return passenger.copy(id = newId)
    }

    private fun update(entity: Passenger): Passenger {
        val sql = queries.getString("passenger.save.update")
        jdbcTemplate.update(sql, entity.name, entity.id)
        return entity
    }
    
    /**
     * Deletes a Passenger entity by its ID.
     *
     * @param id the ID of the Passenger entity to delete.
     */
    override fun deleteById(id: Long) {
        findById(id)
        val sql = queries.getString("passenger.delete")
        jdbcTemplate.update(sql, id) > 0
    }
}