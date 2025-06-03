package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.PasswordUtil;
import javafx.beans.property.SimpleStringProperty;

public class User extends Person {
    private long id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private String role;

    /**
     * Constructor for creating a user object without a specified role.
     * Use this for login or temporary user data that doesn't require role-based logic.
     *
     * @param firstName   First name of the user.
     * @param surname     Last name of the user.
     * @param id          User ID.
     * @param username    Username used for login.
     * @param passwordHash Hashed password for authentication.
     */


    public User(String firstName, String surname, long id, String username, String passwordHash) {

        super(firstName, surname);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(passwordHash);
    }

    /**
     * Constructor for creating a fully defined user with role and credentials.
     * Typically used when loading from persistent storage or assigning roles.
     *
     * @param id           User ID.
     * @param firstName    First name.
     * @param surname      Last name.
     * @param username     Login name.
     * @param passwordHash Hashed password string.
     * @param role         Role of the user (e.g., "admin", "nurse").
     */

    public User(long id, String firstName, String surname, String username, String passwordHash, String role) {
        super(firstName, surname);
        this.id = id;
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(passwordHash);
        this.role = role;
    }

    /**
     * Returns the ID of the user.
     *
     * @return ID of the user.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id ID to assign to the user.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the username of the user.
     *
     * @return Username as a <code>String</code>.
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Returns the JavaFX property for the username. Useful for bindings.
     *
     * @return <code>SimpleStringProperty</code> representing the username.
     */
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username Username to assign.
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return Hashed password as a <code>String</code>.
     */
    public String getPassword() {
        return password.get();
    }


    /**
     * Returns the JavaFX property for the password. Useful for bindings.
     *
     * @return <code>SimpleStringProperty</code> representing the password hash.
     */
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    /**
     * Sets the password for the user. The password will be hashed using <code>PasswordUtil</code>.
     *
     * @param plainPassword The plain text password to hash and store.
     */
    public void setPassword(String plainPassword) {
        this.password.set(PasswordUtil.hashPassword(plainPassword));
    }

    /**
     * Checks if the given plain text password matches the stored password hash.
     *
     * @param plainPassword The plain text password to check.
     * @return <code>true</code> if the password matches, otherwise <code>false</code>.
     */
    public boolean checkPassword(String plainPassword) {
        return PasswordUtil.hashPassword(plainPassword).equals(getPassword());
    }

    /**
     * Returns the role of the user (e.g., admin, nurse, etc.).
     *
     * @return Role as a <code>String</code>.
     */

    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role Role to assign.
     */
    public void setAdmin(String role) {
        this.role = role;
    }

}
