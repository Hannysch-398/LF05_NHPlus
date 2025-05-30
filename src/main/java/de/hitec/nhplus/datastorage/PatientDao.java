package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Implements the Interface <code>DaoImp</code>. Overrides methods to generate specific <code>PreparedStatements</code>,
 * to execute the specific SQL Statements.
 */
public class PatientDao extends DaoImp<Patient> {

    /**
     * The constructor initiates an object of <code>PatientDao</code> and passes the connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the SQL-statements.
     */
    public PatientDao(Connection connection) {
        super(connection);
    }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Patient</code>.
     *
     * @param patient Object of <code>Patient</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given patient.
     */
    @Override
    protected PreparedStatement getCreateStatement(Patient patient) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO patient (firstname, surname, dateOfBirth, carelevel, roomnumber, status," +
                    "deletionDate, archiveDate, changedBy, deletedBy) " +
                    "VALUES (?, ?, ?, ?, ? ,? , ? , ? ,?,?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, patient.getFirstName());
            preparedStatement.setString(2, patient.getSurname());
            preparedStatement.setString(3, patient.getDateOfBirth());
            preparedStatement.setString(4, patient.getCareLevel());
            preparedStatement.setString(5, patient.getRoomNumber());
            preparedStatement.setString(6, patient.getStatus());

            if (patient.getDeletionDate() != null) {
                System.out.println("insert spalte 7");
                preparedStatement.setDate(7, java.sql.Date.valueOf(patient.getDeletionDate()));
            } else {
                System.out.println("insert spalte 71");
                preparedStatement.setNull(7, java.sql.Types.DATE);
            }
            if (patient.getArchiveDate() != null) {
                System.out.println("insert spalte 8");
                preparedStatement.setDate(8, java.sql.Date.valueOf(patient.getArchiveDate()));
            } else {
                System.out.println("insert spalte 81");
                preparedStatement.setNull(8, java.sql.Types.DATE);
            }
            preparedStatement.setString(9, patient.getChangedBy());
            preparedStatement.setString(10, patient.getDeletedBy());

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a patient by a given patient id (pid).
     *
     * @param pid Patient id to query.
     * @return <code>PreparedStatement</code> to query the patient.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long pid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM patient WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, pid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Maps a <code>ResultSet</code> of one patient to an object of <code>Patient</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Patient</code>.
     * @return Object of class <code>Patient</code> with the data from the resultSet.
     */
    @Override
    protected Patient getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate deletionDate = null;
        if (result.getDate("deletionDate") != null) {
            deletionDate = result.getDate("deletionDate").toLocalDate();
        }

        LocalDate archiveDate = null;
        if (result.getDate("archiveDate") != null) {
            archiveDate = result.getDate("archiveDate").toLocalDate();
        }
        return new Patient(result.getInt(1), result.getString("firstname"), result.getString("surname"),
                DateConverter.convertStringToLocalDate(result.getString(4)), result.getString("careLevel"),
                result.getString("roomnumber"), result.getString("status"),deletionDate,archiveDate,result.getString("changedBy"),
                result.getString("deletedBy") );
    }

    /**
     * Generates a <code>PreparedStatement</code> to query all patients.
     *
     * @return <code>PreparedStatement</code> to query all patients.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM patient WHERE status = ?";
            statement = this.connection.prepareStatement(SQL);
            statement.setString(1, Patient.STATUS_ACTIVE);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    /**
     * Maps a <code>ResultSet</code> of all patients to an <code>ArrayList</code> of <code>Patient</code> objects.
     *
     * @param result ResultSet with all rows. The Columns will be mapped to objects of class <code>Patient</code>.
     * @return <code>ArrayList</code> with objects of class <code>Patient</code> of all rows in the
     * <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Patient> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Patient> list = new ArrayList<>();
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

            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));

            /*Patient patient =
                    new Patient((result.getInt(1)),
                            result.getString("firstname"),
                            result.getString("surname"),
                            DateConverter.convertStringToLocalDate(result.getString("dateOfBirth")),
                            result.getString("careLevel"),
                            result.getString("roomnumber"),
                            result.getString("status"),
                            deletionDate,archiveDate )
                            result.getString("changedBy"),
                            result.getString("deletedBy")
            );*/
            Patient patient = new Patient(
                    result.getInt("pid"),
                    result.getString("firstname"),
                    result.getString("surname"),
                    DateConverter.convertStringToLocalDate(result.getString("dateOfBirth")),
                    result.getString("careLevel"),
                    result.getString("roomnumber"),
                    result.getString("status"),
                    deletionDate,
                    archiveDate,
                    result.getString("changedBy"),
                    result.getString("deletedBy")
            );
            list.add(patient);
        }
        return list;
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given patient, identified
     * by the id of the patient (pid).
     *
     * @param patient Patient object to update.
     * @return <code>PreparedStatement</code> to update the given patient.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Patient patient) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE patient SET " + "firstname = ?, " + "surname = ?, " + "dateOfBirth = ?, " +
                    "carelevel = ?, " + "roomnumber = ?, "+ "status = ?,"+"deletionDate = ?,"+"archiveDate= ? , changedBy = ?, deletedBy = ?" +
                    "WHERE pid = ?";
            /* +
                    "assets = ? "*/
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, patient.getFirstName());
            preparedStatement.setString(2, patient.getSurname());
            preparedStatement.setString(3, patient.getDateOfBirth());
            preparedStatement.setString(4, patient.getCareLevel());
            preparedStatement.setString(5, patient.getRoomNumber());
            preparedStatement.setString(6, patient.getStatus());


            if (patient.getDeletionDate() != null) {
                preparedStatement.setDate(7, java.sql.Date.valueOf(patient.getDeletionDate()));
            } else {
                preparedStatement.setNull(7, java.sql.Types.DATE);
            }

            if (patient.getArchiveDate() != null) {
                preparedStatement.setDate(8, java.sql.Date.valueOf(patient.getArchiveDate()));
            } else {
                preparedStatement.setNull(8, java.sql.Types.DATE);
            }
            preparedStatement.setString(9, patient.getChangedBy());
            preparedStatement.setString(10, patient.getDeletedBy());


            preparedStatement.setLong(11, patient.getPid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a patient with the given id.
     *
     * @param pid Id of the patient to delete.
     * @return <code>PreparedStatement</code> to delete patient with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long pid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM patient WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, pid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getDeactivateStatement(long pid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE patient SET active = 'i' WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, pid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement setDeleteDateStatement(long pid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE patient SET deletionDate = ?, archiveDate = ? WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now().plusYears(10))); // Löschdatum
            preparedStatement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));               // Archivdatum
            preparedStatement.setLong(3, pid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
    public void deleteExpiredPatient() throws SQLException {
        final String SQL = "DELETE FROM patient " + "WHERE deletionDate IS NOT NULL " + "AND deletionDate <= ? " +
                "AND status != ?";  // Nur wenn NICHT aktiv

        try (PreparedStatement stmt = this.connection.prepareStatement(SQL)) {
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(2, Patient.STATUS_ACTIVE);

            int deleted = stmt.executeUpdate();
            System.out.println("Anzahl gelöschter inaktiver Patienten mit abgelaufenem Löschdatum: " + deleted);
        }
    }



}
