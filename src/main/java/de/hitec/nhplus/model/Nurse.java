package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Date;

/**
 * Nurses work in a Nursing home and treat patients.
 */
public class Nurse extends Person {

    private SimpleLongProperty nid;
    private final SimpleStringProperty phoneNumber;
    private SimpleStringProperty status;
    public static final String STATUS_ACTIVE = "a";
    public static final String STATUS_INACTIVE = "i";
    public LocalDate deletionDate;

    /**
     * Constructor to initiate an object of class <code>Nurse</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a nurse id (nid).
     *
     * @param firstName   First name of the nurse.
     * @param surname     Last name of the nurse.
     * @param phoneNumber phone Number of the nurse.
     */
    public Nurse(String firstName, String surname, String phoneNumber, String status, Date deletionDate) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(0);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.status = new SimpleStringProperty(status);
        this.deletionDate = null;
    }

    public Nurse(String firstName, String surname, String phoneNumber, String status) {
        this(firstName, surname, phoneNumber, status, null);
    }

    /**
     * Constructor to initiate an object of class <code>Nurse</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a nurse id (nid).
     *
     * @param nid         nurse id.
     * @param firstName   First name of the nurse.
     * @param surname     Last name of the nurse.
     * @param phoneNumber phoneNumber of the nurse
     */
    public Nurse(long nid, String firstName, String surname, String phoneNumber, String status) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(nid);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.status = new SimpleStringProperty(status);
    }

    public long getNid() {
        return (nid != null) ? nid.get() : 0;
    }

    public SimpleLongProperty nidProperty() {
        return nid;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String toString() {
        return "Pfleger/in" + "\nNID: " + this.nid + "\nFirstname: " + this.getFirstName() + "\nSurname: " +
                this.getSurname() + "\nPhoneNumber: " + this.phoneNumber + "\n";
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getStatus() {
        return status.get();
    }
    public LocalDate getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate = deletionDate;
    }


    public void markForDeletion() {
        this.status.set(STATUS_INACTIVE); // optional
        this.deletionDate = LocalDate.now().plusYears(10);
    }




}



