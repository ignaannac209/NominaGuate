package com.nominaguate.controller;

import com.nominaguate.model.RolUsuario;
import com.nominaguate.model.Usuario;

/**
 * Gestión centralizada de la sesión del usuario activo en la aplicación.
 * Permite controlar roles, permisos y consultar la identidad del usuario logueado.
 */
public final class SesionActual {

    private static Usuario usuarioActual;
    private static Integer idUsuarioDefault = 1; // ID por defecto para pruebas aisladas de controladores

    private SesionActual() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Llamado por LoginController justo después de autenticar con éxito.
     */
    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Devuelve el ID del usuario activo. Si no hay sesión iniciada,
     * retorna el ID 1 por defecto para permitir pruebas.
     */
    public static Integer getIdUsuario() {
        if (usuarioActual != null) {
            return usuarioActual.getIdUsuario();
        }
        return idUsuarioDefault;
    }

    public static void setIdUsuarioDefault(Integer id) {
        idUsuarioDefault = id;
    }

    public static String getNombreCompleto() {
        return usuarioActual != null ? usuarioActual.getNombreCompleto() : null;
    }

    public static RolUsuario getRol() {
        return usuarioActual != null ? usuarioActual.getRol() : null;
    }

    /**
     * Atajo para que los controladores decidan qué mostrar/habilitar según el rol.
     */
    public static boolean tieneRol(RolUsuario rol) {
        return usuarioActual != null && usuarioActual.getRol() == rol;
    }
}