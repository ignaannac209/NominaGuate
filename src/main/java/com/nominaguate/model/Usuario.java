package com.nominaguate.model;

import java.time.LocalDateTime;

/**
 * Cuenta de acceso al sistema. Un Usuario puede o no estar ligado a un
 * Empleado (idEmpleado es null para cuentas ADMIN/RRHH que no son nómina).
 */
public class Usuario {

    private Integer idUsuario;
    private String nombreUsuario;
    private String claveHash;      // Hash (sal + digest) generado por PasswordUtil, nunca texto plano
    private String nombreCompleto;
    private RolUsuario rol;
    private boolean activo;
    private Integer idEmpleado;    // Nullable: referencia a Empleado cuando rol = EMPLEADO
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;

    public Usuario() {
        this.activo = true;
    }

    public Usuario(Integer idUsuario, String nombreUsuario, String claveHash, String nombreCompleto,
                   RolUsuario rol, boolean activo, Integer idEmpleado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.claveHash = claveHash;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.activo = activo;
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveHash() {
        return claveHash;
    }

    public void setClaveHash(String claveHash) {
        this.claveHash = claveHash;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    // Atajos usados por controladores para mostrar/ocultar opciones de menú
    public boolean esAdmin() {
        return rol == RolUsuario.ADMIN;
    }

    public boolean esRRHH() {
        return rol == RolUsuario.RRHH;
    }

    public boolean esEmpleado() {
        return rol == RolUsuario.EMPLEADO;
    }
}
