package com.nominaguate.model;

import java.math.BigDecimal;

/**
 * STUB TEMPORAL. Reemplazar por la clase Empleado real del módulo
 * de Gestión de Empleados. El Servicio y el Controlador de Planilla
 * solo necesitan getIdEmpleado() y getSalarioActual().
 */
public class Empleado {

    private Integer idEmpleado;
    private String nombreCompleto;
    private BigDecimal salarioActual;

    public Empleado() {
    }

    public Empleado(Integer idEmpleado, String nombreCompleto, BigDecimal salarioActual) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.salarioActual = salarioActual;
    }

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

    public BigDecimal getSalarioActual() {
        return salarioActual;
    }

    public void setSalarioActual(BigDecimal salarioActual) {
        this.salarioActual = salarioActual;
    }
}
