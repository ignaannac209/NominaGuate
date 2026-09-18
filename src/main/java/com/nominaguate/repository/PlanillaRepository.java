package com.nominaguate.repository;

<<<<<<< HEAD
import com.nominaguate.config.DataBaseConnection;
import com.nominaguate.dto.BoletaPagoVistaDTO;
=======
<<<<<<< HEAD
import com.nominaguate.config.ConexionBD;
=======
import com.nominaguate.config.DataBaseConnection;
import com.nominaguate.dto.BoletaPagoVistaDTO;
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
import com.nominaguate.model.ConceptoNomina;
import com.nominaguate.model.ConceptoNomina.NaturalezaConcepto;
import com.nominaguate.model.ConceptoNomina.TipoConcepto;
import com.nominaguate.model.PeriodoPlanilla;
import com.nominaguate.model.PeriodoPlanilla.EstadoPeriodo;
import com.nominaguate.model.PeriodoPlanilla.TipoPeriodo;
import com.nominaguate.model.PlanillaCabecera;
import com.nominaguate.model.PlanillaDetalle;
import com.nominaguate.model.PlanillaDetalleConcepto;
<<<<<<< HEAD
=======
<<<<<<< HEAD
import com.nominaguate.dto.BoletaPagoVistaDTO;
=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Acceso a datos del módulo de Planilla. Toda operación usa PreparedStatement.
 */
public class PlanillaRepository {

<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
    // Lista periodos filtrados por estado, para llenar el ComboBox
    public List<PeriodoPlanilla> listarPeriodosPorEstado(EstadoPeriodo estado) throws SQLException {
        String sql = "SELECT id_periodo, nombre_periodo, fecha_inicio, fecha_fin, tipo_periodo, estado " +
                "FROM periodos_planilla WHERE estado = ? ORDER BY fecha_inicio DESC";
        List<PeriodoPlanilla> periodos = new ArrayList<>();

<<<<<<< HEAD
        try (Connection con = DataBaseConnection.getConnectionDataBase();
=======
<<<<<<< HEAD
        try (Connection con = ConexionBD.obtenerConexion();
=======
        try (Connection con = DataBaseConnection.getConnectionDataBase();
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    periodos.add(mapearPeriodo(rs));
                }
            }
        }
        return periodos;
    }

