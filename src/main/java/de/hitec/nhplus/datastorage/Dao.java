package de.hitec.nhplus.datastorage;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic Data Access Object (DAO) interface that defines standard CRUD operations
 * for entities persisted in a relational database.
 *
 * @param <T> the type of the entity to be managed
 */
public interface Dao<T> {
    /**
     * Persists the given entity into the database.
     *
     * @param t the entity to be created
     * @throws SQLException if a database access error occurs
     */
    void create(T t) throws SQLException;

    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param key the primary key of the entity
     * @return the corresponding entity, or {@code null} if not found
     * @throws SQLException if a database access error occurs
     */
    T read(long key) throws SQLException;


    /**
     * Retrieves all entities of type {@code T} from the database.
     *
     * @return a list of all entities, or an empty list if none found
     * @throws SQLException if a database access error occurs
     */
    List<T> readAll() throws SQLException;

    /**
     * Updates the given entity in the database.
     *
     * @param t the entity with updated values
     * @throws SQLException if a database access error occurs
     */
    void update(T t) throws SQLException;

    /**
     * Permanently deletes the entity with the given ID from the database.
     *
     * @param key the ID of the entity to be deleted
     * @throws SQLException if a database access error occurs
     */
    void deleteById(long key) throws SQLException;

    /**
     * Logically deactivates the entity with the given ID (e.g., by setting a flag).
     *
     * @param key the ID of the entity to deactivate
     * @throws SQLException if a database access error occurs
     */
    void deactivateById(long key) throws SQLException;
}
