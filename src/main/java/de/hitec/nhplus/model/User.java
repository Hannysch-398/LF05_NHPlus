package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.PasswordUtil;
import javafx.beans.property.SimpleStringProperty;

public class User extends Person{
    private long id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;



    public User(String firstName, String surname, long id, String username, String plainPassword) {

        super(firstName, surname);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(PasswordUtil.hashPassword(plainPassword));
    }
    // Für Benutzer aus der Datenbank (mit ID und bereits gehashtem Passwort)
    public User(long id, String firstName, String surname, String username, String passwordHash) {
        super(firstName, surname);
        this.id = id;
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(passwordHash);
    }

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

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String plainPassword) {
        this.password.set(PasswordUtil.hashPassword(plainPassword));
    }

    public boolean checkPassword(String plainPassword) {
        return PasswordUtil.hashPassword(plainPassword).equals(getPassword());
    }
    /*
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty getPasswordHash() { return passwordHash; }

    public SimpleStringProperty passwordProperty() {
        return passwordHash;
    }

    public void setPasswordHash(SimpleStringProperty passwordHash) { this.passwordHash = passwordHash; }

    public boolean checkPassword(String plainPassword) {
        return PasswordUtil.hashPassword(plainPassword).equals(getPassword());
    }*/
}
