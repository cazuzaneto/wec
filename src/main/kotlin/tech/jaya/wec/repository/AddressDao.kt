package tech.jaya.wec.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import tech.jaya.wec.model.Address
import java.sql.Statement

@Repository
class AddressDao(private val jdbcTemplate: JdbcTemplate) {

    val rowMapper = RowMapper { rs, _ ->
        Address(
            id = rs.getLong("id"),
            text = rs.getString("text")
        )
    }

    fun findAll(): List<Address> {
        return jdbcTemplate.query("SELECT * FROM addresses", rowMapper)
    }

    fun findById(id: Long): Address? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM addresses WHERE id = ?", rowMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun save(address: Address): Address {
        address.id?.let {
            jdbcTemplate.update("UPDATE addresses SET text = ? WHERE id = ?", address.text, it)
            return address
        }

        val insertStatement = "INSERT INTO addresses (text) VALUES (?)"
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, address.text)
            ps
        }, keyHolder)
        val newId = keyHolder.key!!.toLong()
        return address.copy(id = newId)
    }

    fun update(address: Address): Boolean {
        val updatedRows = jdbcTemplate.update("UPDATE addresses SET text = ? WHERE id = ?", address.text, address.id)
        return updatedRows > 0
    }

    fun deleteById(id: Long): Boolean {
        val updatedRows = jdbcTemplate.update("DELETE FROM addresses WHERE id = ?", id)
        return updatedRows > 0
    }
}