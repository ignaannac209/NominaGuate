package com.nominaguate.service;

import com.nominaguate.model.Usuario;
import com.nominaguate.repository.UsuarioRepository;
import com.nominaguate.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Reglas de autenticación: valida credenciales contra la BD y determina
 * si la cuenta puede iniciar sesión.
 */
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Intenta autenticar con usuario y contraseña en texto plano.
     * Devuelve Optional.empty() si el usuario no existe, la contraseña es
     * incorrecta o la cuenta está inactiva (mismo mensaje genérico para las
     * tres, por seguridad: no revelar cuál fue la causa exacta).
     */
    public Optional<Usuario> autenticar(String nombreUsuario, String claveTexto) throws SQLException {
        if (nombreUsuario == null || nombreUsuario.isBlank() || claveTexto == null || claveTexto.isBlank()) {
            return Optional.empty();
        }

        Optional<Usuario> usuarioEncontrado = usuarioRepository.buscarPorNombreUsuario(nombreUsuario.trim());
        if (usuarioEncontrado.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioEncontrado.get();
        if (!usuario.isActivo()) {
            return Optional.empty();
        }
        if (!PasswordUtil.verificar(claveTexto, usuario.getClaveHash())) {
            return Optional.empty();
        }

        usuarioRepository.actualizarUltimoAcceso(usuario.getIdUsuario());
        return Optional.of(usuario);
    }
}
