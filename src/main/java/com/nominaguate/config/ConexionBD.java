package com.nominaguate.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexiones JDBC hacia db_rrhh_nomina.
 * En producción, mover URL/usuario/clave a un archivo de configuración.
 */
public final class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/db_rrhh_nomina?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    private ConexionBD() {
    }

    // Nueva conexión por operación, cerrada por el llamador (try-with-resources)
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
