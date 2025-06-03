package de.hitec.nhplus.datastorage;

/**
 * Factory class for creating DAO instances with a shared database connection.
 * <p>
 * Implements the Singleton pattern to ensure that only one instance of the factory exists.
 */
public class DaoFactory {

    private static DaoFactory instance;

    /**
     * Private constructor to prevent instantiation from outside.
     * Use {@link #getDaoFactory()} to obtain the singleton instance.
     */
    private DaoFactory() {
    }

    /**
     * Returns the singleton instance of the {@code DaoFactory}.
     *
     * @return the singleton instance of the factory
     */
    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }


        return DaoFactory.instance;
    }

    /**
     * Creates a new instance of {@link TreatmentDao} using a shared database connection.
     *
     * @return a new {@code TreatmentDao} instance
     */
    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a new instance of {@link PatientDao} using a shared database connection.
     *
     * @return a new {@code PatientDao} instance
     */
    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a new instance of {@link NurseDao} using a shared database connection.
     *
     * @return a new {@code NurseDao} instance
     */
    public NurseDao createNurseDAO() {
        return new NurseDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates a new instance of {@link UserDao} using a shared database connection.
     *
     * @return a new {@code UserDao} instance
     */
    public UserDao createUserDAO() {
        return new UserDao(ConnectionBuilder.getConnection());
    }
}