    private PeriodoPlanilla mapearPeriodo(ResultSet rs) throws SQLException {
        return new PeriodoPlanilla(
                rs.getInt("id_periodo"),
                rs.getString("nombre_periodo"),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate(),
                TipoPeriodo.valueOf(rs.getString("tipo_periodo")),
                EstadoPeriodo.valueOf(rs.getString("estado"))
        );
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
    // Conceptos activos indexados por código, listos para el Servicio
    public Map<String, ConceptoNomina> listarConceptosActivos() throws SQLException {
        String sql = "SELECT id_concepto, codigo_concepto, nombre_concepto, tipo_concepto, " +
                "naturaleza, porcentaje, estado FROM conceptos_nomina WHERE estado = 'ACTIVO'";
        Map<String, ConceptoNomina> conceptos = new HashMap<>();

<<<<<<< HEAD
        try (Connection con = DataBaseConnection.getConnectionDataBase();
=======
<<<<<<< HEAD
        try (Connection con = ConexionBD.obtenerConexion();
=======
        try (Connection con = DataBaseConnection.getConnectionDataBase();
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ConceptoNomina concepto = new ConceptoNomina();
                concepto.setIdConcepto(rs.getInt("id_concepto"));
                concepto.setCodigoConcepto(rs.getString("codigo_concepto"));
                concepto.setNombreConcepto(rs.getString("nombre_concepto"));
                concepto.setTipoConcepto(TipoConcepto.valueOf(rs.getString("tipo_concepto")));
                concepto.setNaturaleza(NaturalezaConcepto.valueOf(rs.getString("naturaleza")));
                concepto.setPorcentaje(rs.getBigDecimal("porcentaje"));
                concepto.setEstado(ConceptoNomina.EstadoConcepto.valueOf(rs.getString("estado")));
                conceptos.put(concepto.getCodigoConcepto(), concepto);
            }
        }
        return conceptos;
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
    // Inserta cabecera, detalles, desglose de conceptos y boletas en un solo bloque atómico
    public PlanillaCabecera procesarPlanilla(PlanillaCabecera cabecera) throws SQLException {
        String sqlCabecera = "INSERT INTO planilla_cabecera " +
                "(id_periodo, id_usuario_genero, total_devengado, total_descuentos, total_neto, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO planilla_detalle " +
                "(id_planilla, id_empleado, salario_base, dias_laborados, horas_extra, monto_horas_extra, " +
                "bonificacion_incentivo, total_otros_devengados, total_devengado, igss_laboral, isr, " +
                "total_otros_descuentos, total_descuentos, salario_neto, estado) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlConcepto = "INSERT INTO planilla_detalle_conceptos (id_detalle, id_concepto, monto) " +
                "VALUES (?, ?, ?)";
        String sqlBoleta = "INSERT INTO boletas_pago (id_detalle, codigo_boleta, estado) VALUES (?, ?, 'EMITIDA')";

<<<<<<< HEAD
=======
<<<<<<< HEAD
        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false); // inicio de transacción

            int idPlanilla = insertarCabecera(con, sqlCabecera, cabecera);
            cabecera.setIdPlanilla(idPlanilla);

            for (PlanillaDetalle detalle : cabecera.getDetalles()) {
                detalle.setIdPlanilla(idPlanilla);
                int idDetalle = insertarDetalle(con, sqlDetalle, detalle);
                detalle.setIdDetalle(idDetalle);

                insertarConceptos(con, sqlConcepto, idDetalle, detalle.getConceptos());
                insertarBoleta(con, sqlBoleta, idDetalle);
            }

            con.commit(); // todo o nada
            return cabecera;

        } catch (SQLException e) {
            if (con != null) con.rollback(); // deshace todo ante cualquier fallo
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
=======
>>>>>>> origin/main
        try (Connection con = DataBaseConnection.getConnectionDataBase()) {
            con.setAutoCommit(false); // inicio de transacción

            try {
                int idPlanilla = insertarCabecera(con, sqlCabecera, cabecera);
                cabecera.setIdPlanilla(idPlanilla);

                for (PlanillaDetalle detalle : cabecera.getDetalles()) {
                    detalle.setIdPlanilla(idPlanilla);
                    int idDetalle = insertarDetalle(con, sqlDetalle, detalle);
                    detalle.setIdDetalle(idDetalle);

                    insertarConceptos(con, sqlConcepto, idDetalle, detalle.getConceptos());
                    insertarBoleta(con, sqlBoleta, idDetalle);
                }

                con.commit(); // todo o nada
                return cabecera;
            } catch (SQLException e) {
                con.rollback(); // deshace todo ante cualquier fallo
                throw e;
            } finally {
                con.setAutoCommit(true);
<<<<<<< HEAD
=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
            }
        }
    }

    private int insertarCabecera(Connection con, String sql, PlanillaCabecera cabecera) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cabecera.getIdPeriodo());
            ps.setInt(2, cabecera.getIdUsuarioGenero());
            ps.setBigDecimal(3, cabecera.getTotalDevengado());
            ps.setBigDecimal(4, cabecera.getTotalDescuentos());
            ps.setBigDecimal(5, cabecera.getTotalNeto());
            ps.setString(6, cabecera.getEstado().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private int insertarDetalle(Connection con, String sql, PlanillaDetalle detalle) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getIdPlanilla());
            ps.setInt(2, detalle.getIdEmpleado());
            ps.setBigDecimal(3, detalle.getSalarioBase());
            ps.setBigDecimal(4, detalle.getDiasLaborados());
            ps.setBigDecimal(5, detalle.getHorasExtra());
            ps.setBigDecimal(6, detalle.getMontoHorasExtra());
            ps.setBigDecimal(7, detalle.getBonificacionIncentivo());
            ps.setBigDecimal(8, detalle.getTotalOtrosDevengados());
            ps.setBigDecimal(9, detalle.getTotalDevengado());
            ps.setBigDecimal(10, detalle.getIgssLaboral());
            ps.setBigDecimal(11, detalle.getIsr());
            ps.setBigDecimal(12, detalle.getTotalOtrosDescuentos());
            ps.setBigDecimal(13, detalle.getTotalDescuentos());
            ps.setBigDecimal(14, detalle.getSalarioNeto());
            ps.setString(15, detalle.getEstado().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private void insertarConceptos(Connection con, String sql, int idDetalle,
                                    List<PlanillaDetalleConcepto> conceptos) throws SQLException {
<<<<<<< HEAD
        if (conceptos == null || conceptos.isEmpty()) return;
=======
<<<<<<< HEAD
        if (conceptos.isEmpty()) return;
=======
        if (conceptos == null || conceptos.isEmpty()) return;
>>>>>>> feature/implementacion-login
>>>>>>> origin/main

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (PlanillaDetalleConcepto concepto : conceptos) {
                ps.setInt(1, idDetalle);
                ps.setInt(2, concepto.getIdConcepto());
                ps.setBigDecimal(3, concepto.getMonto());
                ps.addBatch(); // inserta el desglose en lote
            }
            ps.executeBatch();
        }
    }

    private void insertarBoleta(Connection con, String sql, int idDetalle) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            ps.setString(2, generarCodigoBoleta(idDetalle));
            ps.executeUpdate();
        }
    }

    private String generarCodigoBoleta(int idDetalle) {
<<<<<<< HEAD
        return "BOL-" + LocalDate.now().getYear() + "-" + idDetalle;
    }

