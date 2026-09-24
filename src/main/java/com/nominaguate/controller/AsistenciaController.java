package com.nominaguate.controller;

import com.nominaguate.service.AsistenciaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AsistenciaController {

    @FXML 
    private TextField txtEmpleadoId;

    private AsistenciaService asistenciaService;

    @FXML
    public void initialize() {
        this.asistenciaService = new AsistenciaService();
    }

    @FXML
    private void handleMarcarEntrada() {
        try {
            String input = txtEmpleadoId.getText().trim();
            if (input.isEmpty()) {
                mostrarAlerta("Atención", "Por favor ingrese el ID del empleado.", Alert.AlertType.WARNING);
                return;
            }
            int empId = Integer.parseInt(input);
            if (asistenciaService.marcarEntrada(empId)) {
                mostrarAlerta("Éxito", "Entrada registrada correctamente.", Alert.AlertType.INFORMATION);
                txtEmpleadoId.clear();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de empleado debe ser un número entero.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al registrar la entrada: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleMarcarSalida() {
        try {
            String input = txtEmpleadoId.getText().trim();
            if (input.isEmpty()) {
                mostrarAlerta("Atención", "Por favor ingrese el ID del empleado.", Alert.AlertType.WARNING);
                return;
            }
            int empId = Integer.parseInt(input);
            if (asistenciaService.marcarSalida(empId)) {
                mostrarAlerta("Éxito", "Salida registrada y horas calculadas exitosamente.", Alert.AlertType.INFORMATION);
                txtEmpleadoId.clear();
            } else {
                mostrarAlerta("Atención", "No se encontró registro de entrada activo para hoy.", Alert.AlertType.WARNING);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de empleado debe ser un número entero.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al registrar la salida: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        try {
                      Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("NominaGuate - Dashboard Gerencial");
            stage.centerOnScreen();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al Dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}