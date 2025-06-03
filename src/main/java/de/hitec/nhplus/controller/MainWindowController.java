package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
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
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller class for the main application window.
 * <p>
 * Handles navigation between patient, nurse, and treatment views,
 * manages automatic logout on inactivity, and displays the currently logged-in user.
 */


public class MainWindowController {


    @FXML
    private BorderPane mainBorderPane;

    private Timeline inactivityTimer;

    /**
     * Called automatically after the FXML file has been loaded.
     * Initializes the inactivity timer and registers global event filters
     * for mouse and keyboard activity to reset the timer on interaction.
     */
    @FXML
    public void initialize() {
        setupInactivityTimer();


        Platform.runLater(() -> {
            Scene scene = mainBorderPane.getScene();
            scene.addEventFilter(MouseEvent.ANY, e -> resetTimer());
            scene.addEventFilter(KeyEvent.ANY, e -> resetTimer());
        });
    }

    /**
     * Initializes the inactivity timer.
     * If no input is detected within a defined time interval, {@code autoLogout()} is triggered.
     */
    private void setupInactivityTimer() {
        inactivityTimer = new Timeline(new KeyFrame(Duration.minutes(1), e -> autoLogout()));
        inactivityTimer.setCycleCount(1);
        inactivityTimer.play();
    }

    /**
     * Resets the inactivity timer, e.g. when user interaction is detected such as mouse movement or key press.
     */
    private void resetTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.stop();
            inactivityTimer.playFromStart();
        }
    }

    /**
     * Logs out the current user automatically, clears the session, and returns to the login screen.
     * This is triggered by the inactivity timer.
     */
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

    /**
     * Displays the patient overview and deletes expired patient records from the database beforehand.
     *
     * @param event the ActionEvent triggered by the user interaction
     */
    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        try {
            PatientDao patientDao = DaoFactory.getDaoFactory().createPatientDAO();
            patientDao.deleteExpiredPatient();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllPatientView.fxml"));

            mainBorderPane.setCenter(loader.load());
        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Displays the treatment overview and deletes expired treatment records from the database beforehand.
     *
     * @param event the ActionEvent triggered by the user interaction
     */
    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        try {
            TreatmentDao treatmentDao = DaoFactory.getDaoFactory().createTreatmentDao();
            treatmentDao.deleteExpiredTreatments();


            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllTreatmentView.fxml"));
            mainBorderPane.setCenter(loader.load());

        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Displays the caregiver overview and deletes expired nurse records from the database beforehand.
     *
     * @param event the ActionEvent triggered by the user interaction
     */
    @FXML
    private void handleShowAllCaregiver(ActionEvent event) {
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

    /**
     * Logs out the current user, clears the session, and returns to the login screen.
     *
     * @param event the ActionEvent triggered by the user interaction
     */
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

    @FXML
    private Label loggedInUserLabel;

    /**
     * Sets the username of the currently logged-in user in the main window.
     * Typically called after successful login.
     *
     * @param username the currently logged-in username
     */
    public void setLoggedInUser(String username) {
        loggedInUserLabel.setText(username);
    }


}