=======
<<<<<<< HEAD
        return "BOL-" + LocalDate.now().getYear() + "-" + idDetalle; // código simple y único
    }


=======
        return "BOL-" + LocalDate.now().getYear() + "-" + idDetalle;
    }

>>>>>>> feature/implementacion-login
>>>>>>> origin/main
    // Revierte una planilla completa (cabecera, detalles y boletas)
    public void anularPlanilla(int idPlanilla) throws SQLException {
        String sqlBoleta = "UPDATE boletas_pago b JOIN planilla_detalle d ON d.id_detalle = b.id_detalle " +
                "SET b.estado = 'ANULADA' WHERE d.id_planilla = ?";
        String sqlDetalle = "UPDATE planilla_detalle SET estado = 'ANULADO' WHERE id_planilla = ?";
        String sqlCabecera = "UPDATE planilla_cabecera SET estado = 'ANULADA' WHERE id_planilla = ?";

<<<<<<< HEAD
=======
<<<<<<< HEAD
        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlBoleta)) {
                ps.setInt(1, idPlanilla);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                ps.setInt(1, idPlanilla);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(sqlCabecera)) {
                ps.setInt(1, idPlanilla);
                ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
=======
>>>>>>> origin/main
        try (Connection con = DataBaseConnection.getConnectionDataBase()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps = con.prepareStatement(sqlBoleta)) {
                    ps.setInt(1, idPlanilla);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                    ps.setInt(1, idPlanilla);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sqlCabecera)) {
                    ps.setInt(1, idPlanilla);
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
<<<<<<< HEAD
=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
            }
        }
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
    // Trae todas las boletas de una corrida, ya con JOINs resueltos en la vista
    public List<BoletaPagoVistaDTO> listarBoletasPorPlanilla(int idPlanilla) throws SQLException {
        String sql = "SELECT * FROM vw_boletas_planilla WHERE id_planilla = ? ORDER BY empleado";
        List<BoletaPagoVistaDTO> boletas = new ArrayList<>();

<<<<<<< HEAD
        try (Connection con = DataBaseConnection.getConnectionDataBase();
=======
<<<<<<< HEAD
        try (Connection con = ConexionBD.obtenerConexion();
=======
        try (Connection con = DataBaseConnection.getConnectionDataBase();
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPlanilla);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boletas.add(mapearBoletaVista(rs));
                }
            }
        }
        return boletas;
    }

    // Boleta individual, para la vista previa de impresión
    public Optional<BoletaPagoVistaDTO> obtenerBoletaPorId(int idBoleta) throws SQLException {
        String sql = "SELECT * FROM vw_boletas_planilla WHERE id_boleta = ?";

<<<<<<< HEAD
        try (Connection con = DataBaseConnection.getConnectionDataBase();
=======
<<<<<<< HEAD
        try (Connection con = ConexionBD.obtenerConexion();
=======
        try (Connection con = DataBaseConnection.getConnectionDataBase();
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBoleta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearBoletaVista(rs));
                }
            }
        }
        return Optional.empty();
    }

    private BoletaPagoVistaDTO mapearBoletaVista(ResultSet rs) throws SQLException {
        return new BoletaPagoVistaDTO(
                rs.getInt("id_boleta"),
                rs.getString("codigo_boleta"),
                rs.getTimestamp("fecha_emision").toLocalDateTime(),
                rs.getString("estado_boleta"),
                rs.getInt("id_periodo"),
                rs.getString("nombre_periodo"),
                rs.getInt("id_planilla"),
                rs.getString("estado_planilla"),
                rs.getInt("id_empleado"),
                rs.getString("empleado"),
                rs.getString("nombre_departamento"),
                rs.getString("nombre_puesto"),
                rs.getBigDecimal("salario_base"),
                rs.getBigDecimal("dias_laborados"),
                rs.getBigDecimal("horas_extra"),
                rs.getBigDecimal("monto_horas_extra"),
                rs.getBigDecimal("bonificacion_incentivo"),
                rs.getBigDecimal("total_devengado"),
                rs.getBigDecimal("igss_laboral"),
                rs.getBigDecimal("isr"),
                rs.getBigDecimal("total_descuentos"),
                rs.getBigDecimal("salario_neto"),
                rs.getString("desglose_conceptos")
        );
    }
<<<<<<< HEAD
}
=======
<<<<<<< HEAD
}
=======
}
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
