package de.hitec.nhplus.datastorage;
import de.hitec.nhplus.model.User;
import java.sql.*;
import java.util.ArrayList;

public class UserDao extends DaoImp<User> {
    public UserDao(Connection connection) {
        super(connection);
    }

    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet set) throws SQLException {
        return null;
    }

    @Override
    protected PreparedStatement getCreateStatement(User user) {
        return null;
    }

    @Override
    protected PreparedStatement getReadByIDStatement(long key) {
        return null;
    }

    @Override
    protected PreparedStatement getReadAllStatement() {
        return null;
    }

    @Override
    protected PreparedStatement getUpdateStatement(User user) {
        return null;
    }

    @Override
    protected PreparedStatement getDeleteStatement(long key) {
        return null;
    }
}
