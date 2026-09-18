package com.nominaguate.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Empleado {

    public enum EstadoEmpleado {
        ACTIVO, INACTIVO, SUSPENDIDO
    }

    private Integer idEmpleado;
    private String nombreCompleto;
    private String puesto;
    private BigDecimal salarioActual;
    private LocalDate fechaContratacion;
    private EstadoEmpleado estado;

    // Constructor vacío
    public Empleado() {
    }

    // Constructor original (3 parámetros)
    public Empleado(Integer idEmpleado, String nombreCompleto, BigDecimal salarioActual) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.salarioActual = salarioActual;
    }

    // Constructor para cuando creas un empleado nuevo
    public Empleado(String nombreCompleto, String puesto, BigDecimal salarioActual, LocalDate fechaContratacion) {
        this.nombreCompleto = nombreCompleto;
        this.puesto = puesto;
        this.salarioActual = salarioActual;
        this.fechaContratacion = fechaContratacion;
    }

    // Constructor completo para el mapeo desde la base de datos (6 parámetros)
    public Empleado(Integer idEmpleado, String nombreCompleto, String puesto, 
                    BigDecimal salarioActual, LocalDate fechaContratacion, EstadoEmpleado estado) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.puesto = puesto;
        this.salarioActual = salarioActual;
        this.fechaContratacion = fechaContratacion;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getSalarioActual() {
        return salarioActual;
    }

    public void setSalarioActual(BigDecimal salarioActual) {
        this.salarioActual = salarioActual;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public EstadoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpleado estado) {
        this.estado = estado;
    }
}