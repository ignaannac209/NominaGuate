package com.nominaguate;

import com.nominaguate.config.DataBaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

@Override
public void start(Stage primaryStage) throws Exception {
    // Apunta directamente a a la vista
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    primaryStage.setTitle("NominaGuate - Sistema de Nómina");
    primaryStage.setScene(scene);
    primaryStage.show();
}

    public static void main(String[] args) {
        try {
            DataBaseConnection.getConnectionDataBase();
            System.out.println("Conectado a la base de datos!");
        } catch (Exception e) {
            System.out.println("No se pudo conectar a la base de datos: " + e.getMessage());
            System.out.println("La aplicación continuará; puedes iniciar sesión con el usuario de prueba admin/123.");
        }
        launch(args);
    }
}