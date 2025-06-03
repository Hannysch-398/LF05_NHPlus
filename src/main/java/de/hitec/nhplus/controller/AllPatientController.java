package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.Session;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.utils.DateConverter;
import de.hitec.nhplus.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;


/**
 * The <code>AllPatientController</code> contains the entire logic of the patient view. It determines which data is
 * displayed and how to react to events.
 */
public class AllPatientController {

    @FXML
    private TableView<Patient> tableView;

    @FXML
    private TableColumn<Patient, Integer> columnId;

    @FXML
    private TableColumn<Patient, String> columnFirstName;

    @FXML
    private TableColumn<Patient, String> columnSurname;

    @FXML
    private TableColumn<Patient, String> columnDateOfBirth;

    @FXML
    private TableColumn<Patient, String> columnCareLevel;

    @FXML
    private TableColumn<Patient, String> columnRoomNumber;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonAdd;

    @FXML
    private TextField textFieldSurname;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldDateOfBirth;

    @FXML
    private TextField textFieldCareLevel;

    @FXML
    private TextField textFieldRoomNumber;

    @FXML
    private TextField textFieldAssets;

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private PatientDao dao;

    /**
     * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
     * configured.
     */
    public void initialize() {
        this.readAllAndShowInTableView();

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("pid"));


        this.columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        this.columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.columnSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        this.columnDateOfBirth.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnCareLevel.setCellValueFactory(new PropertyValueFactory<>("careLevel"));
        this.columnCareLevel.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        this.columnRoomNumber.setCellFactory(TextFieldTableCell.forTableColumn());


