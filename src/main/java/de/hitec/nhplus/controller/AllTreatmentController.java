package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Person;
import de.hitec.nhplus.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code AllTreatmentController} class manages the treatment overview view.
 * It handles displaying, filtering, adding, editing, and soft-deleting treatments,
 * as well as initializing UI elements like the ComboBox and TableView.
 */
public class AllTreatmentController {

    @FXML
    private TableView<Treatment> tableView;

    @FXML
    private TableColumn<Treatment, Integer> columnId;

    @FXML
    private TableColumn<Treatment, Integer> columnPid;

    @FXML
    private TableColumn<Treatment, String> columnDate;

    @FXML
    private TableColumn<Treatment, String> columnBegin;

    @FXML
    private TableColumn<Treatment, String> columnEnd;

    @FXML
    private TableColumn<Treatment, String> columnDescription;

    @FXML
    private TableColumn<Treatment, String> columnNurse;


    @FXML
    private ComboBox<String> comboBoxPatientSelection;

    @FXML
    private Button buttonDelete;


    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();
    private TreatmentDao dao;
    private final ObservableList<String> patientSelection = FXCollections.observableArrayList();
    private List<Patient> patientList;

    /**
     * Initializes the controller after the FXML has been loaded.
     * Sets up column bindings, populates the ComboBox and TableView,
     * and defines handlers for selection and double-click actions.
     */

    public void initialize() {
        readAllAndShowInTableView();
        comboBoxPatientSelection.setItems(patientSelection);
        comboBoxPatientSelection.getSelectionModel().select(0);

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("tid"));
        this.columnPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        this.columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.tableView.setItems(this.treatments);
        this.columnNurse.setCellValueFactory(new PropertyValueFactory<>("nurseName"));


        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldTreatment, newTreatment) -> AllTreatmentController.this.buttonDelete.setDisable(
                        newTreatment == null));

        this.createComboBoxData();

        this.tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
                Treatment treatment = tableView.getSelectionModel().getSelectedItem();
                treatmentWindow(treatment);
            }
        });
    }

    /**
     * Loads all treatments from the database, assigns nurse names to each treatment,
     * and populates the TableView with the result.
     */
    public void readAllAndShowInTableView() {
        this.treatments.clear();
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            List<Treatment> allTreatments = dao.readAll();

            NurseDao nurseDao = DaoFactory.getDaoFactory().createNurseDAO();

            for (Treatment treatment : allTreatments) {
                Nurse nurse = nurseDao.read((int) treatment.getNid());
                if (nurse != null) {
                    String nurseName = nurse.getSurname() + ", " + nurse.getFirstName();
                    treatment.setNurseName(nurseName);
                } else {
                    treatment.setNurseName("Unbekannt");
                }
            }

            this.treatments.addAll(allTreatments);
            System.out.println("Behandlungen geladen: " + this.treatments.size());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads all patients from the database and populates the ComboBox with their surnames.
     * Includes an "all" option for displaying treatments from all patients.
     */
    private void createComboBoxData() {
        PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            patientList = dao.readAll();

            this.patientSelection.add("alle");
            patientList.stream().map(Person::getSurname).forEach(patientSelection::add);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Filters the displayed treatments based on the selected patient in the ComboBox.
     * If "all" is selected, all treatments are shown.
     * Otherwise, only treatments for the selected patient are displayed.
     */
    @FXML
    public void handleComboBox() {
        String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
        this.treatments.clear();
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();

        try {
            List<Treatment> filteredTreatments;
            if (selectedPatient.equals("alle")) {
                filteredTreatments = this.dao.readAll();
            } else {
                Patient patient = searchInList(selectedPatient);
                if (patient != null) {
                    filteredTreatments = this.dao.readTreatmentsByPid(patient.getPid());
                } else {
                    return;
                }
            }


            NurseDao nurseDao = DaoFactory.getDaoFactory().createNurseDAO();
            for (Treatment treatment : filteredTreatments) {
                Nurse nurse = nurseDao.read((int) treatment.getNid());
                if (nurse != null) {
                    String nurseName = nurse.getSurname() + ", " + nurse.getFirstName();
                    treatment.setNurseName(nurseName);
                } else {
                    treatment.setNurseName("Unbekannt");
                }
            }

            this.treatments.addAll(filteredTreatments);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Searches the internal patient list for a patient with the given surname.
     *
     * @param surname the surname to search for
     * @return the matching {@link Patient}, or {@code null} if not found
     */
    private Patient searchInList(String surname) {
        for (Patient patient : this.patientList) {
            if (patient.getSurname().equals(surname)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Handles the deletion of the selected treatment by marking it as deleted.
     * Shows a confirmation dialog and updates the {@code deletedBy} field with the current user.
     */
    @FXML
    public void handleMarkForDelete() {
        int index = this.tableView.getSelectionModel().getSelectedIndex();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Löschen bestätigen");
        confirmAlert.setHeaderText("Sind Sie sicher?");
        confirmAlert.setContentText("Möchten Sie diesen Behandlungseintrag wirklich löschen?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {

            return;
        }
        Treatment selectedItem = this.treatments.get(index);
        if (selectedItem != null) {
            selectedItem.markForDeletion();
            Treatment treatment = tableView.getSelectionModel().getSelectedItem();
            treatment.setDeletedBy(Session.getCurrentUser().getUsername());

            try {
                DaoFactory.getDaoFactory().createTreatmentDao().update(selectedItem);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            this.tableView.refresh();
        }
    }

    /**
     * Opens the window to create a new treatment.
     * Requires a patient to be selected via the ComboBox; otherwise, an info dialog is shown.
     */
    @FXML
    public void handleNewTreatment() {
        try {
            String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
            Patient patient = searchInList(selectedPatient);
            newTreatmentWindow(patient);
        } catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten aus!");
            alert.showAndWait();
        }
    }

    /**
     * Opens the treatment edit window when a treatment row is double-clicked in the table.
     */
    @FXML
    public void handleMouseClick() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.treatments.get(index);
                treatmentWindow(treatment);
            }
        });
    }

    /**
     * Opens the New Treatment window for the specified patient.
     * Loads the FXML view, initializes the controller, and opens the modal window.
     *
     * @param patient the patient to assign the new treatment to
     */
    public void newTreatmentWindow(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);


            Stage stage = new Stage();

            NewTreatmentController controller = loader.getController();
            controller.initialize(this, stage, patient);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Opens the Treatment Edit window for the given treatment.
     * Loads the FXML view, initializes the controller, and opens the modal window.
     *
     * @param treatment the treatment to edit
     */
    public void treatmentWindow(Treatment treatment) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);


            Stage stage = new Stage();
            TreatmentController controller = loader.getController();
            controller.initializeController(this, stage, treatment);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
