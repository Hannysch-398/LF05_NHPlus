package de.hitec.nhplus.controller;

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
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Controller class for editing an existing treatment record.
 * <p>
 * Loads patient and treatment data, allows modification of editable fields
 * (such as date, time, description, and assigned nurse), and persists updates
 * to the database.
 */
public class TreatmentController {

    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelCareLevel;

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
    private ComboBox<Nurse> comboBoxNurseSelection;

    private AllTreatmentController controller;
    private Stage stage;
    private Patient patient;
    private Treatment treatment;
    private List<Nurse> nurseList;
    private final ObservableList<Nurse> nurseSelection = FXCollections.observableArrayList();

    /**
     * Initializes the controller with the given window, treatment, and parent controller.
     * Loads patient data and displays all relevant information in the form.
     *
     * @param controller the calling controller for refreshing the treatment table
     * @param stage      the current window (closed after editing is complete)
     * @param treatment  the treatment to be edited
     */
    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.controller = controller;
        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.treatment = treatment;
            showData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Displays patient information and treatment details in the form.
     * Populates all input fields and sets the current nurse selection.
     */
    private void showData() {
        this.labelPatientName.setText(patient.getSurname() + ", " + patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datePicker.setValue(date);
        this.textFieldBegin.setText(this.treatment.getBegin());
        this.textFieldEnd.setText(this.treatment.getEnd());
        this.textFieldDescription.setText(this.treatment.getDescription());
        this.textAreaRemarks.setText(this.treatment.getRemarks());


        this.createComboBoxDataNurse();


        this.comboBoxNurseSelection.setItems(nurseSelection);


        for (Nurse nurse : nurseList) {
            if (nurse.getNid() == treatment.getNid()) {
                comboBoxNurseSelection.getSelectionModel().select(nurse);
                break;
            }
        }
    }

    /**
     * Loads all available nurses from the database and populates the ComboBox.
     * Uses a {@link StringConverter} to display nurse names in "Last name, First name" format.
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

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Validates the form inputs to ensure all required fields are filled,
     * time values are valid, and the end time is after the start time.
     *
     * @return true if inputs are valid; false otherwise
     */
    private boolean areInputsValid() {
        try {
            LocalTime begin = DateConverter.convertStringToLocalTime(textFieldBegin.getText());
            LocalTime end = DateConverter.convertStringToLocalTime(textFieldEnd.getText());

            return datePicker.getValue() != null && !textFieldDescription.getText().isBlank() &&
                    !textAreaRemarks.getText().isBlank() && begin != null && end != null && end.isAfter(begin);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Called when the user clicks the "Apply Changes" button.
     * Validates inputs, updates the treatment object, saves changes to the database,
     * and refreshes the parent controller's table view.
     */
    @FXML
    public void handleChange() {
        if (!areInputsValid()) {
            new Alert(Alert.AlertType.WARNING, "Bitte fülle alle Felder korrekt aus.").showAndWait();
            return;
        }

        Nurse selectedNurse = comboBoxNurseSelection.getSelectionModel().getSelectedItem();
        if (selectedNurse == null) {
            new Alert(Alert.AlertType.WARNING, "Bitte eine Pflegekraft auswählen!").showAndWait();
            return;
        }
        treatment.setChangedBy(Session.getCurrentUser().getUsername());
        treatment.setDate(datePicker.getValue().toString());
        treatment.setBegin(textFieldBegin.getText());
        treatment.setEnd(textFieldEnd.getText());
        treatment.setDescription(textFieldDescription.getText());
        treatment.setRemarks(textAreaRemarks.getText());
        treatment.setNid(selectedNurse.getNid());

        doUpdate();

        controller.readAllAndShowInTableView();
        stage.close();
    }

    /**
     * Performs the update of the treatment entry in the database using {@link TreatmentDao}.
     */
    private void doUpdate() {
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.update(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Closes the current editing window without saving changes.
     */

    @FXML
    public void handleCancel() {
        stage.close();
    }
}