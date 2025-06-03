package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller-Klasse für das Fenster zum Anlegen einer neuen Behandlung.
 * <p>
 * Verknüpft Patientendaten mit Eingaben zum Behandlungszeitraum, Beschreibung und betreuender Pflegekraft.
 * Validiert Formulareingaben und speichert die neue Behandlung in der Datenbank.
 */
public class NewTreatmentController {

    @FXML
    private Label labelFirstName;
    @FXML
    private Label labelSurname;
    @FXML
    private TextField textFieldBegin;
    @FXML
    private TextField textFieldEnd;
    @FXML
    private TextField textFieldDescription;
    @FXML
    private TextArea textAreaRemarks;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button buttonAdd;
    @FXML
    private ComboBox<Nurse> comboBoxNurseSelection;

    private AllTreatmentController controller;
    private Patient patient;
    private Stage stage;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final ObservableList<Nurse> nurseSelection = FXCollections.observableArrayList();
    private List<Nurse> nurseList;

    /**
     * Initializes the treatment form, displays the patient data,
     * loads available nurses from the database, and pre-fills date and time fields.
     *
     * @param controller the calling controller for table refresh after saving
     * @param stage      the current window (closed after success or cancel)
     * @param patient    the patient to whom the treatment will be assigned
     */
    public void initialize(AllTreatmentController controller, Stage stage, Patient patient) {
        this.controller = controller;
        this.patient = patient;
        this.stage = stage;

        setupFormValidation();
        showPatientData();
        createComboBoxDataNurse();
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up form validation rules:
     * - Enables the submit button only if all fields are valid
     * - Adds a custom date converter for localized input format handling
     */
    private void setupFormValidation() {
        buttonAdd.setDisable(true);

        ChangeListener<String> inputListener = (obs, oldVal, newVal) -> buttonAdd.setDisable(areInputDataInvalid());
        textFieldBegin.textProperty().addListener(inputListener);
        textFieldEnd.setText(LocalTime.now().format(timeFormatter));
        textFieldEnd.textProperty().addListener(inputListener);
        textFieldDescription.textProperty().addListener(inputListener);
        textAreaRemarks.textProperty().addListener(inputListener);
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> buttonAdd.setDisable(areInputDataInvalid()));

        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date == null) ? "" : DateConverter.convertLocalDateToString(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateConverter.convertStringToLocalDate(string);
            }
        });
    }

    /**
     * Displays the selected patient's first and last name in the form.
     */
    private void showPatientData() {
        labelFirstName.setText(patient.getFirstName());
        labelSurname.setText(patient.getSurname());
    }

    /**
     * Loads all nurses from the database and populates the selection combo box.
     * Configures the display of nurse names in "Last name, First name" format.
     */
    private void createComboBoxDataNurse() {
        NurseDao dao = DaoFactory.getDaoFactory().createNurseDAO();
        try {
            nurseList = dao.readAll();
            nurseSelection.setAll(nurseList);
            comboBoxNurseSelection.setItems(nurseSelection);

            comboBoxNurseSelection.setConverter(new StringConverter<>() {
                @Override
                public String toString(Nurse nurse) {
                    return (nurse == null) ? "" : nurse.getSurname() + ", " + nurse.getFirstName();
                }

                @Override
                public Nurse fromString(String string) {
                    return null;
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Triggered when the user confirms the treatment creation.
     * Validates inputs, constructs a {@link Treatment} object, and stores it in the database.
     * Closes the window on success.
     */

    @FXML
    public void handleAdd() {
        Nurse selectedNurse = comboBoxNurseSelection.getSelectionModel().getSelectedItem();
        if (selectedNurse == null) {
            new Alert(Alert.AlertType.WARNING, "Bitte eine Pflegekraft auswählen!").showAndWait();
            return;
        }

        LocalDate date = datePicker.getValue();
        LocalTime begin = DateConverter.convertStringToLocalTime(textFieldBegin.getText());
        LocalTime end = DateConverter.convertStringToLocalTime(textFieldEnd.getText());
        String description = textFieldDescription.getText();
        String remarks = textAreaRemarks.getText();

        Treatment treatment =
                new Treatment(patient.getPid(), date, begin, end, description, remarks, selectedNurse.getNid(),
                        Treatment.STATUS_ACTIVE, null, null, null, null);
        createTreatment(treatment);

        controller.readAllAndShowInTableView();
        stage.close();
    }

    /**
     * Persists the given treatment object to the database using {@link TreatmentDao}.
     *
     * @param treatment the treatment object to store
     */
    private void createTreatment(Treatment treatment) {
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.create(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes the current form without saving any data.
     */
    @FXML
    public void handleCancel() {
        stage.close();
    }

    /**
     * Validates form input:
     * - Ensures required fields are not empty
     * - Verifies time values are valid and end time is after start time
     *
     * @return true if input is invalid, false otherwise
     */
    private boolean areInputDataInvalid() {
        try {
            LocalTime begin = DateConverter.convertStringToLocalTime(textFieldBegin.getText());
            LocalTime end = DateConverter.convertStringToLocalTime(textFieldEnd.getText());
            return datePicker.getValue() == null || textFieldDescription.getText().isBlank() || begin == null ||
                    end == null || !end.isAfter(begin);
        } catch (Exception e) {
            return true;
        }
    }

}
