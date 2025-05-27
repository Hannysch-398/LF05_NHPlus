package de.hitec.nhplus.datastorage;
import de.hitec.nhplus.model.User;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.util.ArrayList;

public class UserDao extends DaoImp<User> {
    public UserDao(Connection connection) {
        super(connection);
    }

    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        long id = set.getLong("id");
        String firstName = set.getString("firstname");
        String surname = set.getString("surname");
        String username = set.getString("username");
        String password = set.getString("password_hash");
        return new User(id, firstName, surname, username, password);
    }

    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        while (set.next()) {
            list.add(getInstanceFromResultSet(set));
        }
        return list;
    }

    @Override
    protected PreparedStatement getCreateStatement(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO user (firstName, surname, username, password_hash) VALUES (?, ?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getReadByIDStatement(long key) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?");
            stmt.setLong(1, key);
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getReadAllStatement() {
        try {
            return connection.prepareStatement("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getUpdateStatement(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE user SET firstname = ?, surname= ?, username = ?, password_hash = ? WHERE id = ?");
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getSurname());

            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setLong(5, user.getId());

            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(long key) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM user WHERE id = ?");
            stmt.setLong(1, key);
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getDeactivateStatement(long key) {
        return null;
    }

    @Override
    protected PreparedStatement setDeleteDateStatement(long key) {
        return null;
    }

    // Optional: Login-Funktion
    public User findByUsername(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM user WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return getInstanceFromResultSet(rs);
        }
        return null;
    }
}