package com.nominaguate.controller;

/**
Por ahora, usa el usuario con ID 1 para
poder probar este módulo por separado.
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
