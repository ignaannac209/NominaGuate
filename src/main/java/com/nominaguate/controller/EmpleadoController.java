package com.nominaguate.controller;

import com.nominaguate.model.Empleado;
import com.nominaguate.model.Empleado.EstadoEmpleado;
import com.nominaguate.repository.EmpleadoRepository;
import com.nominaguate.service.EmpleadoService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Controlador de la vista de Gestión de Empleados (RRHH).
 */
public class EmpleadoController {

    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, Integer> colId;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colPuesto;
    @FXML private TableColumn<Empleado, BigDecimal> colSalario;
    @FXML private TableColumn<Empleado, String> colFecha;
    @FXML private TableColumn<Empleado, String> colEstado;

    @FXML private TextField txtNombre;
    @FXML private TextField txtPuesto;
    @FXML private TextField txtSalario;
    @FXML private DatePicker dpFechaContratacion;
    @FXML private ComboBox<EstadoEmpleado> cmbEstado;
    @FXML private CheckBox chkSoloActivos;

    @FXML private Button btnRegistrar;
    @FXML private Button btnGuardarCambios;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    private final EmpleadoService empleadoService;
    private Empleado empleadoSeleccionado; // null = modo "nuevo"

    public EmpleadoController() {
        this.empleadoService = new EmpleadoService(new EmpleadoRepository());
    }

    @FXML
    public void initialize() {
        configurarColumnas();
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoEmpleado.values()));
        cmbEstado.getSelectionModel().select(EstadoEmpleado.ACTIVO);

        tblEmpleados.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionado) -> cargarFormulario(seleccionado));

        cargarEmpleados();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salarioActual"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarEmpleados() {
        try {
            ObservableList<Empleado> datos = FXCollections.observableArrayList(
                    chkSoloActivos.isSelected()
                            ? empleadoService.listarActivos()
                            : empleadoService.listarTodos());
            tblEmpleados.setItems(datos);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los empleados", e);
        }
    }

    @FXML
    private void onFiltrarActivos(ActionEvent event) {
        cargarEmpleados();
    }

    private void cargarFormulario(Empleado empleado) {
        empleadoSeleccionado = empleado;
        if (empleado == null) {
            return;
        }
        txtNombre.setText(empleado.getNombreCompleto());
        txtPuesto.setText(empleado.getPuesto());
        txtSalario.setText(empleado.getSalarioActual().toPlainString());
        dpFechaContratacion.setValue(empleado.getFechaContratacion());
        cmbEstado.getSelectionModel().select(empleado.getEstado());
    }

    @FXML
    private void onRegistrar(ActionEvent event) {
        try {
            Empleado nuevo = construirEmpleadoDesdeFormulario(new Empleado());
            empleadoService.registrar(nuevo);
            mostrarInfo("Empleado registrado correctamente.");
            onLimpiar(null);
            cargarEmpleados();
        } catch (IllegalArgumentException e) {
            mostrarAdvertencia(e.getMessage());
        } catch (SQLException e) {
            mostrarError("Error al registrar el empleado", e);
        }
    }

    @FXML
    private void onGuardarCambios(ActionEvent event) {
        if (empleadoSeleccionado == null) {
            mostrarAdvertencia("Selecciona un empleado en la tabla para editar.");
            return;
        }
        try {
            Empleado editado = construirEmpleadoDesdeFormulario(empleadoSeleccionado);
            empleadoService.editar(editado);
            mostrarInfo("Cambios guardados correctamente.");
            onLimpiar(null);
            cargarEmpleados();
        } catch (IllegalArgumentException e) {
            mostrarAdvertencia(e.getMessage());
        } catch (SQLException e) {
            mostrarError("Error al editar el empleado", e);
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        if (empleadoSeleccionado == null) {
            mostrarAdvertencia("Selecciona un empleado en la tabla para dar de baja.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Dar de baja a " + empleadoSeleccionado.getNombreCompleto() + "?");
        confirmacion.showAndWait().ifPresent(boton -> {
            if (boton.getButtonData().isDefaultButton()) {
                try {
                    empleadoService.eliminar(empleadoSeleccionado.getIdEmpleado());
                    mostrarInfo("Empleado dado de baja.");
                    onLimpiar(null);
                    cargarEmpleados();
                } catch (SQLException e) {
                    mostrarError("Error al dar de baja al empleado", e);
                }
            }
        });
    }

    @FXML
    private void onLimpiar(ActionEvent event) {
        empleadoSeleccionado = null;
        tblEmpleados.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtPuesto.clear();
        txtSalario.clear();
        dpFechaContratacion.setValue(null);
        cmbEstado.getSelectionModel().select(EstadoEmpleado.ACTIVO);
    }

    private Empleado construirEmpleadoDesdeFormulario(Empleado base) {
        base.setNombreCompleto(txtNombre.getText());
        base.setPuesto(txtPuesto.getText());
        base.setSalarioActual(parsearSalario(txtSalario.getText()));
        base.setFechaContratacion(dpFechaContratacion.getValue());
        base.setEstado(cmbEstado.getValue());
        return base;
    }

    private BigDecimal parsearSalario(String texto) {
        try {
            return new BigDecimal(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El salario debe ser un número válido, ej. 4500.00");
        }
    }

    // ---------- Utilidades de UI ----------

    private void mostrarError(String mensaje, Exception e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}