package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.*;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static de.hitec.nhplus.utils.DateConverter.convertStringToLocalDate;
import static de.hitec.nhplus.utils.DateConverter.convertStringToLocalTime;

/**
 * Call static class provides to static methods to set up and wipe the database. It uses the class ConnectionBuilder
 * and its path to build up the connection to the database. The class is executable. Executing the class will build
 * up a connection to the database and calls setUpDb() to wipe the database, build up a clean database and fill the
 * database with some test data.
 */
public class SetUpDB {

    /**
     * This method wipes the database by dropping the tables. Then the method calls DDL statements to build it up from
     * scratch and DML statements to fill the database with hard coded test data.
     */
    public static void setUpDb() {
        Connection connection = ConnectionBuilder.getConnection();
        SetUpDB.wipeDb(connection);
        SetUpDB.setUpTablePatient(connection);
        SetUpDB.setUpTableNurse(connection);
        SetUpDB.setUpTableUser(connection);
        SetUpDB.setUpTableTreatment(connection);
        SetUpDB.setUpNurse();
        SetUpDB.setUpUsers();
        SetUpDB.setUpPatients();
        SetUpDB.setUpTreatments();


    }

    /**
     * This method wipes the database by dropping the tables.
     */
    public static void wipeDb(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE treatment");
            statement.execute("DROP TABLE patient");
            statement.execute("DROP TABLE nurse");
            statement.execute("DROP TABLE user");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void setUpTablePatient(Connection connection) {

        final String SQL = "CREATE TABLE IF NOT EXISTS patient (" + "   pid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "   firstname TEXT NOT NULL, " + "   surname TEXT NOT NULL, " + "   dateOfBirth TEXT NOT NULL, " +
                "   carelevel TEXT NOT NULL, " + "   roomnumber TEXT NOT NULL, " + "status TEXT NOT NULL," +
                "deletionDate DATE," + "archiveDate DATE," + "changedBy TEXT," +
                "            deletedBy TEXT);";

        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void setUpTableNurse(Connection connection) {
        final String SQL = "CREATE TABLE IF NOT EXISTS nurse (" + "   nid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "   firstname TEXT NOT NULL, " + "   surname TEXT NOT NULL, " + "  phonenumber TEXT NOT NULL, " +
                "status TEXT NOT NULL,"+"deletionDate DATE, "+"archiveDate DATE,"+"changedBy TEXT," +
                "            deletedBy TEXT);";
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL);

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }


    private static void setUpTableTreatment(Connection connection) {
        final String SQL = """
    CREATE TABLE IF NOT EXISTS treatment (
        tid INTEGER PRIMARY KEY AUTOINCREMENT,
        pid INTEGER NOT NULL,
        treatment_date TEXT NOT NULL,
        begin TEXT NOT NULL,
        end TEXT NOT NULL,
        description TEXT NOT NULL,
        remark TEXT NOT NULL,
        nid INTEGER NOT NULL,
        status TEXT NOT NULL,
        deletionDate DATE,
        archiveDate DATE,
        changedBy TEXT,
        deletedBy TEXT,
        FOREIGN KEY (pid) REFERENCES patient (pid) ON DELETE CASCADE,
        FOREIGN KEY (nid) REFERENCES nurse (nid) ON DELETE CASCADE
    );
""";


        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL);

        } catch (SQLException exception) {

        }
    }

    private static void setUpTableUser(Connection connection) {
        final String SQL = "CREATE TABLE IF NOT EXISTS user (" +

                "   firstname TEXT NOT NULL, " +
                "   surname TEXT NOT NULL, " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "   username TEXT UNIQUE NOT NULL, " +
                "   password_hash TEXT NOT NULL," +
                "   role TEXT NOT NULL" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL);
        } catch (SQLException exception) {

        }
    }

    private static void setUpPatients() {
        try {
            PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();

            dao.create(new Patient("Seppl", "Herberger", convertStringToLocalDate("1945-12-01"), "4", "202",
                    Patient.STATUS_ACTIVE, null, null,null,null));
            dao.create(new Patient("Martina", "Gerdsen", convertStringToLocalDate("1954-08-12"), "5", "010",
                    Patient.STATUS_ACTIVE, null, null,null,null));
            dao.create(new Patient("Gertrud", "Franzen", convertStringToLocalDate("1949-04-16"), "3", "002",
                    Patient.STATUS_ACTIVE, null, null,null,null));
            dao.create(new Patient("Ahmet", "Yilmaz", convertStringToLocalDate("1941-02-22"), "3", "013",
                    Patient.STATUS_ACTIVE, null, null,null,null));
            dao.create(new Patient("Hans", "Neumann", convertStringToLocalDate("1955-12-12"), "2", "001",
                    Patient.STATUS_ACTIVE, null, null,null,null));
            dao.create(new Patient("Elisabeth", "Müller", convertStringToLocalDate("1958-03-07"), "5", "110",
                    Patient.STATUS_ACTIVE, null, null,null,null));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void setUpNurse() {
        try {
            NurseDao dao = DaoFactory.getDaoFactory().createNurseDAO();
            dao.create(new Nurse("Maria", "Schmidt", "020393", Nurse.STATUS_ACTIVE, null, null, null, null));
            dao.create(new Nurse("Michael", "Berg", "0203931", Nurse.STATUS_ACTIVE, null, null, null, null));
            dao.create(new Nurse("Lin", "Park", "0203932", Nurse.STATUS_ACTIVE, null, null, null, null));
            dao.create(new Nurse("James", "Ross", "0203933", Nurse.STATUS_ACTIVE, null, null, null, null));
            dao.create(new Nurse("Anna", "Suarez", "0203933", Nurse.STATUS_ACTIVE, null, null, null, null));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void setUpTreatments() {
        try {
            TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
            dao.create(new Treatment(1, 1, convertStringToLocalDate("2023-06-03"), convertStringToLocalTime("11:00"),
                    convertStringToLocalTime("15:00"), "Gespräch",
                    "Der Patient hat enorme Angstgefühle und glaubt, er sei überfallen worden. Ihm seien alle " +
                            "Wertsachen gestohlen worden.\nPatient beruhigt sich erst, als alle Wertsachen im Zimmer " +
                            "gefunden worden sind.", 1,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(2, 1, convertStringToLocalDate("2023-06-05"), convertStringToLocalTime("11:00"),
                    convertStringToLocalTime("12:30"), "Gespräch",
                    "Patient irrt auf der Suche nach gestohlenen Wertsachen durch die Etage und bezichtigt andere " +
                            "Bewohner des Diebstahls.\nPatient wird in seinen Raum zurückbegleitet und erhält " +
                            "Beruhigungsmittel.", 2,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(3, 2, convertStringToLocalDate("2023-06-04"), convertStringToLocalTime("07:30"),
                    convertStringToLocalTime("08:00"), "Waschen",
                    "Patient mit Waschlappen gewaschen und frisch angezogen. Patient gewendet.", 1,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(4, 1, convertStringToLocalDate("2023-06-06"), convertStringToLocalTime("15:10"),
                    convertStringToLocalTime("16:00"), "Spaziergang",
                    "Spaziergang im Park, Patient döst  im Rollstuhl ein", 2,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(8, 1, convertStringToLocalDate("2023-06-08"), convertStringToLocalTime("15:00"),
                    convertStringToLocalTime("16:00"), "Spaziergang",
                    "Parkspaziergang; Patient ist heute lebhafter und hat klare Momente; erzählt von seiner Tochter",
                    2,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(9, 2, convertStringToLocalDate("2023-06-07"), convertStringToLocalTime("11:00"),
                    convertStringToLocalTime("11:30"), "Waschen",
                    "Waschen per Dusche auf einem Stuhl; Patientin gewendet;", 4,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(12, 5, convertStringToLocalDate("2023-06-08"), convertStringToLocalTime("15:00"),
                    convertStringToLocalTime("15:30"), "Physiotherapie",
                    "Übungen zur Stabilisation und Mobilisierung der Rückenmuskulatur", 1,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(14, 4, convertStringToLocalDate("2023-08-24"), convertStringToLocalTime("09:30"),
                    convertStringToLocalTime("10:15"), "KG", "Lympfdrainage", 2,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(16, 6, convertStringToLocalDate("2023-08-31"), convertStringToLocalTime("13:30"),
                    convertStringToLocalTime("13:45"), "Toilettengang",
                    "Hilfe beim Toilettengang; Patientin klagt über Schmerzen beim Stuhlgang. Gabe von Iberogast", 1,Treatment.STATUS_ACTIVE,null,null,null,null));
            dao.create(new Treatment(17, 6, convertStringToLocalDate("2023-09-01"), convertStringToLocalTime("16:00"),
                    convertStringToLocalTime("17:00"), "KG",
                    "Massage der Extremitäten zur Verbesserung der Durchblutung", 2,Treatment.STATUS_ACTIVE,null,null,null,null));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    private static void setUpUsers() {
        try {
            UserDao dao = DaoFactory.getDaoFactory().createUserDAO();
            dao.create(new User( 1, "Udo", "Mann", "u.mann", "63aeaae0d2d1431ffdb360cafadaed6b9b5cf6046d214d6edd90c1ed074ff6f1", "admin"));
            dao.create(new User( 2, "Anna","Suarez", "a.suarez", "4a38ceb86c5763336babff235a63cf482cbbb9c59b151d39a355cb21ef12be70", "nurse"));
            dao.create(new User( 3, "Lin","Park", "l.park", "9ca0fa116392c9e5338856285a4d79b59ca6657ec2104553db81496b0a3bca0a", "nurse"));
            dao.create(new User( 4, "James","Ross", "j.ross", "9ca0fa116392c9e5338856285a4d79b59ca6657ec2104553db81496b0a3bca0a", "nurse"));
            dao.create(new User( 5, "Michael","Berg", "m.berg", "9ca0fa116392c9e5338856285a4d79b59ca6657ec2104553db81496b0a3bca0a", "nurse"));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SetUpDB.setUpDb();

    }
}
