package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Interface <code>DaoImp</code>. Overrides methods to generate specific <code>PreparedStatements</code>,
 * to execute the specific SQL Statements.
 */
public class TreatmentDao extends DaoImp<Treatment> {

    /**
     * The constructor initiates an object of <code>TreatmentDao</code> and passes the connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the SQL-statements.
     */
    public TreatmentDao(Connection connection) {
        super(connection);
    }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Treatment</code>.
     *
     * @param treatment Object of <code>Treatment</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given patient.
     */
    @Override
    protected PreparedStatement getCreateStatement(Treatment treatment) {
        PreparedStatement preparedStatement = null;
        try {

            final String SQL =
                    "INSERT INTO treatment (pid, treatment_date, begin, end, description, remark,nid,status," +
                            "deletionDate,archiveDate, changedBy, deletedBy ) " + "VALUES (?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, treatment.getPid());
            preparedStatement.setString(2, treatment.getDate());
            preparedStatement.setString(3, treatment.getBegin());
            preparedStatement.setString(4, treatment.getEnd());
            preparedStatement.setString(5, treatment.getDescription());
            preparedStatement.setString(6, treatment.getRemarks());
            preparedStatement.setLong(7, treatment.getNid());
            preparedStatement.setString(8, treatment.getStatus());
            if (treatment.getDeletionDate() != null) {
                preparedStatement.setDate(9, java.sql.Date.valueOf(treatment.getDeletionDate()));
            } else {
                preparedStatement.setNull(9, java.sql.Types.DATE);
            }
            if (treatment.getArchiveDate() != null) {
                preparedStatement.setDate(10, java.sql.Date.valueOf(treatment.getArchiveDate()));
            } else {
                preparedStatement.setNull(10, java.sql.Types.DATE);
            }
            preparedStatement.setString(11, treatment.getChangedBy());
            preparedStatement.setString(12, treatment.getDeletedBy());

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a treatment by a given treatment id (tid).
     *
     * @param tid Treatment id to query.
     * @return <code>PreparedStatement</code> to query the treatment.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long tid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM treatment WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, tid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Maps a <code>ResultSet</code> of one treatment to an object of <code>Treatment</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Treatment</code>.
     * @return Object of class <code>Treatment</code> with the data from the resultSet.
     */
    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
        LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
        LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
        LocalDate deletionDate = null;
        if (result.getDate("deletionDate") != null) {
            deletionDate = result.getDate("deletionDate").toLocalDate();
        }
        LocalDate archiveDate = null;
        if (result.getDate("archiveDate") != null) {
            archiveDate = result.getDate("archiveDate").toLocalDate();
        }
        return new Treatment(result.getLong(1), result.getLong(2), date, begin, end, result.getString(6),
                result.getString(7), result.getLong(8), result.getString("status"), deletionDate, archiveDate, result.getString("changedBy"),result.getString("deletedBy"));
    }

    /**
     * Generates a <code>PreparedStatement</code> to query all treatments.
     *
     * @return <code>PreparedStatement</code> to query all treatments.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM treatment";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    /**
     * Maps a <code>ResultSet</code> of all treatments to an <code>ArrayList</code> with objects of class
     * <code>Treatment</code>.
     *
     * @param result ResultSet with all rows. The columns will be mapped to objects of class <code>Treatment</code>.
     * @return <code>ArrayList</code> with objects of class <code>Treatment</code> of all rows in the
     * <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
            LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
            LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
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
            /*Treatment treatment =
                    new Treatment(result.getLong(1),
                            result.getLong(2),
                            date, begin, end,
                            result.getString(6),
                            result.getString(7),
                            result.getLong(8),
                            result.getString("status"),
                            deletionDate,
                            archiveDate),
                            result.getString("changedBy"),
                            result.getString("deletedBy")
            );*/
            Treatment treatment = new Treatment(
                    result.getLong("tid"),
                    result.getLong("pid"),
                    date,
                    begin,
                    end,
                    result.getString("description"),
                    result.getString("remark"),
                    result.getLong("nid"),
                    result.getString("status"),
                    deletionDate,
                    archiveDate,
                    result.getString("changedBy"),
                    result.getString("deletedBy")
            );
            list.add(treatment);
        }
        return list;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query all treatments of a patient with a given patient id (pid).
     *
     * @param pid Patient id to query all treatments referencing this id.
     * @return <code>PreparedStatement</code> to query all treatments of the given patient id (pid).
     */
    private PreparedStatement getReadAllTreatmentsOfOnePatientByPid(long pid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM treatment WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, pid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Queries all treatments of a given patient id (pid) and maps the results to an <code>ArrayList</code> with
     * objects of class <code>Treatment</code>.
     *
     * @param pid Patient id to query all treatments referencing this id.
     * @return <code>ArrayList</code> with objects of class <code>Treatment</code> of all rows in the
     * <code>ResultSet</code>.
     */
    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ResultSet result = getReadAllTreatmentsOfOnePatientByPid(pid).executeQuery();
        return getListFromResultSet(result);
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given treatment, identified
     * by the id of the treatment (tid).
     *
     * @param treatment Treatment object to update.
     * @return <code>PreparedStatement</code> to update the given treatment.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Treatment treatment) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL =
                    "UPDATE treatment SET " + "pid = ?, " + "treatment_date = ?, " + "begin = ?, " + "end = ?, " +
                            "description = ?, " + "remark = ?, " + "nid = ?," +"status = ?, "+"deletionDate = ?,"+
                            "archiveDate=?, changedBy = ?, deletedBy = ?" + "WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, treatment.getPid());
            preparedStatement.setString(2, treatment.getDate());
            preparedStatement.setString(3, treatment.getBegin());
            preparedStatement.setString(4, treatment.getEnd());
            preparedStatement.setString(5, treatment.getDescription());
            preparedStatement.setString(6, treatment.getRemarks());
            preparedStatement.setLong(7, treatment.getNid());
            preparedStatement.setString(8, treatment.getStatus());

