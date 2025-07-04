package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.nio.channels.ClosedChannelException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long tid;
    private final long pid;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private String description;
    private String remarks;
    private long nid;
    private String nurseName;
    private SimpleStringProperty status;
    public static final String STATUS_ACTIVE = "a";
    public static final String STATUS_INACTIVE = "i";
    private final ObjectProperty<LocalDate> deletionDate = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalDate> archiveDate = new SimpleObjectProperty<>(null);
    private final SimpleStringProperty changedBy = new SimpleStringProperty();
    private final SimpleStringProperty deletedBy = new SimpleStringProperty();

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }
    /**
     * Constructor to initiate an object of class <code>Treatment</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a treatment id (tid).
     *
     * @param pid         Id of the treated patient.
     * @param date        Date of the Treatment.
     * @param begin       Time of the start of the treatment in format "hh:MM"
     * @param end         Time of the end of the treatment in format "hh:MM".
     * @param description Description of the treatment.
     * @param remarks     Remarks to the treatment.
     * @param nid         Id of the nurse that did the treatment.
     */
    public Treatment(long pid, LocalDate date, LocalTime begin, LocalTime end, String description, String remarks,
                     long nid, String status, LocalDate deletionDate, LocalDate archiveDate, String changedBy, String deletedBy) {
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.nid = nid;
        this.status = new SimpleStringProperty(status);
        this.deletionDate.set(deletionDate);
        this.archiveDate.set(archiveDate);
        this.changedBy.set(changedBy);
        this.deletedBy.set(deletedBy);
    }

    /**
     * Constructor to initiate an object of class <code>Treatment</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a treatment id (tid).
     *
     * @param tid         Id of the treatment.
     * @param pid         Id of the treated patient.
     * @param date        Date of the Treatment.
     * @param begin       Time of the start of the treatment in format "hh:MM"
     * @param end         Time of the end of the treatment in format "hh:MM".
     * @param description Description of the treatment.
     * @param remarks     Remarks to the treatment.
     * @param nid         Id of the nurse that did the treatment
     */
    public Treatment(long tid, long pid, LocalDate date, LocalTime begin, LocalTime end, String description,
                     String remarks, long nid, String status, LocalDate deletionDate, LocalDate archiveDate, String changedBy, String deletedBy) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.nid = nid;
        this.status = new SimpleStringProperty(status);
        this.changedBy.set(changedBy);
        this.deletedBy.set(deletedBy);
        this.deletionDate.set(deletionDate);
        this.archiveDate.set(archiveDate);
    }

    /**
     * Getter and Setter Methods
     */

    public long getTid() {
        return tid;
    }

    public long getPid() {
        return this.pid;
    }

    public long getNid() {
        return nid;
    }

    public String getDate() {
        return date.toString();
    }

    public String getBegin() {
        return begin.toString();
    }

    public String getEnd() {
        return end.toString();
    }

    public void setDate(String date) {
        this.date = DateConverter.convertStringToLocalDate(date);
    }

    public void setBegin(String begin) {
        this.begin = DateConverter.convertStringToLocalTime(begin);

    }

    public void setEnd(String end) {
        this.end = DateConverter.convertStringToLocalTime(end);
        ;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus(){
        return status.get();
    }

    public void setStatus(String status){
        this.status.set(status);
    }

    public LocalDate getDeletionDate(){
        return deletionDate.get();
    }

    public void setDeletionDate(LocalDate deletionDate){
        this.deletionDate.set(deletionDate);
    }

    public LocalDate getArchiveDate(){
        return archiveDate.get();
    }
    public void setArchiveDate(LocalDate archiveDate) {
        this.archiveDate.set(archiveDate);
    }
    public String getDeletedBy() {
        return deletedBy.get();
    }
    public void setDeletedBy(String deletedBy) {
        this.deletedBy.set(deletedBy);
    }
    public String getChangedBy() {
        return changedBy.get();
    }
    public void setChangedBy(String changedBy) {
        this.changedBy.set(changedBy);
    }


    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid + "\nPID: " + this.pid + "\nDate: " + this.date + "\nBegin: " +
                this.begin + "\nEnd: " + this.end + "\nDescription: " + this.description + "\nRemarks: " +
                this.remarks + "\nNurse" + "\nNID:" + this.nid  + "\nStatus: " + this.status +
                "\nDatum gelöscht: " + this.deletionDate +"\nDatum archiviert: " + this.archiveDate + "\n";
    }
    /**
     * Marks the treatment for future deletion by setting the archive date to the current date
     * and the deletion date to 10 years from now. Optionally sets the status to inactive.
     * This method is typically used for soft-deletion workflows.
     */
    public void setNid(long nid) {
        this.nid = nid;
    }
    public void markForDeletion() {
        this.status.set(STATUS_INACTIVE); // optional
        this.archiveDate.set(LocalDate.now());
        this.deletionDate.set(LocalDate.now().plusYears(10));

    }

}
