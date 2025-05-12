package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Nurse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class NurseDao extends DaoImp<Nurse> {

    /**
     * Implements the Interface <code>DaoImp</code>. Overrides methods to generate specific <code>PreparedStatements</code>,
     * to execute the specific SQL Statements.
     */
    public NurseDao(Connection connection){super(connection);}

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Nurse</code>.
     *
     * @param nurse Object of <code>nurse</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given nurse.
     */

    @Override
    protected PreparedStatement getCreateStatement(Nurse nurse) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO nurse (firstname, surname, phoneNumber) " +
                    "VALUES (?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, nurse.getFirstName());
            preparedStatement.setString(2, nurse.getSurname());
            preparedStatement.setString(3, nurse.getPhoneNumber());


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a nurse by a given nurse id (nid).
     *
     * @param nid Nurse id to query.
     * @return <code>PreparedStatement</code> to query the nurse.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM nurse WHERE nid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Maps a <code>ResultSet</code> of one nurse to an object of <code>Nurse</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Nurse</code>.
     * @return Object of class <code>Nurse</code> with the data from the resultSet.
     */
    @Override
    protected Nurse getInstanceFromResultSet(ResultSet result) throws SQLException {

        return new Nurse(result.getLong(1), result.getString(2), result.getString(3), result.getString(4));

    }
    /**
     * Generates a <code>PreparedStatement</code> to query all nurses.
     *
     * @return <code>PreparedStatement</code> to query all nurses.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM nurse";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    /**
     * Maps a <code>ResultSet</code> of all nurses to an <code>ArrayList</code> of <code>Nurse</code> objects.
     *
     * @param result ResultSet with all rows. The Columns will be mapped to objects of class <code>Nurse</code>.
     * @return <code>ArrayList</code> with objects of class <code>Nurse</code> of all rows in the
     * <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Nurse> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Nurse> list = new ArrayList<>();
        while (result.next()) {
            Nurse nurse = new Nurse(result.getLong(1), result.getString(2), result.getString(3), result.getString(4));


            list.add(nurse);
        }
        return list;
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given nurse, identified
     * by the id of the nurse (nid).
     *
     * @param nurse Nurse object to update.
     * @return <code>PreparedStatement</code> to update the given nurse.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Nurse nurse) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE nurse SET " + "firstname = ?, " + "surname = ?, " + "phoneNumber = ? " +
                 "WHERE nid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, nurse.getFirstName());
            preparedStatement.setString(2, nurse.getSurname());
            preparedStatement.setString(3, nurse.getPhoneNumber());
            preparedStatement.setLong(4, nurse.getNid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a nurse with the given id.
     *
     * @param nid Id of the nurse to delete.
     * @return <code>PreparedStatement</code> to delete nurse with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM nurse WHERE nid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }



}
