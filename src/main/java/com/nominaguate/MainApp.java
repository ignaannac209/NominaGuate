package com.nominaguate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Cargar el FXML de la vista inicial (ej. Login)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nominaguate/view/LoginView.fxml"));
        Parent root = loader.load();

        // 2. Crear UNA SOLA escena con el root cargado
        Scene scene = new Scene(root);

        // 3. Asignar y mostrar el escenario
        primaryStage.setTitle("NominaGuate - Sistema de Nómina");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}