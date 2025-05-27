package de.hitec.nhplus.controller;
import de.hitec.nhplus.utils.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;

    private Timeline inactivityTimer;

    @FXML
    public void initialize() {
        setupInactivityTimer();

        // Warten, bis die Szene fertig ist
        Platform.runLater(() -> {
            Scene scene = mainBorderPane.getScene();
            scene.addEventFilter(MouseEvent.ANY, e -> resetTimer());
            scene.addEventFilter(KeyEvent.ANY, e -> resetTimer());
        });
    }

    private void setupInactivityTimer() {
        inactivityTimer = new Timeline(new KeyFrame(Duration.minutes(1), e -> autoLogout()));
        inactivityTimer.setCycleCount(1);
        inactivityTimer.play();
    }

    private void resetTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.stop();
            inactivityTimer.playFromStart();
        }
    }

    private void autoLogout() {
        Session.clear();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginView.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


    @FXML
    private void handleLogout(ActionEvent event) {
        Session.clear();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginView.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
