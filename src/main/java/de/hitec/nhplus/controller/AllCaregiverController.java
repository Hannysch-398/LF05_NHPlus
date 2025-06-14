package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.model.Nurse;

import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.utils.Session;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


/**
 * The <code>AllPatientController</code> contains the entire logic of the patient view. It determines which data is
 * displayed and how to react to events.
 */
public class AllCaregiverController {

    @FXML
    private TableView<Nurse> tableView;
    @FXML
    private TableColumn<Nurse, Long> columnNid;
    @FXML
    private TableColumn<Nurse, String> columnFirstName;
    @FXML
    private TableColumn<Nurse, String> columnSurname;
    @FXML
    private TableColumn<Nurse, String> columnPhoneNumber;


    @FXML
    private Button buttonEdit;

    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private TextField textFieldPhoneNumber;

    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelete;


    private final ObservableList<Nurse> nurses = FXCollections.observableArrayList();
    private NurseDao dao;

    /**
     * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
     * configured.
     */
    public void initialize() throws SQLException {
        this.dao = DaoFactory.getDaoFactory().createNurseDAO();
        this.readAllAndShowInTableView();


        this.columnNid.setCellValueFactory(new PropertyValueFactory<>("nid"));

        this.columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        this.columnFirstName.setOnEditCommit(this::handleEditFirstname);


        this.columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.columnSurname.setCellFactory(TextFieldTableCell.forTableColumn());


        this.columnSurname.setOnEditCommit(this::handleEditSurname);


        this.columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        this.columnPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        this.columnPhoneNumber.setOnEditCommit(this::handleEditphoneNumber);


        this.tableView.setItems(this.nurses);

        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldNurse, newNurse) -> {
            if (Session.isAdmin()) {
                this.buttonDelete.setDisable(newNurse == null);
            }
        });

        this.buttonAdd.setDisable(true);

        if (Session.isAdmin()) {

            setNumericInput(this.textFieldPhoneNumber);

            ChangeListener<String> inputNewNurseListener =
                    (observable, oldText, newText) -> this.buttonAdd.setDisable(!this.areInputDataValid());

            this.textFieldSurname.textProperty().addListener(inputNewNurseListener);
            this.textFieldFirstName.textProperty().addListener(inputNewNurseListener);
            this.textFieldPhoneNumber.textProperty().addListener(inputNewNurseListener);
        }


    }

    /**
     * Sets a {@link TextFormatter} on the given {@link TextField} to restrict input to numeric characters (digits
     * only).
     * This is useful for fields like phone numbers or IDs where only digits should be allowed.
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
     * Displays a warning alert to inform the user that the attempted action is not authorized.
     * This is typically shown when a non-admin user tries to perform restricted operations.
     */
    private void showNotAuthorizedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Keine Berechtigung");
        alert.setHeaderText("Aktion nicht erlaubt");
        alert.setContentText("Nur Administratoren dürfen Pflegekräfte bearbeiten.");
        alert.showAndWait();
    }

    /**
     * Handles edit events for the surname column.
     * If the user is not an admin, the change is reverted and a warning is shown.
     * Otherwise, the new value is persisted and the "changedBy" field is updated.
     *
     * @param event the edit event containing the changed nurse
     */

    private void handleEditSurname(TableColumn.CellEditEvent<Nurse, String> event) {
        if (!Session.isAdmin()) {
            tableView.refresh();
            showNotAuthorizedAlert();
            return;
        }

        Nurse nurse = event.getRowValue();
        nurse.setSurname(event.getNewValue());


        nurse.setChangedBy(Session.getCurrentUsername());

        try {
            dao.update(nurse);
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    /**
     * When a cell of the column with FirstName was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */

    @FXML
    private void handleEditFirstname(TableColumn.CellEditEvent<Nurse, String> event) {
        if (!Session.isAdmin()) {
            tableView.refresh();
            showNotAuthorizedAlert();
            return;
        }

        Nurse nurse = event.getRowValue();
        nurse.setFirstName(event.getNewValue());

        // Set changedBy to current user
        setChangedBy();

        try {
            dao.update(nurse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * When a cell of the column with phoneN was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    private void handleEditphoneNumber(TableColumn.CellEditEvent<Nurse, String> event) {
        if (!Session.isAdmin()) {
            tableView.refresh();
            showNotAuthorizedAlert();
            return;
        }

        Nurse nurse = event.getRowValue();
        nurse.setFirstName(event.getNewValue());

        // Set changedBy to current user
        setChangedBy();

        try {
            dao.update(nurse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates a nurse by calling the method <code>update()</code> of {@link NurseDao}.
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Nurse, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all nurses to the table by clearing the list of all nurses and filling it again by all persisted
     * patients, delivered by {@link NurseDao}.
     */
    private void readAllAndShowInTableView() throws SQLException {
        this.nurses.clear();
        this.dao = DaoFactory.getDaoFactory().createNurseDAO();

        List<Nurse> result = this.dao.readAll();

        this.nurses.addAll(result);
    }


    /**
     * Handles the deletion of a caregiver entry by marking it for deletion.
     * Requires admin rights and asks for user confirmation.
     */
    @FXML
    public void handleMarkForDelete() {
        Nurse selectedItem = this.tableView.getSelectionModel().getSelectedItem();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Löschen bestätigen");
        confirmAlert.setHeaderText("Sind Sie sicher?");
        confirmAlert.setContentText("Möchten Sie diesen Pfleger wirklich löschen?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {

            return;
        }
        if (selectedItem != null) {

            selectedItem.markForDeletion(); // setzt z. B. status = "i", deletionDate = +10 Jahre
            setDeletedBy();


            try {
                DaoFactory.getDaoFactory().createNurseDAO().update(selectedItem); // speichert Soft-Delete
                readAllAndShowInTableView();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }


        }
    }


    /**
     * This method handles the events fired by the button to add a nurse. It collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Nurse</code> of it and passes the object to
     * {@link NurseDao} to persist the data.
     */
    @FXML
    public void handleAdd() throws SQLException {
        String surname = this.textFieldSurname.getText();
        String firstName = this.textFieldFirstName.getText();
        String phoneNumber = this.textFieldPhoneNumber.getText();
        try {
            this.dao.create(new Nurse(firstName, surname, phoneNumber, Nurse.STATUS_ACTIVE, null, null, null, null));
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
        this.textFieldPhoneNumber.clear();
    }

    /**
     * Validates that all required input fields are filled with non-blank values.
     *
     * @return true if input is valid, false otherwise
     */
    private boolean areInputDataValid() {
        return !this.textFieldFirstName.getText().isBlank() && !this.textFieldSurname.getText().isBlank() &&
                !this.textFieldPhoneNumber.getText().isBlank();
    }



    private void setChangedBy() {
        Nurse nurse = tableView.getSelectionModel().getSelectedItem();
        nurse.setChangedBy(Session.getCurrentUser().getUsername());
    }

    private void setDeletedBy() {
        Nurse nurse = tableView.getSelectionModel().getSelectedItem();
        nurse.setDeletedBy(Session.getCurrentUser().getUsername());
    }
}
