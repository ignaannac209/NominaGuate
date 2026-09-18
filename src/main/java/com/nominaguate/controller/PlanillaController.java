package com.nominaguate.controller;

import com.nominaguate.dto.BoletaPagoVistaDTO;
import com.nominaguate.model.Empleado;
import com.nominaguate.model.PeriodoPlanilla;
import com.nominaguate.model.PlanillaCabecera;
import com.nominaguate.model.PlanillaDetalle;
import com.nominaguate.repository.PlanillaRepository;
import com.nominaguate.service.PlanillaService;
import com.nominaguate.service.ResumenAsistenciaEmpleado;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controlador de la vista de Procesamiento de Planilla.
 */
public class PlanillaController {

    @FXML private ComboBox<PeriodoPlanilla> cmbPeriodo;
    @FXML private TableView<PlanillaDetalle> tblDetalle;
    @FXML private TableColumn<PlanillaDetalle, Number> colEmpleado;
    @FXML private TableColumn<PlanillaDetalle, Number> colDevengado;
    @FXML private TableColumn<PlanillaDetalle, Number> colIgss;
    @FXML private TableColumn<PlanillaDetalle, Number> colIsr;
    @FXML private TableColumn<PlanillaDetalle, Number> colNeto;
    @FXML private Label lblTotalNeto;
    @FXML private Button btnProcesar;
    @FXML private ProgressIndicator progresoCalculo;

    private final PlanillaService planillaService;

    // Cargados desde los módulos de Empleados y Asistencia antes de procesar
    private List<Empleado> empleadosActivos;
    private Map<Integer, ResumenAsistenciaEmpleado> resumenAsistencia;

    public PlanillaController() {
        this.planillaService = new PlanillaService(new PlanillaRepository());
    }

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarPeriodos(); // llena el combo al abrir la vista
    }

    private void configurarColumnas() {
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        colDevengado.setCellValueFactory(new PropertyValueFactory<>("totalDevengado"));
        colIgss.setCellValueFactory(new PropertyValueFactory<>("igssLaboral"));
        colIsr.setCellValueFactory(new PropertyValueFactory<>("isr"));
        colNeto.setCellValueFactory(new PropertyValueFactory<>("salarioNeto"));
    }

    private void cargarPeriodos() {
        try {
            ObservableList<PeriodoPlanilla> periodos =
                    FXCollections.observableArrayList(planillaService.listarPeriodosAbiertos());
            cmbPeriodo.setItems(periodos);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los periodos", e);
        }
    }

    // Se dispara al elegir un periodo en el ComboBox
    @FXML
    private void onSeleccionarPeriodo(ActionEvent event) {
        PeriodoPlanilla periodo = cmbPeriodo.getValue();
        if (periodo == null) return;

        tblDetalle.getItems().clear();
        lblTotalNeto.setText("Q 0.00");
        // Aquí se cargan empleadosActivos y resumenAsistencia desde sus módulos
    }

    // Botón "Procesar Planilla"
    @FXML
    private void onProcesarPlanilla(ActionEvent event) {
        PeriodoPlanilla periodo = cmbPeriodo.getValue();
        if (periodo == null) {
            mostrarAdvertencia("Selecciona un periodo antes de procesar.");
            return;
        }
        if (empleadosActivos == null || empleadosActivos.isEmpty()) {
            mostrarAdvertencia("No hay empleados activos para calcular.");
            return;
        }

        btnProcesar.setDisable(true);
        progresoCalculo.setVisible(true);

        try {
            Integer idUsuarioGenero = SesionActual.getIdUsuario(); // sesión ya autenticada
            PlanillaCabecera cabecera = planillaService.procesarPlanilla(
                    periodo, empleadosActivos, resumenAsistencia, idUsuarioGenero);

            mostrarDetalleEnTabla(cabecera);
            mostrarInfo("Planilla procesada. Total neto: Q " + cabecera.getTotalNeto());

        } catch (SQLException e) {
            mostrarError("Error al procesar la planilla", e);
        } finally {
            btnProcesar.setDisable(false);
            progresoCalculo.setVisible(false);
        }
    }

    private void mostrarDetalleEnTabla(PlanillaCabecera cabecera) {
        tblDetalle.setItems(FXCollections.observableArrayList(cabecera.getDetalles()));
        lblTotalNeto.setText("Q " + cabecera.getTotalNeto());
    }

    // Abre la vista previa de boleta para la fila seleccionada
    @FXML
    private void onVerBoleta(ActionEvent event) {
        PlanillaDetalle seleccion = tblDetalle.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarAdvertencia("Selecciona un empleado en la tabla.");
            return;
        }

        try {
            List<BoletaPagoVistaDTO> boletas =
                    planillaService.obtenerBoletasDePlanilla(seleccion.getIdPlanilla());

            boletas.stream()
                    .filter(b -> Integer.valueOf(b.idEmpleado()).equals(seleccion.getIdEmpleado()))
                    .findFirst()
                    .ifPresentOrElse(this::abrirVentanaBoleta,
                            () -> mostrarAdvertencia("Boleta no encontrada."));

        } catch (SQLException e) {
            mostrarError("Error al consultar la boleta", e);
        }
    }

    private void abrirVentanaBoleta(BoletaPagoVistaDTO boleta) {
        // Aquí se carga el FXML de vista previa e inyecta el DTO
        mostrarInfo("Boleta " + boleta.codigoBoleta() + " - Neto: Q " + boleta.salarioNeto());
    }

    // Utilidades de UI

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