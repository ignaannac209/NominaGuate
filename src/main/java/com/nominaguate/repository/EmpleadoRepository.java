package com.nominaguate.repository;

import com.nominaguate.config.DataBaseConnection;
import com.nominaguate.model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos del módulo de Empleados.
 */
public class EmpleadoRepository {

    // Lista los empleados activos, listos para calcular la planilla
    public List<Empleado> listarActivos() throws SQLException {
        String sql = "SELECT id_empleado, nombre_completo, salario_actual " +
                     "FROM empleados WHERE estado = 'ACTIVO' ORDER BY nombre_completo";

        List<Empleado> empleados = new ArrayList<>();

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre_completo"),
                        rs.getBigDecimal("salario_actual")
                );
                empleados.add(empleado);
            }
        }
        return empleados;
    }
}