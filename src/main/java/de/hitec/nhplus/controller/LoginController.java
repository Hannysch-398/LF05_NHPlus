package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.controller.MainWindowController;
import de.hitec.nhplus.utils.Session;
import javafx.fxml.FXMLLoader;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.UserDao;
import de.hitec.nhplus.model.User;
import de.hitec.nhplus.utils.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * The {@code LoginController} class manages the login view and authentication logic.
 * It handles user input, verifies credentials against the database, manages session setup,
 * and opens the main application window upon successful login.
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;


    /**
     * Handles the login process when the login button is clicked.
     * Validates input fields, checks the provided credentials against the database,
     * sets the current session user if authentication is successful, and opens the main window.
     * Displays appropriate messages in case of input errors or failed login.
     */
    public void handleLogin() {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            messageLabel.setText("Bitte Benutzername und Passwort eingeben.");
            return;
        }


        try {
            UserDao userDao = DaoFactory.getDaoFactory().createUserDAO();
            List<User> users = userDao.readAll();


            for (User user : users) {

                if (user.getUsername().equals(inputUsername) && user.checkPassword(inputPassword)) {
                    Session.setCurrentUser(user);
                    messageLabel.setText("Login erfolgreich!");

                    mainWindow();
                    return;
                }
            }

            messageLabel.setText("Benutzername oder Passwort ist falsch.");
        } catch (SQLException e) {
            messageLabel.setText("Fehler beim Zugriff auf die Datenbank.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the main application window after a successful login.
     * Loads the main layout from FXML, passes the logged-in user's name to the controller,
     * and replaces the current login scene with the main interface.
     */
    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
            BorderPane pane = loader.load();

            Scene scene = new Scene(pane);
            MainWindowController mainController = loader.getController();
            mainController.setLoggedInUser(Session.getCurrentUsername());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("NHPlus");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
