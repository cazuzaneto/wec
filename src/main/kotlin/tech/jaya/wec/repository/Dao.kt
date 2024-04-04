/**
 * This interface defines the basic CRUD operations that all DAOs in the system must implement.
 *
 * @param T the type of the entity that the DAO manages.
 */
package tech.jaya.wec.repository

interface Dao<T> {
    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity to find.
     * @return the entity if found, null otherwise.
     */
    fun findById(id: Long): T?

    /**
     * Finds all entities.
     *
     * @return a list of all entities.
     */
    fun findAll(): List<T>

    /**
     * Saves an entity to the database. If the entity already exists, it is updated.
     *
     * @param entity the entity to save.
     * @return the saved entity.
     */
    fun save(entity: T): T

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete.
     */
    fun deleteById(id: Long)
}