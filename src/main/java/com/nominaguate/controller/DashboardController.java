package com.nominaguate.controller;

import com.nominaguate.dto.PeriodoFiltroDTO;
import com.nominaguate.dto.PlanillaResumenDTO;
import com.nominaguate.dto.ResumenDashboardDTO;
import com.nominaguate.dto.TotalDepartamentoDTO;
import com.nominaguate.repository.DashboardRepository;
import com.nominaguate.service.DashboardService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador de la vista de Dashboard Gerencial: KPIs, gráfico por
 * departamento y tabla de planillas generadas.
 */
public class DashboardController {

    @FXML private ComboBox<PeriodoFiltroDTO> cmbFiltroPeriodo;

    @FXML private Label lblTotalPlanillas;
    @FXML private Label lblTotalDevengado;
    @FXML private Label lblTotalDescuentos;
    @FXML private Label lblTotalNeto;
    @FXML private Label lblPromedioNeto;

    @FXML private BarChart<String, Number> chartPorDepartamento;

    @FXML private TableView<PlanillaResumenDTO> tblPlanillas;
    @FXML private TableColumn<PlanillaResumenDTO, Number> colIdPlanilla;
    @FXML private TableColumn<PlanillaResumenDTO, String> colPeriodo;
    @FXML private TableColumn<PlanillaResumenDTO, String> colFecha;
    @FXML private TableColumn<PlanillaResumenDTO, String> colEstado;
    @FXML private TableColumn<PlanillaResumenDTO, Number> colCantidadEmpleados;
    @FXML private TableColumn<PlanillaResumenDTO, Number> colTotalNeto;
    
@FXML
private void onAbrirEmpleados(ActionEvent event) {
    try {
        Parent raiz = FXMLLoader.load(
                getClass().getResource("/com/nominaguate/view/EmpleadoView.fxml"));
        Stage ventanaEmpleados = new Stage();
        ventanaEmpleados.setTitle("NominaGuate - Gestión de Empleados");
        ventanaEmpleados.setScene(new Scene(raiz, 1000, 650));
        ventanaEmpleados.show();
    } catch (IOException e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("No se pudo abrir la vista de Empleados");
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DashboardService dashboardService;

    public DashboardController() {
        this.dashboardService = new DashboardService(new DashboardRepository());
    }

    @FXML
    public void initialize() {
        // Solo ADMIN y RRHH deberían llegar aquí; el login ya enruta según rol,
        // pero se valida de nuevo por si el controlador se invoca desde otro lugar.
        if (SesionActual.haySesionActiva() && SesionActual.getUsuarioActual().esEmpleado()) {
            mostrarError("No tienes permisos para ver el Dashboard Gerencial.");
            return;
        }

        configurarColumnasTabla();
        cargarFiltroPeriodos();
        cargarDatos(null); // sin filtro: vista consolidada de todos los periodos
    }

    private void configurarColumnasTabla() {
        colIdPlanilla.setCellValueFactory(new PropertyValueFactory<>("idPlanilla"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("nombrePeriodo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoPlanilla"));
        colCantidadEmpleados.setCellValueFactory(new PropertyValueFactory<>("cantidadEmpleados"));
        colTotalNeto.setCellValueFactory(new PropertyValueFactory<>("totalNeto"));

        // La fecha necesita formato propio, no viene lista desde el DTO
        colFecha.setCellValueFactory(datos -> {
            var fecha = datos.getValue().fechaGeneracion();
            String texto = fecha != null ? fecha.format(FORMATO_FECHA) : "";
            return new javafx.beans.property.SimpleStringProperty(texto);
        });
    }

    private void cargarFiltroPeriodos() {
        try {
            List<PeriodoFiltroDTO> periodos = dashboardService.listarPeriodosConDatos();
            cmbFiltroPeriodo.setItems(FXCollections.observableArrayList(periodos));
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los periodos: " + e.getMessage());
        }
    }

    @FXML
    private void onFiltrarPeriodo(ActionEvent event) {
        PeriodoFiltroDTO seleccion = cmbFiltroPeriodo.getValue();
        Integer idPeriodo = seleccion != null ? seleccion.idPeriodo() : null;
        cargarDatos(idPeriodo);
    }

    @FXML
    private void onLimpiarFiltro(ActionEvent event) {
        cmbFiltroPeriodo.getSelectionModel().clearSelection();
        cargarDatos(null);
    }

    private void cargarDatos(Integer idPeriodo) {
        cargarResumen(idPeriodo);
        cargarGraficoDepartamentos(idPeriodo);
        cargarTablaPlanillas();
    }

    private void cargarResumen(Integer idPeriodo) {
        try {
            ResumenDashboardDTO resumen = dashboardService.obtenerResumenGeneral(idPeriodo);

            lblTotalPlanillas.setText(String.valueOf(resumen.totalPlanillasGeneradas()));
            lblTotalDevengado.setText(formatearQuetzales(resumen.totalDevengado()));
            lblTotalDescuentos.setText(formatearQuetzales(resumen.totalDescuentos()));
            lblTotalNeto.setText(formatearQuetzales(resumen.totalNeto()));
            lblPromedioNeto.setText(formatearQuetzales(resumen.promedioSalarioNeto()));

        } catch (SQLException e) {
            mostrarError("No se pudo cargar el resumen: " + e.getMessage());
        }
    }

    private void cargarGraficoDepartamentos(Integer idPeriodo) {
        try {
            List<TotalDepartamentoDTO> totales = dashboardService.obtenerTotalesPorDepartamento(idPeriodo);

            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Total neto pagado");

            for (TotalDepartamentoDTO total : totales) {
                serie.getData().add(new XYChart.Data<>(total.nombreDepartamento(), total.totalNeto()));
            }

            chartPorDepartamento.getData().clear();
            chartPorDepartamento.getData().add(serie);

        } catch (SQLException e) {
            mostrarError("No se pudo cargar el gráfico por departamento: " + e.getMessage());
        }
    }

    private void cargarTablaPlanillas() {
        try {
            List<PlanillaResumenDTO> planillas = dashboardService.listarPlanillasGeneradas();
            tblPlanillas.setItems(FXCollections.observableArrayList(planillas));
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las planillas generadas: " + e.getMessage());
        }
    }

    private String formatearQuetzales(java.math.BigDecimal monto) {
        if (monto == null) return "Q 0.00";
        return "Q " + monto.setScale(2, RoundingMode.HALF_UP);
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
