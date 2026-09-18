package com.nominaguate.controller;

<<<<<<< HEAD
=======
<<<<<<< HEAD
/**
Por ahora, usa el usuario con ID 1 para
poder probar este módulo por separado.
 */
public class SesionActual {

    private static Integer idUsuario = 1;
=======
>>>>>>> origin/main
import com.nominaguate.model.RolUsuario;
import com.nominaguate.model.Usuario;

/**
 * Contexto de sesión global de la aplicación de escritorio (un solo usuario
 * activo a la vez, típico en apps JavaFX de un solo puesto).
 *
 * Reemplaza el stub anterior que solo exponía un idUsuario fijo = 1.
 * El resto del código que ya usaba SesionActual.getIdUsuario() sigue
 * funcionando igual: ahora ese valor viene del Usuario autenticado en el login.
 */
public final class SesionActual {

    private static Usuario usuarioActual;
<<<<<<< HEAD
=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main

    private SesionActual() {
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD
    public static Integer getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(Integer id) {
        idUsuario = id;
=======
>>>>>>> origin/main
    // Llamado por LoginController justo después de autenticar con éxito
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

    // Se mantiene por compatibilidad con código existente (PlanillaController, etc.)
    public static Integer getIdUsuario() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : null;
    }

    public static String getNombreCompleto() {
        return usuarioActual != null ? usuarioActual.getNombreCompleto() : null;
    }

    public static RolUsuario getRol() {
        return usuarioActual != null ? usuarioActual.getRol() : null;
    }

    // Atajo para que los controladores decidan qué mostrar/habilitar según el rol
    public static boolean tieneRol(RolUsuario rol) {
        return usuarioActual != null && usuarioActual.getRol() == rol;
<<<<<<< HEAD
=======
>>>>>>> feature/implementacion-login
>>>>>>> origin/main
    }
}
