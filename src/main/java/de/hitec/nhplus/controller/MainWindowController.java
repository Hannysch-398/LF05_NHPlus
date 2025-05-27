package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;

public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllCaregiver(ActionEvent event){
        try {
            // Vor dem Laden: veraltete Einträge prüfen und ggf. löschen
            NurseDao nurseDao = DaoFactory.getDaoFactory().createNurseDAO();
            nurseDao.deleteExpiredNurses(); // Diese Methode musst du wie unten beschrieben implementieren

            // Jetzt die Übersicht laden
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllCareGiverView.fxml"));
            mainBorderPane.setCenter(loader.load());

        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowTest(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/TestView.fxml"));
        try{
            mainBorderPane.setCenter(loader.load());

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }


}
