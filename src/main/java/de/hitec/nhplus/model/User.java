package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalTime;
public class User extends Person{
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;



    public User(String firstName, String surname, String username, String password) {
        super(firstName, surname);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }
}
