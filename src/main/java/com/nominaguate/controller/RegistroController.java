package com.nominaguate.controller;

import com.nominaguate.model.RolUsuario;
import com.nominaguate.model.Usuario;
import com.nominaguate.repository.UsuarioRepository;
import com.nominaguate.util.PasswordUtil;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controlador de la vista de registro de nuevos usuarios (RegistroView.fxml).
 */
public class RegistroController {

    private static final int LONGITUD_MINIMA_CLAVE = 8;

    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private ComboBox<RolUsuario> cmbRol;
    @FXML private Label lblMensaje;
    @FXML private ProgressIndicator progresoRegistro;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;

    private final UsuarioRepository usuarioRepository;

    public RegistroController() {
        this.usuarioRepository = new UsuarioRepository();
    }

    @FXML
    public void initialize() {
        cmbRol.setItems(FXCollections.observableArrayList(RolUsuario.values()));
        ocultarMensaje();
        progresoRegistro.setVisible(false);
    }

    @FXML
    private void handleRegistrar(ActionEvent event) {
        String nombreCompleto = valorSeguro(txtNombreCompleto);
        String nombreUsuario = valorSeguro(txtUsuario);
        String clave = txtPassword.getText() == null ? "" : txtPassword.getText();
        String confirmacion = txtConfirmarPassword.getText() == null ? "" : txtConfirmarPassword.getText();
        RolUsuario rolSeleccionado = cmbRol.getValue();

        // 1. Campos obligatorios
        if (nombreCompleto.isBlank() || nombreUsuario.isBlank() || clave.isBlank()
                || confirmacion.isBlank() || rolSeleccionado == null) {
            mostrarError("Completa todos los campos antes de continuar.");
            return;
        }

        // 2. Contraseñas coinciden
        if (!clave.equals(confirmacion)) {
            mostrarError("Las contraseñas no coinciden.");
            return;
        }

        // 3. Longitud mínima razonable
        if (clave.length() < LONGITUD_MINIMA_CLAVE) {
            mostrarError("La contraseña debe tener al menos " + LONGITUD_MINIMA_CLAVE + " caracteres.");
            return;
        }

        establecerCargando(true);

        try {
            // 4. Unicidad del nombre de usuario
            Optional<Usuario> existente = usuarioRepository.buscarPorNombreUsuario(nombreUsuario);
            if (existente.isPresent()) {
                mostrarError("Ese nombre de usuario ya está en uso.");
                return;
            }

            // 5. Hash de la contraseña 
            String claveHasheada = PasswordUtil.hashear(clave);

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreCompleto(nombreCompleto);
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            nuevoUsuario.setClaveHash(claveHasheada);
            nuevoUsuario.setRol(rolSeleccionado);
            nuevoUsuario.setActivo(true);
          

            usuarioRepository.crearUsuario(nuevoUsuario);

            mostrarExito("Usuario registrado correctamente. Redirigiendo al inicio de sesión...");
            limpiarFormulario();
            volverALogin(event, 1200);

        } catch (SQLException e) {
            // Registrar el stack trace en consola para diagnóstico
            System.err.println("[RegistroController] Error SQL al registrar usuario:");
            e.printStackTrace();

            if (esViolacionDeUnicidad(e)) {
                mostrarError("Ese nombre de usuario ya está en uso.");
            } else {
                mostrarError("No se pudo conectar con la base de datos.");
            }
        } finally {
            establecerCargando(false);
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        try {
            irALogin(event);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de inicio de sesión.");
        }
    }

    // Navegación

    private void volverALogin(ActionEvent event, long retardoMs) {
        if (retardoMs <= 0) {
            try {
                irALogin(event);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarError("Registro exitoso, pero no se pudo cargar el inicio de sesión.");
            }
            return;
        }

        // Pequeña pausa para que el usuario alcance a leer el mensaje de éxito
        javafx.animation.PauseTransition pausa = new javafx.animation.PauseTransition(
                javafx.util.Duration.millis(retardoMs));
        pausa.setOnFinished(e -> {
            try {
                irALogin(event);
            } catch (IOException ex) {
                ex.printStackTrace();
                mostrarError("Registro exitoso, pero no se pudo cargar el inicio de sesión.");
            }
        });
        pausa.play();
    }

    private void irALogin(ActionEvent event) throws IOException {
               Parent raiz = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        Stage escenarioActual = (Stage) ((Button) event.getSource()).getScene().getWindow();
        escenarioActual.setScene(new Scene(raiz, 420, 460));
        escenarioActual.setTitle("NominaGuate - Inicio de Sesion");
        escenarioActual.centerOnScreen();
    }

    // Utilidades de UI

    private void establecerCargando(boolean cargando) {
        progresoRegistro.setVisible(cargando);
        btnRegistrar.setDisable(cargando);
        btnCancelar.setDisable(cargando);
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #c0392b;"); // rojo
        lblMensaje.setVisible(true);
        lblMensaje.setManaged(true);
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #27ae60;"); // verde
        lblMensaje.setVisible(true);
        lblMensaje.setManaged(true);
    }

    private void ocultarMensaje() {
        lblMensaje.setText("");
        lblMensaje.setVisible(false);
        lblMensaje.setManaged(false);
    }

    private void limpiarFormulario() {
        txtNombreCompleto.clear();
        txtUsuario.clear();
        txtPassword.clear();
        txtConfirmarPassword.clear();
        cmbRol.getSelectionModel().clearSelection();
    }

    private String valorSeguro(TextField campo) {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    // MySQL: SQLState 23000 / código 1062 = violación de restricción UNIQUE
    private boolean esViolacionDeUnicidad(SQLException e) {
        return "23000".equals(e.getSQLState()) || e.getErrorCode() == 1062;
    }
}
