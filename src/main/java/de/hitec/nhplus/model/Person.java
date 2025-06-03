package de.hitec.nhplus.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Abstract base class representing a person with a first name and surname.
 * This class uses JavaFX properties for data binding in UI applications.
 */

public abstract class Person {

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty surname;

    /**
     * Constructor initializing the person's first name and surname.
     * @param firstName The person's first name
     * @param surname   The person's surname
     */
    public Person(String firstName, String surname) {
        this.firstName = new SimpleStringProperty(firstName);
        this.surname = new SimpleStringProperty(surname);
    }
    /**
     * Getter and Setter Methods
     */

    public String getFirstName() {
        return firstName.get();
    }


    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }


    public String getSurname() {
        return surname.get();
    }


    public SimpleStringProperty surnameProperty() {
        return surname;
    }


    public void setSurname(String surname) {
        this.surname.set(surname);
    }
}
