package com.nominaguate.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexiones JDBC hacia db_rrhh_nomina
 */
public final class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/db_rrhh_nomina?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "$DmynM4A";

    private ConexionBD() {
    }

    // Nueva conexión por operación, cerrada por el llamador (try-with-resources)
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
