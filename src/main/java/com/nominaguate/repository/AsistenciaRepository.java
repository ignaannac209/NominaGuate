package com.nominaguate.repository;

import com.nominaguate.config.ConexionBD;
import com.nominaguate.dto.ResumenAsistenciaDTO;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaRepository {

    public boolean registrarEntrada(int empleadoId) throws SQLException {
        String sql = "INSERT INTO asistencia (empleado_id, fecha, hora_entrada, estado) VALUES (?, ?, ?, 'PRESENTE')";
        
        // Uso directo de ConexionBD.obtenerConexion()
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, empleadoId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setTime(3, Time.valueOf(LocalTime.now()));
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean registrarSalida(int empleadoId) throws SQLException {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        String query = "SELECT hora_entrada FROM asistencia WHERE empleado_id = ? AND fecha = ?";
        LocalTime horaEntrada = null;

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setInt(1, empleadoId);
            stmt.setDate(2, Date.valueOf(hoy));
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getTime("hora_entrada") != null) {
                horaEntrada = rs.getTime("hora_entrada").toLocalTime();
            }
        }

        if (horaEntrada == null) {
            return false; // No hay entrada registrada para el día de hoy
        }

        long minutos = Duration.between(horaEntrada, ahora).toMinutes();
        double horas = minutos / 60.0;

        String update = "UPDATE asistencia SET hora_salida = ?, horas_trabajadas = ? WHERE empleado_id = ? AND fecha = ?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = cn.prepareStatement(update)) {
            stmt.setTime(1, Time.valueOf(ahora));
            stmt.setDouble(2, horas);
            stmt.setInt(3, empleadoId);
            stmt.setDate(4, Date.valueOf(hoy));
            return stmt.executeUpdate() > 0;
        }
    }

    public List<ResumenAsistenciaDTO> obtenerResumenNomina(LocalDate inicio, LocalDate fin) throws SQLException {
        List<ResumenAsistenciaDTO> resumenList = new ArrayList<>();
        String sql = "SELECT empleado_id, " +
                     "SUM(horas_trabajadas) AS total_horas, " +
                     "SUM(CASE WHEN estado = 'AUSENTE' THEN 1 ELSE 0 END) AS total_ausencias " +
                     "FROM asistencia WHERE fecha BETWEEN ? AND ? GROUP BY empleado_id";

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fin));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resumenList.add(new ResumenAsistenciaDTO(
                    rs.getInt("empleado_id"),
                    rs.getDouble("total_horas"),
                    rs.getInt("total_ausencias")
                ));
            }
        }
        return resumenList;
    }
}