package com.nominaguate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Punto de entrada.
 *
 * La aplicacion ahora arranca en la pantalla de Login. Tras autenticar,
 * LoginController reemplaza la Scene de este mismo Stage segun el rol:
 * ADMIN/RRHH -> DashboardView, EMPLEADO -> PlanillaView.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent raiz = FXMLLoader.load(
                getClass().getResource("/com/nominaguate/view/LoginView.fxml"));

        stage.setTitle("NominaGuate - Inicio de Sesion");
        stage.setScene(new Scene(raiz, 420, 460));
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
