package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Patient extends Person {
    private SimpleLongProperty pid;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty careLevel;
    private final SimpleStringProperty roomNumber;
    //   private final SimpleStringProperty assets;
    private final List<Treatment> allTreatments = new ArrayList<>();
    private SimpleStringProperty status;
    public static final String STATUS_ACTIVE = "a";
    public static final String STATUS_INACTIVE = "i";
    private final ObjectProperty<LocalDate> deletionDate = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalDate> archiveDate = new SimpleObjectProperty<>(null);
    private final SimpleStringProperty changedBy = new SimpleStringProperty();
    private final SimpleStringProperty deletedBy = new SimpleStringProperty();

    /**
     * Constructor to initiate an object of class <code>Patient</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a patient id (pid).
     *
     * @param firstName   First name of the patient.
     * @param surname     Last name of the patient.
     * @param dateOfBirth Date of birth of the patient.
     * @param careLevel   Care level of the patient.
     * @param roomNumber  Room number of the patient.
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber,
                   String status, LocalDate deletionDate, LocalDate archiveDate, String changedBy, String deletedBy) {
        super(firstName, surname);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.status = new SimpleStringProperty(status);
        this.deletionDate.set(deletionDate);
        this.archiveDate.set(archiveDate);
        this.changedBy.set(changedBy);
        this.deletedBy.set(deletedBy);
    }

    /**
     * Constructor to initiate an object of class <code>Patient</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a patient id (pid).
     *
     * @param pid         Patient id.
     * @param firstName   First name of the patient.
     * @param surname     Last name of the patient.
     * @param dateOfBirth Date of birth of the patient.
     * @param careLevel   Care level of the patient.
     * @param roomNumber  Room number of the patient.
     */
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel,
                   String roomNumber, String status, LocalDate deletionDate, LocalDate archiveDate, String changedBy, String deletedBy) {
        super(firstName, surname);
        this.pid = new SimpleLongProperty(pid);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.status = new SimpleStringProperty(status);
        this.deletionDate.set(deletionDate);
        this.archiveDate.set(archiveDate);
        this.changedBy.set(changedBy);
        this.deletedBy.set(deletedBy);
    }

    public long getPid() {
        return pid.get();
    }

    public SimpleLongProperty pidProperty() {
        return pid;
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    /**
     * Stores the given string as new <code>birthOfDate</code>.
     *
     * @param dateOfBirth as string in the following format: YYYY-MM-DD.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getCareLevel() {
        return careLevel.get();
    }

    public SimpleStringProperty careLevelProperty() {
        return careLevel;
    }

    public void setCareLevel(String careLevel) {
        this.careLevel.set(careLevel);
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }


    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
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
    public String getChangedBy() {
        return changedBy.get();
    }

    public void setChangedBy(String changedBy) {
        this.changedBy.set(changedBy);
    }

    public SimpleStringProperty changedByProperty() {
        return changedBy;
    }

    public String getDeletedBy() {
        return deletedBy.get();
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy.set(deletedBy);
    }

    public SimpleStringProperty deletedByProperty() {
        return deletedBy;
    }

    /**
     * Adds a treatment to the list of treatments, if the list does not already contain the treatment.
     *
     * @param treatment Treatment to add.
     * @return False, if the treatment was already part of the list, else true.
     */
    public boolean add(Treatment treatment) {
        if (this.allTreatments.contains(treatment)) {
            return false;
        }
        this.allTreatments.add(treatment);
        return true;
    }

    public String toString() {
        return "Patient" + "\nMNID: " + this.pid + "\nFirstname: " + this.getFirstName() + "\nSurname: " +
                this.getSurname() + "\nBirthday: " + this.dateOfBirth + "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomNumber + "\nStatus: " + this.status + "\nDatum gelöscht: " +
                this.deletionDate + "\nDatum archiviert: " + this.archiveDate + "\n"  + this.changedBy +
                "\nGelöscht von: " + this.deletedBy + "\n";
    }
    /**
     * Marks the patient for future deletion by setting the archive date to the current date
     * and the deletion date to 10 years from now. Optionally sets the status to inactive.
     * This method is typically used for soft-deletion workflows.
     */
    public void markForDeletion() {
        this.status.set(STATUS_INACTIVE); // optional
        System.out.println(status);
        this.archiveDate.set(LocalDate.now());
        System.out.println(archiveDate);
        this.deletionDate.set(LocalDate.now().plusYears(10));
        System.out.println(deletionDate);



    }



}