package de.hitec.nhplus.controller;
import de.hitec.nhplus.Main;
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

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    


    public void handleLogin() {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            messageLabel.setText("Bitte Benutzername und Passwort eingeben.");
            return;
        }
        //Test
        System.out.println("Eingegebenes Passwort (klar): " + inputPassword);
        System.out.println("Gehasht (zur Kontrolle): " + PasswordUtil.hashPassword(inputPassword));

        try {
            UserDao userDao = DaoFactory.getDaoFactory().createUserDAO();
            List<User> users = userDao.readAll();


            for (User user : users) {

                if (user.getUsername().equals(inputUsername) && user.checkPassword(inputPassword)) {
                    Session.setCurrentUser(user);
                    messageLabel.setText("Login erfolgreich!");

                    //zur Hauptseite wechseln
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

    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
            BorderPane pane = loader.load();

            Scene scene = new Scene(pane);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("NHPlus");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginView.fxml"));
            VBox pane2 = loader.load();
            Scene scene2 = new Scene(pane2);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(scene2);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
