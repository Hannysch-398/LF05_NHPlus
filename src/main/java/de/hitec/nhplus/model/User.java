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
     * Getter and Setter Methods
     */

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getUsername() {
        return username.get();
    }


    public SimpleStringProperty usernameProperty() {
        return username;
    }


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


    public SimpleStringProperty passwordProperty() {
        return password;
    }


    public void setPassword(String plainPassword) {
        this.password.set(PasswordUtil.hashPassword(plainPassword));
    }


    public String getRole() {
        return role;
    }


    public void setAdmin(String role) {
        this.role = role;
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





}
