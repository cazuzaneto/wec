package tech.jaya.wec.dao.exception

/**
 * This exception is thrown when an entity is not found in the database.
 *
 * @param message The detailed message of this exception. The detail message is saved for later retrieval by the Throwable.getMessage() method.
 */
class EntityNotFoundException(message: String) : RuntimeException(message)
