package com.nominaguate.model;

import java.time.LocalDate;

/**
 * Periodo de pago (quincenal o mensual).
 */
public class PeriodoPlanilla {

    public enum TipoPeriodo { QUINCENAL, MENSUAL }
    public enum EstadoPeriodo { ABIERTO, PROCESADO, CERRADO }

    private Integer idPeriodo;
    private String nombrePeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private TipoPeriodo tipoPeriodo;
    private EstadoPeriodo estado;

    public PeriodoPlanilla() {
    }

    // Constructor para crear un periodo nuevo
    public PeriodoPlanilla(String nombrePeriodo, LocalDate fechaInicio, LocalDate fechaFin,
                            TipoPeriodo tipoPeriodo) {
        this.nombrePeriodo = nombrePeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoPeriodo = tipoPeriodo;
        this.estado = EstadoPeriodo.ABIERTO;
    }

    // Constructor completo, usado al leer de BD
    public PeriodoPlanilla(Integer idPeriodo, String nombrePeriodo, LocalDate fechaInicio,
                            LocalDate fechaFin, TipoPeriodo tipoPeriodo, EstadoPeriodo estado) {
        this.idPeriodo = idPeriodo;
        this.nombrePeriodo = nombrePeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoPeriodo = tipoPeriodo;
        this.estado = estado;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public TipoPeriodo getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public EstadoPeriodo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPeriodo estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombrePeriodo; // para ComboBox
    }
}
