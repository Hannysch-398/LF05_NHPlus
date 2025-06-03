package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Nurse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NurseDao extends DaoImp<Nurse> {

    /**
     * Implements the Interface <code>DaoImp</code>. Overrides methods to generate specific
     * <code>PreparedStatements</code>,
     * to execute the specific SQL Statements.
     */
    public NurseDao(Connection connection) {
        super(connection);
    }

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

            final String SQL = "INSERT INTO nurse (firstname, surname, phoneNumber,status,deletionDate,archiveDate, changedBy, deletedBy) " +
                    "VALUES " +
                    "(?, ?, ?,?,?,?,?,?)";

            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, nurse.getFirstName());
            preparedStatement.setString(2, nurse.getSurname());
            preparedStatement.setString(3, nurse.getPhoneNumber());
            preparedStatement.setString(4, nurse.getStatus());
            if (nurse.getDeletionDate() != null) {
                preparedStatement.setDate(5, java.sql.Date.valueOf(nurse.getDeletionDate()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.DATE);
            }
            if (nurse.getArchiveDate() != null) {
                preparedStatement.setDate(6, java.sql.Date.valueOf(nurse.getArchiveDate()));
            } else {
                preparedStatement.setNull(6, java.sql.Types.DATE);
            }
            preparedStatement.setString(7, nurse.getChangedBy());
            preparedStatement.setString(8, nurse.getDeletedBy());

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
        LocalDate deletionDate = null;
        if (result.getDate("deletionDate") != null) {
            deletionDate = result.getDate("deletionDate").toLocalDate();
        }

        LocalDate archiveDate = null;
        if (result.getDate("archiveDate") != null) {
            archiveDate = result.getDate("archiveDate").toLocalDate();
        }


        return new Nurse(
                result.getLong("nid"),
                result.getString("firstname"),
                result.getString("surname"),
                result.getString("phoneNumber"),
                result.getString("status"),
                deletionDate,
                archiveDate,
                result.getString("changedBy"),
                result.getString("deletedBy")
        );

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
            final String SQL = "SELECT * FROM nurse WHERE status = ?";
            statement = this.connection.prepareStatement(SQL);
            statement.setString(1, Nurse.STATUS_ACTIVE);
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
            // Konvertierung von java.sql.Date zu LocalDate (null-sicher)
            LocalDate deletionDate = null;
            java.sql.Date sqlDeletionDate = result.getDate("deletionDate");
            if (sqlDeletionDate != null) {
                deletionDate = sqlDeletionDate.toLocalDate();
            }

            LocalDate archiveDate = null;
            java.sql.Date sqlArchiveDate = result.getDate("archiveDate");
            if (sqlArchiveDate != null) {
                archiveDate = sqlArchiveDate.toLocalDate();
            }


            Nurse nurse = new Nurse(
                    result.getLong("nid"),
                    result.getString("firstname"),
                    result.getString("surname"),
                    result.getString("phoneNumber"),
                    result.getString("status"),
                    deletionDate,
                    archiveDate,
                    result.getString("changedBy"),
                    result.getString("deletedBy")
            );


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
            final String SQL =

                    "UPDATE nurse SET " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "phoneNumber = ?, " +
                            "status = ?," +
                            "deletionDate = ?, " +
                            "archiveDate = ?, " +
                            "changedBy = ?, " +
                            "deletedBy = ? " +
                            "WHERE nid = ?";

            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, nurse.getFirstName());
            preparedStatement.setString(2, nurse.getSurname());
            preparedStatement.setString(3, nurse.getPhoneNumber());
            preparedStatement.setString(4, nurse.getStatus());


            if (nurse.getDeletionDate() != null) {
                preparedStatement.setDate(5, java.sql.Date.valueOf(nurse.getDeletionDate()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.DATE);
            }

            if (nurse.getArchiveDate() != null) {
                preparedStatement.setDate(6, java.sql.Date.valueOf(nurse.getArchiveDate()));
            } else {
                preparedStatement.setNull(6, java.sql.Types.DATE);
            }
            preparedStatement.setString(7, nurse.getChangedBy());
            preparedStatement.setString(8, nurse.getDeletedBy());

            preparedStatement.setLong(9, nurse.getNid());


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

    /**
     * Generates a <code>PreparedStatement</code> to mark a nurse as inactive.
     * This is a soft-deletion by setting the <code>active</code> column to 'i'.
     *
     * @param nid ID of the nurse to deactivate.
     * @return <code>PreparedStatement</code> to update the nurse's active status.
     */
    @Override
    protected PreparedStatement getDeactivateStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE nurse SET active = 'i' WHERE nid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to set the deletion and archive dates
     * of a nurse identified by the given ID. The archive date is set to the current date,
     * the deletion date is set 10 years in the future.
     *
     * @param nid ID of the nurse to update.
     * @return <code>PreparedStatement</code> to update the deletion and archive dates.
     */
    @Override
    protected PreparedStatement setDeleteDateStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE nurse SET deletionDate = ?, archiveDate = ? WHERE nid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now().plusYears(10))); // Löschdatum
            preparedStatement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));               // Archivdatum
            preparedStatement.setLong(3, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Deletes all nurses from the database whose deletion date has passed,
     * are not active anymore, and have a non-null deletion date.
     * This is a hard delete.
     *
     * @throws SQLException If an error occurs during SQL execution.
     */
    public void deleteExpiredNurses() throws SQLException {
        final String SQL = "DELETE FROM nurse " + "WHERE deletionDate IS NOT NULL " + "AND deletionDate <= ? " +
                "AND status != ?";  // Nur wenn NICHT aktiv

        try (PreparedStatement stmt = this.connection.prepareStatement(SQL)) {
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(2, Nurse.STATUS_ACTIVE);

            int deleted = stmt.executeUpdate();

        }
    }


}
