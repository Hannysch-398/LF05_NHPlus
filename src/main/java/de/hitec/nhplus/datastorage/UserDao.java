package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) implementation for the {@link User} entity.
 * Provides database operations for creating, reading, updating and deleting user records.
 */

public class UserDao extends DaoImp<User> {
    /**
     * Constructs a {@code UserDao} with the given database connection.
     *
     * @param connection The database connection to be used for SQL operations.
     */
    public UserDao(Connection connection) {
        super(connection);
    }

    /**
     * Maps a {@link ResultSet} row to a {@link User} object.
     *
     * @param set A {@link ResultSet} pointing to a single row.
     * @return A populated {@link User} object.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        long id = set.getLong("id");
        String firstName = set.getString("firstname");
        String surname = set.getString("surname");
        String username = set.getString("username");
        String password = set.getString("password_hash");
        String role = set.getString("role");
        return new User(id, firstName, surname, username, password, role);
    }

    /**
     * Maps a {@link ResultSet} to a list of {@link User} objects.
     *
     * @param set The {@link ResultSet} containing multiple user records.
     * @return An {@link ArrayList} of {@link User} objects.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        while (set.next()) {
            list.add(getInstanceFromResultSet(set));
        }
        return list;
    }

    /**
     * Creates a {@link PreparedStatement} for inserting a new user into the database.
     *
     * @param user The {@link User} object to be persisted.
     * @return A {@link PreparedStatement} for executing the insert query.
     */
    @Override
    protected PreparedStatement getCreateStatement(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement(

                    //             "INSERT INTO user (firstname, surname, username, password_hash,role) VALUES (?, ?,
                    //             ?,?,?)",
                    "INSERT INTO user (firstName, surname, username, password_hash, role) VALUES (?, ?,?,?,?)",

                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Creates a {@link PreparedStatement} for querying a user by ID.
     *
     * @param key The unique user ID.
     * @return A {@link PreparedStatement} for the read operation.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long key) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            stmt.setLong(1, key);
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a {@link PreparedStatement} to read all users from the database.
     *
     * @return A {@link PreparedStatement} for the read-all operation.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        try {
            return connection.prepareStatement("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a {@link PreparedStatement} to update an existing user.
     *
     * @param user The {@link User} object with updated data.
     * @return A {@link PreparedStatement} for the update operation.
     */
    @Override
    protected PreparedStatement getUpdateStatement(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE user SET firstname = ?, surname= ?, username = ?, password_hash = ?, role = ? WHERE id = " +
                            "?");
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());
            stmt.setLong(6, user.getId());

            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a {@link PreparedStatement} to delete a user by ID.
     *
     * @param key The unique ID of the user to delete.
     * @return A {@link PreparedStatement} for the delete operation.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long key) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM user WHERE id = ?");
            stmt.setLong(1, key);
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This operation is not supported for {@link User} entities.
     *
     * @throws UnsupportedOperationException Always thrown when called.
     */
    @Override
    protected PreparedStatement getDeactivateStatement(long key) {
        throw new UnsupportedOperationException("Users cannot be deactivated.");
    }

    /**
     * This operation is not supported for {@link User} entities.
     *
     * @throws UnsupportedOperationException Always thrown when called.
     */
    @Override
    protected PreparedStatement setDeleteDateStatement(long key) {
        throw new UnsupportedOperationException("Users cannot be deactivated.");
    }

    /**
     * Finds a user by their unique username.
     *
     * @param username The username to search for.
     * @return The {@link User} object if found; otherwise {@code null}.
     * @throws SQLException If a database access error occurs.
     */
    public User findByUsername(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return getInstanceFromResultSet(rs);
        }
        return null;
    }
}