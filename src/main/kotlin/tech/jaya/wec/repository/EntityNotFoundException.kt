/**
 * This exception is thrown when an entity is not found in the database.
 *
 * @param message The detailed message of this exception. The detail message is saved for later retrieval by the Throwable.getMessage() method.
 */
package tech.jaya.wec.repository

class EntityNotFoundException(message: String): RuntimeException(message)