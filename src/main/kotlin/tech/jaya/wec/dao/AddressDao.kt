package tech.jaya.wec.dao

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tech.jaya.wec.model.Address
import tech.jaya.wec.dao.exception.EntityNotFoundException
import java.util.ResourceBundle

/**
 * This class is responsible for performing CRUD operations on the Address entity.
 *
 * @property jdbcTemplate used to interact with the database.
 */
@Repository
class AddressDao(private val jdbcTemplate: JdbcTemplate) : Dao<Address> {

    private val queries = ResourceBundle.getBundle("sql-queries")

    private val simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        .withTableName("addresses")
        .usingGeneratedKeyColumns("id")

    private val rowMapper = RowMapper { rs, _ ->
        Address(
            id = rs.getLong("id"),
            text = rs.getString("text")
        )
    }

    /**
     * Retrieves all addresses from the database.
     *
     * @return a list of all addresses.
     */
    override fun findAll(): List<Address> {
        val sql = queries.getString("AddressDao.findAll")
        return jdbcTemplate.query(sql, rowMapper)
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param id the ID of the address to retrieve.
     * @return the address if found, null otherwise.
     */
    override fun findById(id: Long): Address? {
        return try {
            val sql = queries.getString("AddressDao.findById")
            jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Saves an address to the database. If the address already exists, it is updated.
     *
     * @param entity the address to save.
     * @return the saved address.
     */
    override fun save(entity: Address): Address {
        return entity.id?.let {
            update(entity)
        } ?: insert(entity)
    }

    /**
     * Inserts a new address into the database.
     *
     * @param address the address to insert.
     * @return the inserted address with its new ID.
     */
    private fun insert(address: Address): Address {
        val parameters = HashMap<String, Any>(1)
        parameters["text"] = address.text

        val newId = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()

        return address.copy(id = newId)
    }

    /**
     * Updates an existing address in the database.
     *
     * @param address the address to update.
     * @return the updated address.
     * @throws EntityNotFoundException if the address does not exist.
     */
    private fun update(address: Address): Address {
        val existingId = address.id!!
        findById(existingId) ?: throw EntityNotFoundException("Address with id $existingId not found")
        val sql = queries.getString("AddressDao.save.update")
        jdbcTemplate.update(sql, address.text, address.id)
        return address
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id the ID of the address to delete.
     * @return true if the address was deleted, false otherwise.
     */
    override fun deleteById(id: Long) {
        findById(id) ?: throw EntityNotFoundException("Address with id $id not found")
        val sql = queries.getString("AddressDao.deleteById")
        jdbcTemplate.update(sql, id)
    }
}
