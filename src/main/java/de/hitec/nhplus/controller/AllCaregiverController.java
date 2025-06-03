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
    private void setNumericInput(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // beliebig viele Ziffern erlaubt
                return change;
            }
            return null;
        }));
    }

    private void showNotAuthorizedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Keine Berechtigung");
        alert.setHeaderText("Aktion nicht erlaubt");
        alert.setContentText("Nur Administratoren dürfen Pflegekräfte bearbeiten.");
        alert.showAndWait();
    }


    /**
     * When a cell of the column with first names was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Nurse, String> event) {

        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with surnames was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    private void handleEditSurname(TableColumn.CellEditEvent<Nurse, String> event) {
        if (!Session.isAdmin()) {
            tableView.refresh();  // reset change visually
            showNotAuthorizedAlert();
            return;
        }

        Nurse nurse = event.getRowValue();
        nurse.setSurname(event.getNewValue());

        // Set changedBy to current user
        setChangedBy();

        try {
            dao.update(nurse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
     * When a cell of the column with phoneNumber was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditPhoneNumber(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setPhoneNumber(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with Nurse ID was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */

    //   @FXML
    //   public void handleOnEditCareLevel(TableColumn.CellEditEvent<Nurse, String> event) {
    //       event.getRowValue().setNid(event.getNewValue());
    //       this.doUpdate(event);
    //  }

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
     * This method handles events fired by the button to delete nurses. It calls {@link NurseDao} to delete the
     * nurse from the database and removes the object from the list, which is the data source of the
     * <code>TableView</code>.
     */
   /* @FXML
    public void handleDelete() {
        Nurse selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        // Sicherheitsabfrage
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Löschen bestätigen");
        confirmAlert.setHeaderText("Sind Sie sicher?");
        confirmAlert.setContentText("Möchten Sie diesen Pfleger wirklich löschen?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            // Abgebrochen
            return;
        }
        if (selectedItem != null) {
            try {
                DaoFactory.getDaoFactory().createNurseDAO().deleteById(selectedItem.getNid());
                this.tableView.getItems().remove(selectedItem);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }*/
    @FXML
    public void handleMarkForDelete() {
        Nurse selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        // Sicherheitsabfrage
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Löschen bestätigen");
        confirmAlert.setHeaderText("Sind Sie sicher?");
        confirmAlert.setContentText("Möchten Sie diesen Pfleger wirklich löschen?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            // Abgebrochen
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
        System.out.println("handleAdd aufgerufen");
        try {

            this.dao.create(new Nurse(firstName, surname, phoneNumber, Nurse.STATUS_ACTIVE, null, null, null, null));
            System.out.println("Pflegekraft wird erstellt: " + firstName + " " + surname + " " + phoneNumber);

        } catch (SQLException exception) {
            exception.printStackTrace();

            System.err.println("Fehler beim Erstellen der Pflegekraft.");

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

    private boolean areInputDataValid() {


        return !this.textFieldFirstName.getText().isBlank() && !this.textFieldSurname.getText().isBlank() &&
                !this.textFieldPhoneNumber.getText().isBlank();
    }



    @FXML
    public void handleEdit() {

        Nurse selected = this.tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return; // kein Element ausgewählt
        }

        // Daten aus Textfeldern holen
        String newFirstName = this.textFieldFirstName.getText();
        String newSurname = this.textFieldSurname.getText();
        String newPhoneNumber = this.textFieldPhoneNumber.getText();

        // Validierung
        /*if (newFirstName.isBlank() || newSurname.isBlank() || newPhoneNumber.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte alle Felder ausfüllen.");
            alert.show();
            return;
        }*/
        String currentUser = Session.getCurrentUser().getUsername();
        Nurse nurse = tableView.getSelectionModel().getSelectedItem();
        nurse.setChangedBy(currentUser);
        System.out.println(nurse.getChangedBy());
        // Objekt aktualisieren
        if (!newFirstName.isEmpty()) {
            selected.setFirstName(newFirstName);
        }
        if (!newSurname.isEmpty()) {
            selected.setSurname(newSurname);
        }
        if (!newPhoneNumber.isEmpty()) {
            selected.setPhoneNumber(newPhoneNumber);
        }

        try {
            this.dao.update(selected); // Änderungen in der DB speichern
            this.tableView.refresh(); // Ansicht aktualisieren
            clearTextfields();        // Eingabefelder leeren
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setChangedBy(){
        Nurse nurse = tableView.getSelectionModel().getSelectedItem();
        nurse.setChangedBy(Session.getCurrentUser().getUsername());
    }
    private void setDeletedBy(){
        Nurse nurse = tableView.getSelectionModel().getSelectedItem();
        nurse.setDeletedBy(Session.getCurrentUser().getUsername());
    }
}
