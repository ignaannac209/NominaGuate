package com.nominaguate.repository;

import com.nominaguate.config.DataBaseConnection;
import com.nominaguate.model.RolUsuario;
import com.nominaguate.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioRepository {

    // Usado por el servicio de autenticación para validar credenciales
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT id_usuario, nombre_usuario, clave_hash, nombre_completo, rol, activo, " +
                "id_empleado, fecha_creacion, ultimo_acceso " +
                "FROM usuarios WHERE nombre_usuario = ?";

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Registra la marca de tiempo del último inicio de sesión exitoso
    public void actualizarUltimoAcceso(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET ultimo_acceso = ? WHERE id_usuario = ?";

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    // Alta de usuario (pantalla de administración de usuarios, si se implementa)
    public Usuario crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_usuario, clave_hash, nombre_completo, rol, activo, id_empleado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DataBaseConnection.getConnectionDataBase();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getClaveHash()); // ya debe venir hasheada (ver PasswordUtil)
            ps.setString(3, usuario.getNombreCompleto());
            ps.setString(4, usuario.getRol().name());
            ps.setBoolean(5, usuario.isActivo());
            if (usuario.getIdEmpleado() != null) {
                ps.setInt(6, usuario.getIdEmpleado());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                usuario.setIdUsuario(keys.getInt(1));
            }
        }
        return usuario;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setClaveHash(rs.getString("clave_hash"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        usuario.setRol(RolUsuario.valueOf(rs.getString("rol")));
        usuario.setActivo(rs.getBoolean("activo"));

        int idEmpleado = rs.getInt("id_empleado");
        usuario.setIdEmpleado(rs.wasNull() ? null : idEmpleado);

        if (rs.getTimestamp("fecha_creacion") != null) {
            usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        if (rs.getTimestamp("ultimo_acceso") != null) {
            usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso").toLocalDateTime());
        }
        return usuario;
    }
}
