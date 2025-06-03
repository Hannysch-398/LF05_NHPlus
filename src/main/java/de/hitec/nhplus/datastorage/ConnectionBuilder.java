package de.hitec.nhplus.datastorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

/**
 * Utility class for managing the SQLite database connection.
 * <p>
 * Provides a singleton-style access to a shared database connection
 * and ensures that foreign key constraints are enforced.
 */
public class ConnectionBuilder {

    private static final String DB_NAME = "nursingHome.db";
    private static final String URL = "jdbc:sqlite:db/" + DB_NAME;

    private static Connection connection;

    /**
     * Returns a shared {@link Connection} instance to the SQLite database.
     * <p>
     * If the connection does not yet exist, it is initialized with
     * foreign key constraint enforcement enabled.
     *
     * @return the active {@link Connection} instance, or null if an error occurred
     */
    synchronized public static Connection getConnection() {
        try {
            if (ConnectionBuilder.connection == null) {
                SQLiteConfig configuration = new SQLiteConfig();
                configuration.enforceForeignKeys(true);
                ConnectionBuilder.connection = DriverManager.getConnection(URL, configuration.toProperties());
            }
        } catch (SQLException exception) {
            System.out.println("Verbindung zur Datenbank konnte nicht aufgebaut werden!");
            exception.printStackTrace();
        }
        return ConnectionBuilder.connection;
    }

    /**
     * Closes the current database connection if it exists.
     * <p>
     * After closing, the connection is set to null to allow for reinitialization.
     */
    synchronized public static void closeConnection() {
        try {
            if (ConnectionBuilder.connection != null) {
                ConnectionBuilder.connection.close();
                ConnectionBuilder.connection = null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
