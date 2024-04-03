package tech.jaya.wec.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import tech.jaya.wec.model.Car
import java.sql.Statement

@Repository
class CarDao(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        Car(
            id = rs.getLong("id"),
            licensePlate = rs.getString("license_plate"),
            model = rs.getString("model"),
            color = rs.getString("color")
        )
    }

    fun findAll(): List<Car> {
        return jdbcTemplate.query("SELECT * FROM cars", rowMapper)
    }

    fun findById(id: Long): Car? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM cars WHERE id = ?", rowMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun save(car: Car): Car {
        car.id?.let {
            jdbcTemplate.update(
                "UPDATE cars SET license_plate = ?, model = ?, color = ? WHERE id = ?",
                car.licensePlate, car.model, car.color, it
            )
            return car
        } ?: run {
            val insertStatement = "INSERT INTO cars (license_plate, model, color) VALUES (?, ?, ?)"
            val keyHolder = GeneratedKeyHolder()

            jdbcTemplate.update({ connection ->
                connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS).apply {
                    setString(1, car.licensePlate)
                    setString(2, car.model)
                    setString(3, car.color)
                }
            }, keyHolder)

            return car.copy(id = keyHolder.key!!.toLong())
        }
    }


    fun deleteById(id: Long) {
        jdbcTemplate.update("DELETE FROM cars WHERE id = ?", id)
    }
}
