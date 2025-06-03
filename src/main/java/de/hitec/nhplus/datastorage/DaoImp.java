package de.hitec.nhplus.datastorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation of the {@link Dao} interface, providing common logic
 * for CRUD operations using JDBC. Subclasses must provide specific SQL statements and
 * mapping logic for converting {@link ResultSet} rows into domain objects.
 *
 * @param <T> the type of the domain object this DAO handles
 */
public abstract class DaoImp<T> implements Dao<T> {

    /**
     * JDBC connection used by the DAO implementation.
     */
    protected Connection connection;

    /**
     * Constructs a DAO implementation with a given JDBC connection.
     *
     * @param connection the database connection to use
     */
    public DaoImp(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(T t) throws SQLException {
        getCreateStatement(t).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T read(long key) throws SQLException {
        T object = null;
        ResultSet result = getReadByIDStatement(key).executeQuery();
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> readAll() throws SQLException {
        return getListFromResultSet(getReadAllStatement().executeQuery());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T t) throws SQLException {
        getUpdateStatement(t).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(long key) throws SQLException {
        getDeleteStatement(key).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivateById(long key) throws SQLException {
        getDeactivateStatement(key).executeUpdate();
    }

    /**
     * Maps a single row from the given {@link ResultSet} into a domain object.
     *
     * @param set the {@code ResultSet} to extract the data from
     * @return the domain object corresponding to the current row
     * @throws SQLException if a database access error occurs
     */
    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    /**
     * Maps all rows from the given {@link ResultSet} into a list of domain objects.
     *
     * @param set the {@code ResultSet} containing multiple rows
     * @return a list of domain objects
     * @throws SQLException if a database access error occurs
     */
    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    /**
     * Returns a prepared SQL statement for creating a new entity.
     *
     * @param t the entity to be created
     * @return the prepared statement
     */
    protected abstract PreparedStatement getCreateStatement(T t);

    /**
     * Returns a prepared SQL statement for reading an entity by ID.
     *
     * @param key the primary key of the entity
     * @return the prepared statement
     */
    protected abstract PreparedStatement getReadByIDStatement(long key);

    /**
     * Returns a prepared SQL statement for reading all entities.
     *
     * @return the prepared statement
     */
    protected abstract PreparedStatement getReadAllStatement();

    /**
     * Returns a prepared SQL statement for updating an existing entity.
     *
     * @param t the entity to be updated
     * @return the prepared statement
     */
    protected abstract PreparedStatement getUpdateStatement(T t);

    /**
     * Returns a prepared SQL statement for deleting an entity by ID.
     *
     * @param key the primary key of the entity
     * @return the prepared statement
     */
    protected abstract PreparedStatement getDeleteStatement(long key);

    /**
     * Returns a prepared SQL statement for deactivating (soft-deleting) an entity by ID.
     *
     * @param key the primary key of the entity
     * @return the prepared statement
     */
    protected abstract PreparedStatement getDeactivateStatement(long key);

    /**
     * Returns a prepared SQL statement for setting the deletion date of an entity.
     * Typically used in soft-delete scenarios to mark the deletion timestamp.
     *
     * @param key the primary key of the entity
     * @return the prepared statement
     */
    protected abstract PreparedStatement setDeleteDateStatement(long key);
}
