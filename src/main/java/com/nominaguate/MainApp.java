package com.nominaguate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Punto de entrada.
<<<<<<< HEAD
=======
 *
 * La aplicacion ahora arranca en la pantalla de Login. Tras autenticar,
 * LoginController reemplaza la Scene de este mismo Stage segun el rol:
 * ADMIN/RRHH -> DashboardView, EMPLEADO -> PlanillaView.
>>>>>>> feature/implementacion-login
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent raiz = FXMLLoader.load(
<<<<<<< HEAD
                getClass().getResource("/com/nominaguate/view/PlanillaView.fxml"));

        stage.setTitle("NominaGuate - Procesamiento de Planilla");
        stage.setScene(new Scene(raiz, 900, 550));
=======
                getClass().getResource("/com/nominaguate/view/LoginView.fxml"));

        stage.setTitle("NominaGuate - Inicio de Sesion");
        stage.setScene(new Scene(raiz, 420, 460));
        stage.centerOnScreen();
>>>>>>> feature/implementacion-login
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