            if (treatment.getDeletionDate() != null) {
                preparedStatement.setDate(9, java.sql.Date.valueOf(treatment.getDeletionDate()));
            } else {
                preparedStatement.setNull(9, java.sql.Types.DATE);
            }

            if (treatment.getArchiveDate() != null) {
                preparedStatement.setDate(10, java.sql.Date.valueOf(treatment.getArchiveDate()));
            } else {
                preparedStatement.setNull(10, java.sql.Types.DATE);
            }
            preparedStatement.setString(11, treatment.getChangedBy());
            preparedStatement.setString(12, treatment.getDeletedBy());

            preparedStatement.setLong(13, treatment.getTid()); // → WHERE tid = ?
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a treatment with the given id.
     *
     * @param tid Id of the Treatment to delete.
     * @return <code>PreparedStatement</code> to delete treatment with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long tid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM treatment WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, tid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getDeactivateStatement(long tid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE treatment SET active = 'i' WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, tid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
    /**
     * Generates a {@link PreparedStatement} to set the deletion and archive date
     * of a treatment identified by its ID.
     * The deletion date is set 10 years into the future,
     * while the archive date is set to the current date.
     *
     * @param tid ID of the treatment to update
     * @return the prepared statement to update the treatment dates
     */
    @Override
    protected PreparedStatement setDeleteDateStatement(long tid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE treatment SET deletionDate = ?, archiveDate = ? WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now().plusYears(10))); // deletion date
            preparedStatement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));               // archive date
            preparedStatement.setLong(3, tid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Deletes all treatments from the database whose deletion date has passed,
     * are no longer active, and have a non-null deletion date.
     * This is a hard delete operation.
     *
     * @throws SQLException if an error occurs during execution
     */
    public void deleteExpiredTreatments() throws SQLException {
        final String SQL = "DELETE FROM treatment WHERE deletionDate IS NOT NULL AND deletionDate <= ? AND status != ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(SQL)) {
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(2, Treatment.STATUS_ACTIVE);

            int deleted = stmt.executeUpdate();
            System.out.println("Number of deleted inactive treatments with expired deletion date: " + deleted);
        }
    }





}