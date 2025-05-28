package de.hitec.nhplus.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    private final ObjectProperty<LocalDate> deletionDate = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalDate> archiveDate = new SimpleObjectProperty<>(null);


    /**
     * Constructor to initiate an object of class <code>Nurse</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a nurse id (nid).
     *
     * @param firstName   First name of the nurse.
     * @param surname     Last name of the nurse.
     * @param phoneNumber phone Number of the nurse.
     */
    public Nurse(String firstName, String surname, String phoneNumber, String status,
                 LocalDate deletionDate, LocalDate archiveDate) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(); // leer – DB vergibt ID später
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.status = new SimpleStringProperty(status);
        this.deletionDate.set(deletionDate);
        this.archiveDate.set(archiveDate);
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
    public Nurse(long nid, String firstName, String surname, String phoneNumber, String status,
                 LocalDate deletionDate, LocalDate archiveDate) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(nid);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.status = new SimpleStringProperty(status);
        this.deletionDate.set(deletionDate);
        this.archiveDate.set(archiveDate);
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

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getStatus() {
        return status.get();
    }

    public LocalDate getDeletionDate() {
        return deletionDate.get();
    }

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate.set(deletionDate);
    }


    public LocalDate getArchiveDate() {
        return archiveDate.get();
    }

    public void setArchiveDate(LocalDate archiveDate) {
        this.archiveDate.set(archiveDate);
    }

    public String toString() {
        return "Pfleger/in" + "\nNID: " + this.nid + "\nFirstname: " + this.getFirstName() + "\nSurname: " +
                this.getSurname() + "\nPhoneNumber: " + this.phoneNumber + "\nStatus: " + this.status +
                "\nDatum gelöscht: " + this.deletionDate +"\nDatum archiviert: " + this.archiveDate +"\n";
    }


    public void markForDeletion() {
        this.status.set(STATUS_INACTIVE); // optional
        this.archiveDate.set(LocalDate.now());
        this.deletionDate.set(LocalDate.now().plusYears(10));

    }


}



