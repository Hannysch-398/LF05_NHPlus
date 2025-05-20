package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Person;
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

    private void showData() {
        this.labelPatientName.setText(patient.getSurname() + ", " + patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datePicker.setValue(date);
        this.textFieldBegin.setText(this.treatment.getBegin());
        this.textFieldEnd.setText(this.treatment.getEnd());
        this.textFieldDescription.setText(this.treatment.getDescription());
        this.textAreaRemarks.setText(this.treatment.getRemarks());
        this.comboBoxNurseSelection.setItems(nurseSelection);
        comboBoxNurseSelection.getSelectionModel().select(0);
        this.createComboBoxDataNurse();
    }

    private void createComboBoxDataNurse() {
        NurseDao dao = DaoFactory.getDaoFactory().createNurseDAO();
        try {
            nurseList = dao.readAll(); // Liste der Pflegekräfte laden
            nurseSelection.setAll(nurseList); // in ObservableList einfügen
            comboBoxNurseSelection.setItems(nurseSelection); // ComboBox befüllen

            // StringConverter für Anzeige in der ComboBox setzen
            comboBoxNurseSelection.setConverter(new StringConverter<>() {
                @Override
                public String toString(Nurse nurse) {
                    return (nurse == null) ? "" : nurse.getSurname() + ", " + nurse.getFirstName();
                }

                @Override
                public Nurse fromString(String string) {
                    return null; // wird nicht benötigt
                }
            });

        } catch (SQLException exception) {
            exception.printStackTrace(); // besser: Logging verwenden
        }
    }
    private boolean areInputsValid() {
        try {
            LocalTime begin = DateConverter.convertStringToLocalTime(textFieldBegin.getText());
            LocalTime end = DateConverter.convertStringToLocalTime(textFieldEnd.getText());

            return datePicker.getValue() != null
                    && !textFieldDescription.getText().isBlank()
                    && !textAreaRemarks.getText().isBlank()
                    && begin != null
                    && end != null
                    && end.isAfter(begin);
        } catch (Exception e) {
            return false;
        }
    }
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

    private void doUpdate() {
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.update(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }
}