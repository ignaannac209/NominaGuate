package com.nominaguate.repository;

import com.nominaguate.config.DataBaseConnection;
import com.nominaguate.dto.PeriodoFiltroDTO;
import com.nominaguate.dto.PlanillaResumenDTO;
import com.nominaguate.dto.ResumenDashboardDTO;
import com.nominaguate.dto.TotalDepartamentoDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de solo lectura para el Dashboard Gerencial.
 *
 * Todas las consultas parten de vw_boletas_planilla (ya existente), que trae
 * boleta + planilla + periodo + empleado + departamento ya resueltos por JOIN.
 * Si el equipo ya tiene vistas dedicadas (p. ej. vw_resumen_planilla o
 * vw_planillas_generadas) con estos mismos agregados, basta con reemplazar
 * el SQL de cada método por un SELECT directo a esa vista; las firmas de los
 * métodos y los DTOs no cambian.
 */
public class DashboardRepository {

    // Tarjetas de KPIs del encabezado del Dashboard
    public ResumenDashboardDTO obtenerResumenGeneral(Integer idPeriodo) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT id_planilla) AS total_planillas, " +
                        "COUNT(*) AS total_boletas, " +
                        "COALESCE(SUM(total_devengado), 0) AS total_devengado, " +
                        "COALESCE(SUM(total_descuentos), 0) AS total_descuentos, " +
                        "COALESCE(SUM(salario_neto), 0) AS total_neto, " +
                        "COALESCE(AVG(salario_neto), 0) AS promedio_neto " +
                        "FROM vw_boletas_planilla WHERE estado_boleta <> 'ANULADA'");

        if (idPeriodo != null) {
            sql.append(" AND id_periodo = ?");
        }

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (idPeriodo != null) {
                ps.setInt(1, idPeriodo);
            }

            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); // la agregación siempre devuelve una fila
                return new ResumenDashboardDTO(
                        rs.getLong("total_planillas"),
                        rs.getLong("total_boletas"),
                        rs.getBigDecimal("total_devengado"),
                        rs.getBigDecimal("total_descuentos"),
                        rs.getBigDecimal("total_neto"),
                        rs.getBigDecimal("promedio_neto")
                );
            }
        }
    }

    // Tabla "Planillas generadas": una fila por corrida de planilla
    public List<PlanillaResumenDTO> listarPlanillasGeneradas() throws SQLException {
        String sql = "SELECT id_planilla, nombre_periodo, MIN(fecha_emision) AS fecha_generacion, " +
                "estado_planilla, COUNT(*) AS cantidad_empleados, " +
                "SUM(total_devengado) AS total_devengado, SUM(total_descuentos) AS total_descuentos, " +
                "SUM(salario_neto) AS total_neto " +
                "FROM vw_boletas_planilla " +
                "GROUP BY id_planilla, nombre_periodo, estado_planilla " +
                "ORDER BY fecha_generacion DESC";

        List<PlanillaResumenDTO> planillas = new ArrayList<>();

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                planillas.add(new PlanillaResumenDTO(
                        rs.getInt("id_planilla"),
                        rs.getString("nombre_periodo"),
                        rs.getTimestamp("fecha_generacion").toLocalDateTime(),
                        rs.getString("estado_planilla"),
                        rs.getLong("cantidad_empleados"),
                        rs.getBigDecimal("total_devengado"),
                        rs.getBigDecimal("total_descuentos"),
                        rs.getBigDecimal("total_neto")
                ));
            }
        }
        return planillas;
    }

    // Gráfico de barras: total neto pagado por departamento (planilla más reciente, o histórico si idPeriodo es null)
    public List<TotalDepartamentoDTO> obtenerTotalesPorDepartamento(Integer idPeriodo) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT nombre_departamento, SUM(salario_neto) AS total_neto " +
                        "FROM vw_boletas_planilla WHERE estado_boleta <> 'ANULADA'");

        if (idPeriodo != null) {
            sql.append(" AND id_periodo = ?");
        }
        sql.append(" GROUP BY nombre_departamento ORDER BY total_neto DESC");

        List<TotalDepartamentoDTO> totales = new ArrayList<>();

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (idPeriodo != null) {
                ps.setInt(1, idPeriodo);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totales.add(new TotalDepartamentoDTO(
                            rs.getString("nombre_departamento"),
                            rs.getBigDecimal("total_neto")
                    ));
                }
            }
        }
        return totales;
    }

    // Combo de filtro por periodo en el Dashboard (solo periodos que ya tienen boletas)
    public List<PeriodoFiltroDTO> listarPeriodosConDatos() throws SQLException {
        String sql = "SELECT DISTINCT id_periodo, nombre_periodo FROM vw_boletas_planilla ORDER BY id_periodo DESC";
        List<PeriodoFiltroDTO> periodos = new ArrayList<>();

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                periodos.add(new PeriodoFiltroDTO(rs.getInt("id_periodo"), rs.getString("nombre_periodo")));
            }
        }
        return periodos;
    }
}