        this.tableView.setItems(this.patients);

        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
            @Override
            public void changed(ObservableValue<? extends Patient> observableValue, Patient oldPatient,
                                Patient newPatient) {
                ;

                if (Session.isAdmin()) {
                    AllPatientController.this.buttonDelete.setDisable(newPatient == null);
                }

            }
        });
        this.buttonAdd.setDisable(true);

        if (Session.isAdmin()) {
            ChangeListener<String> inputNewPatientListener =
                    (observableValue, oldText, newText) -> AllPatientController.this.buttonAdd.setDisable(
                            !AllPatientController.this.areInputDataValid());
            setNumericInput(this.textFieldRoomNumber);
            setNumericInput(this.textFieldCareLevel);
            setBirthDateFormatter(textFieldDateOfBirth);

            this.textFieldSurname.textProperty().addListener(inputNewPatientListener);
            this.textFieldFirstName.textProperty().addListener(inputNewPatientListener);
            this.textFieldDateOfBirth.textProperty().addListener(inputNewPatientListener);
            this.textFieldCareLevel.textProperty().addListener(inputNewPatientListener);
            this.textFieldRoomNumber.textProperty().addListener(inputNewPatientListener);

        }
    }

    /**
     * Sets a {@link TextFormatter} on the given {@link TextField} to allow two valid date formats:
     * - yyyy-MM-dd (e.g., 1985-12-01)
     * - dd.MM.yyyy (e.g., 01.12.1985)
     * <p>
     * If a valid input in dd.MM.yyyy is detected, it will be automatically converted to yyyy-MM-dd.
     *
     * @param field the {@link TextField} for entering a birth date
     */
    private void setBirthDateFormatter(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().trim();


            if (newText.isEmpty() || newText.length() < 10) {
                return change;
            }

            if (newText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    LocalDate.parse(newText, DateTimeFormatter.ISO_LOCAL_DATE);
                    return change;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }

            // Wenn Format dd.MM.yyyy → umwandeln in yyyy-MM-dd
            if (newText.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                try {
                    LocalDate parsed = LocalDate.parse(newText, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    Platform.runLater(() -> field.setText(parsed.toString())); // async setzen
                    return null;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }

            // alles andere ablehnen, aber nicht sofort beim Tippen
            return change;
        }));
    }

    /**
     * Sets a {@link TextFormatter} on the given {@link TextField} to restrict input to numeric characters (digits
     * only).
     * This ensures that the user can only enter numbers, which is useful for fields like phone numbers or IDs.
     *
     * @param field the {@link TextField} that should accept only numeric input
     */
    private void setNumericInput(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    /**
     * When a cell of the column with first names was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Patient, String> event) {

        if (Session.isAdmin()){
        event.getRowValue().setFirstName(event.getNewValue());
        setChangedBy();
        this.doUpdate(event);}
        else{
            tableView.refresh();  // Zeigt den alten Wert wieder
            showNotAuthorizedAlert();}

    }

    /**
     * When a cell of the column with surnames was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Patient, String> event) {
        if (Session.isAdmin()) {
            event.getRowValue().setSurname(event.getNewValue());
            setChangedBy();
            this.doUpdate(event);
        }

        else{
        tableView.refresh();  // Zeigt den alten Wert wieder
            showNotAuthorizedAlert();}

    }

    /**
     * When a cell of the column with dates of birth was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditDateOfBirth(TableColumn.CellEditEvent<Patient, String> event) {

        if (Session.isAdmin()){
        event.getRowValue().setDateOfBirth(event.getNewValue());
        setChangedBy();

        this.doUpdate(event);}
        else{
            tableView.refresh();  // Zeigt den alten Wert wieder
            showNotAuthorizedAlert();}

    }

    /**
     * When a cell of the column with care levels was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditCareLevel(TableColumn.CellEditEvent<Patient, String> event) {

        if (Session.isAdmin()){
        event.getRowValue().setCareLevel(event.getNewValue());
        setChangedBy();

        this.doUpdate(event);}
        else{
            tableView.refresh();  // Zeigt den alten Wert wieder
            showNotAuthorizedAlert();}

    }

    /**
     * When a cell of the column with room numbers was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditRoomNumber(TableColumn.CellEditEvent<Patient, String> event) {

        if (Session.isAdmin()){
        event.getRowValue().setRoomNumber(event.getNewValue());
        setChangedBy();
        this.doUpdate(event);}
        else{
            tableView.refresh();  // Zeigt den alten Wert wieder
            showNotAuthorizedAlert();}

    }


    /**
     * Updates a patient by calling the method <code>update()</code> of {@link PatientDao}.
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all patients to the table by clearing the list of all patients and filling it again by all persisted
     * patients, delivered by {@link PatientDao}.
     */
    private void readAllAndShowInTableView() {
        this.patients.clear();
        this.dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            this.patients.addAll(this.dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method handles events fired by the button to delete patients. It calls {@link PatientDao} to delete the
     * patient from the database and removes the object from the list, which is the data source of the
     * <code>TableView</code>.
     */
    @FXML
    public void handleMarkForDelete() {
        Patient selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Löschen bestätigen");
        confirmAlert.setHeaderText("Sind Sie sicher?");
        confirmAlert.setContentText("Möchten Sie diese/n Patient/in wirklich löschen?");
        setDeletedBy();
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {

            return;
        }
        if (selectedItem != null) {
            System.out.println("mark for deletion aufgerufen");
            selectedItem.markForDeletion();
            try {
                DaoFactory.getDaoFactory().createPatientDAO().update(selectedItem);
                readAllAndShowInTableView();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

    }

    /**
     * This method handles the events fired by the button to add a patient. It collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Patient</code> of it and passes the object to
     * {@link PatientDao} to persist the data.
     */
    @FXML
    public void handleAdd() {
        String surname = this.textFieldSurname.getText();
        String firstName = this.textFieldFirstName.getText();
        String birthday = this.textFieldDateOfBirth.getText();
        LocalDate date = DateConverter.convertStringToLocalDate(birthday);
        String careLevel = this.textFieldCareLevel.getText();
        String roomNumber = this.textFieldRoomNumber.getText();

        try {
            this.dao.create(
                    new Patient(firstName, surname, date, careLevel, roomNumber, Patient.STATUS_ACTIVE, null, null,
                            null, null));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.textFieldFirstName.clear();
        this.textFieldSurname.clear();
        this.textFieldDateOfBirth.clear();
        this.textFieldCareLevel.clear();
        this.textFieldRoomNumber.clear();
    }

    /**
     * Validates whether the input fields for creating a new patient contain valid data.
     * Specifically checks that:
     * - No required field is blank
     * - The birth date is parsable into a {@link LocalDate}
     *
     * @return true if all input fields are valid, false otherwise
     */
    private boolean areInputDataValid() {
        if (!this.textFieldDateOfBirth.getText().isBlank()) {
            try {
                DateConverter.convertStringToLocalDate(this.textFieldDateOfBirth.getText());
            } catch (Exception exception) {
                return false;
            }
        }

        return !this.textFieldFirstName.getText().isBlank() && !this.textFieldSurname.getText().isBlank() &&
                !this.textFieldDateOfBirth.getText().isBlank() && !this.textFieldCareLevel.getText().isBlank() &&
                !this.textFieldRoomNumber.getText().isBlank();
    }


    /**
     * Sets the {@code changedBy} field of the currently selected patient
     * to the username of the currently logged-in user from the {@link Session}.
     */

    private void showNotAuthorizedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Keine Berechtigung");
        alert.setHeaderText("Aktion nicht erlaubt");
        alert.setContentText("Nur Administratoren dürfen Pflegekräfte bearbeiten.");
        alert.showAndWait();
    }
    private void setChangedBy(){


        Patient patient = tableView.getSelectionModel().getSelectedItem();
        patient.setChangedBy(Session.getCurrentUser().getUsername());
    }
    private void setDeletedBy(){
        Patient patient = tableView.getSelectionModel().getSelectedItem();
        patient.setDeletedBy(Session.getCurrentUser().getUsername());
    }
}
