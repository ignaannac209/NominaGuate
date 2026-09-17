package com.nominaguate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Punto de entrada.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent raiz = FXMLLoader.load(
                getClass().getResource("/com/nominaguate/view/PlanillaView.fxml"));

        stage.setTitle("NominaGuate - Procesamiento de Planilla");
        stage.setScene(new Scene(raiz, 900, 550));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
