package com.nominaguate.controller;

import com.nominaguate.model.RolUsuario;
import com.nominaguate.model.Usuario;
import com.nominaguate.repository.UsuarioRepository;
import com.nominaguate.service.AutenticacionService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controlador de la vista de inicio de sesión.
 */
public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private Button btnIngresar;
    @FXML private Label lblError;
    @FXML private ProgressIndicator progresoLogin;

    private final AutenticacionService autenticacionService;

    public LoginController() {
        this.autenticacionService = new AutenticacionService(new UsuarioRepository());
    }

    @FXML
    public void initialize() {
        lblError.setText("");
        lblError.setVisible(false);
        progresoLogin.setVisible(false);

        // Permite iniciar sesión presionando Enter dentro del campo de clave
        txtClave.setOnAction(this::onIngresar);
    }

    @FXML
    private void onIngresar(ActionEvent event) {
        String nombreUsuario = txtUsuario.getText();
        String clave = txtClave.getText();

        if (nombreUsuario == null || nombreUsuario.isBlank() || clave == null || clave.isBlank()) {
            mostrarError("Ingresa usuario y contraseña.");
            return;
        }

        btnIngresar.setDisable(true);
        progresoLogin.setVisible(true);
        lblError.setVisible(false);

        try {
            Optional<Usuario> usuarioAutenticado = autenticacionService.autenticar(nombreUsuario, clave);

            if (usuarioAutenticado.isEmpty()) {
                mostrarError("Usuario o contraseña incorrectos.");
                return;
            }

            SesionActual.iniciarSesion(usuarioAutenticado.get());
            abrirVentanaPrincipal(usuarioAutenticado.get());

        } catch (SQLException e) {
            mostrarError("No se pudo conectar con la base de datos.");
        } catch (IOException e) {
            mostrarError("No se pudo cargar la siguiente pantalla.");
        } finally {
            btnIngresar.setDisable(false);
            progresoLogin.setVisible(false);
        }
    }

    // Enruta a la ventana correspondiente según el rol del usuario autenticado
    private void abrirVentanaPrincipal(Usuario usuario) throws IOException {
        String vista = (usuario.getRol() == RolUsuario.ADMIN || usuario.getRol() == RolUsuario.RRHH)
                ? "/com/nominaguate/view/DashboardView.fxml"
                : "/com/nominaguate/view/PlanillaView.fxml"; // TODO: vista propia para EMPLEADO (ver sus boletas)

        Parent raiz = FXMLLoader.load(getClass().getResource(vista));

        Stage escenarioActual = (Stage) btnIngresar.getScene().getWindow();
        escenarioActual.setScene(new Scene(raiz, 1000, 650));
        escenarioActual.setTitle("NominaGuate - " + usuario.getNombreCompleto());
        escenarioActual.centerOnScreen();
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    // Navega a la pantalla de registro de nuevos usuarios
    @FXML
    private void onIrARegistro(ActionEvent event) {
        try {
            Parent raiz = FXMLLoader.load(getClass().getResource("/com/nominaguate/view/RegistroView.fxml"));
            Stage escenarioActual = (Stage) btnIngresar.getScene().getWindow();
            escenarioActual.setScene(new Scene(raiz, 420, 560));
            escenarioActual.setTitle("NominaGuate - Registro de Usuario");
            escenarioActual.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo abrir la pantalla de registro.");
        }
    }

    // Utilidad opcional si se prefiere Alert en vez del Label inline
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
