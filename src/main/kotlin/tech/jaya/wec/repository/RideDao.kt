/**
 * This class is responsible for performing CRUD operations on Ride entities in the database.
 *
 * @property jdbcTemplate The JdbcTemplate to be used for executing SQL queries.
 * @property queries The Properties object containing the SQL queries.
 * @property simpleJdbcInsert The SimpleJdbcInsert to be used for inserting new Ride entities.
 * @property rowMapper The RowMapper for mapping SQL result sets to Ride entities.
 */
package tech.jaya.wec.repository

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tech.jaya.wec.model.Address
import tech.jaya.wec.model.Ride
import tech.jaya.wec.model.Status
import java.sql.ResultSet
import java.util.Properties

@Repository
class RideDao(private val jdbcTemplate: JdbcTemplate) : Dao<Ride> {

    private val queries: Properties = PropertiesLoaderUtils.loadProperties(ClassPathResource("sql-queries.properties"))

    private val simpleJdbcInsert: SimpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("rides")
        .usingGeneratedKeyColumns("id")

    private val rowMapper = RowMapper<Ride> { rs: ResultSet, _: Int ->
        Ride(
            id = rs.getLong("id"),
            pickup = Address(rs.getLong("pickup_id"), rs.getString("pickup_text")),
            dropOff = Address(rs.getLong("dropoff_id"), rs.getString("dropoff_text")),
            status = Status.valueOf(rs.getString("status"))
        )
    }


    /**
     * Finds a Ride entity by its ID.
     *
     * @param id the ID of the Ride entity to find.
     * @return the Ride entity if found, null otherwise.
     */
    override fun findById(id: Long): Ride? {
        return try {
            val sql = queries.getProperty("ride.findById")
            jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Finds all Ride entities.
     *
     * @return a list of all Ride entities.
     */
    override fun findAll(): List<Ride> {
        val sql = queries.getProperty("ride.findAll")
        return jdbcTemplate.query(sql, rowMapper)
    }

    /**
     * Saves a Ride entity to the database. If the entity already exists, it is updated.
     *
     * @param entity the Ride entity to save.
     * @return the saved Ride entity.
     */
    override fun save(entity: Ride): Ride = entity.id?.let {
        update(entity)
    } ?: run {
        insert(entity)
    }

    private fun insert(ride: Ride): Ride {
        val pickupId = ride.pickup.id ?: throw IllegalArgumentException("Pickup ID is missing")
        val dropOffId = ride.dropOff.id ?: throw IllegalArgumentException("DropOff ID is missing")

        val parameters = HashMap<String, Any>(3)
        parameters["pickup_id"] = pickupId
        parameters["dropoff_id"] = dropOffId
        parameters["status"] = ride.status.name

        val newId = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()

        return ride.copy(id = newId)
    }

    private fun update(ride: Ride): Ride {
        val sql = queries.getProperty("ride.save.update")
        jdbcTemplate.update(sql, ride.pickup.id, ride.dropOff.id, ride.status.name, ride.id)
        return ride
    }

    /**
     * Deletes a Ride entity by its ID.
     *
     * @param id the ID of the Ride entity to delete.
     */
    override fun deleteById(id: Long) {
        findById(id)
        val sql = queries.getProperty("ride.delete")
        jdbcTemplate.update(sql, id)
    }
}