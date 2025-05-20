package de.hitec.nhplus;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Main instance;
    private Stage primaryStage;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        showLoginWindow();
        //mainWindow();
    }
    public void showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginView.fxml"));
            VBox pane2 = loader.load();
            Scene scene2 = new Scene(pane2);
            this.primaryStage.setTitle("Login");
            this.primaryStage.setScene(scene2);
            this.primaryStage.setResizable(false);
            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
            BorderPane pane = loader.load();

            Scene scene = new Scene(pane);
            this.primaryStage.setTitle("NHPlus");
            this.primaryStage.setScene(scene);
            this.primaryStage.setResizable(false);
            this.primaryStage.show();

            this.primaryStage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        launch(args);
    }
}