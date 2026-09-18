package com.nominaguate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

<<<<<<< HEAD

=======
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
>>>>>>> origin/main
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent raiz = FXMLLoader.load(
<<<<<<< HEAD
=======
<<<<<<< HEAD
                getClass().getResource("/com/nominaguate/view/PlanillaView.fxml"));

        stage.setTitle("NominaGuate - Procesamiento de Planilla");
        stage.setScene(new Scene(raiz, 900, 550));
=======
>>>>>>> origin/main
                getClass().getResource("/com/nominaguate/view/LoginView.fxml"));

        stage.setTitle("NominaGuate - Inicio de Sesion");
        stage.setScene(new Scene(raiz, 420, 460));
        stage.centerOnScreen();
<<<<<<< HEAD
=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
