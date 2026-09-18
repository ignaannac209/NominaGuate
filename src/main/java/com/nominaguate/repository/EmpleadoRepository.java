package com.nominaguate.repository;

import com.nominaguate.config.ConexionBD;
import com.nominaguate.model.Empleado;
import com.nominaguate.model.Empleado.EstadoEmpleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos del módulo de Gestión de Empleados (RRHH).
 * Toda operación usa PreparedStatement.
 *
 * NOTA: ajustar el nombre de la tabla/columnas si en tu script de BD
 * no coinciden exactamente con "empleados" (id_empleado, nombre_completo,
 * puesto, salario_actual, fecha_contratacion, estado).
 */
public class EmpleadoRepository {

    public List<Empleado> listarTodos() throws SQLException {
        String sql = "SELECT id_empleado, nombre_completo, puesto, salario_actual, " +
                "fecha_contratacion, estado FROM empleados ORDER BY nombre_completo";
        List<Empleado> empleados = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        }
        return empleados;
    }

    public List<Empleado> listarActivos() throws SQLException {
        String sql = "SELECT id_empleado, nombre_completo, puesto, salario_actual, " +
                "fecha_contratacion, estado FROM empleados WHERE estado = 'ACTIVO' " +
                "ORDER BY nombre_completo";
        List<Empleado> empleados = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        }
        return empleados;
    }

    public Empleado insertar(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre_completo, puesto, salario_actual, " +
                "fecha_contratacion, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, empleado.getNombreCompleto());
            ps.setString(2, empleado.getPuesto());
            ps.setBigDecimal(3, empleado.getSalarioActual());
            ps.setDate(4, java.sql.Date.valueOf(empleado.getFechaContratacion()));
            ps.setString(5, empleado.getEstado().name());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    empleado.setIdEmpleado(keys.getInt(1));
                }
            }
        }
        return empleado;
    }

    public void actualizar(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleados SET nombre_completo = ?, puesto = ?, " +
                "salario_actual = ?, fecha_contratacion = ?, estado = ? WHERE id_empleado = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, empleado.getNombreCompleto());
            ps.setString(2, empleado.getPuesto());
            ps.setBigDecimal(3, empleado.getSalarioActual());
            ps.setDate(4, java.sql.Date.valueOf(empleado.getFechaContratacion()));
            ps.setString(5, empleado.getEstado().name());
            ps.setInt(6, empleado.getIdEmpleado());

            ps.executeUpdate();
        }
    }

    // Baja lógica: se deja como INACTIVO en vez de borrar la fila, porque
    // el empleado puede ya tener planillas procesadas asociadas a su id.
    public void eliminar(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET estado = 'INACTIVO' WHERE id_empleado = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);
            ps.executeUpdate();
        }
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("id_empleado"),
                rs.getString("nombre_completo"),
                rs.getString("puesto"),
                rs.getBigDecimal("salario_actual"),
                rs.getDate("fecha_contratacion").toLocalDate(),
                EstadoEmpleado.valueOf(rs.getString("estado"))
        );
    }
}