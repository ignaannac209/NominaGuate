package com.nominaguate.controller;

/**
 * STUB TEMPORAL. Reemplazar por el mecanismo real de sesión del
 * módulo de Login. De momento devuelve un id de usuario fijo (1)
 * para poder compilar y probar este módulo de forma aislada.
 */
public class SesionActual {

    private static Integer idUsuario = 1;

    private SesionActual() {
    }

    public static Integer getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(Integer id) {
        idUsuario = id;
    }
}
