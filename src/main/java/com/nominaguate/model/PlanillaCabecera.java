package com.nominaguate.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrida de planilla para un periodo determinado.
 */
public class PlanillaCabecera {

    public enum EstadoPlanilla { BORRADOR, PROCESADA, ANULADA }

    private Integer idPlanilla;
    private Integer idPeriodo;
    private Integer idUsuarioGenero;
    private LocalDateTime fechaGeneracion;
    private BigDecimal totalDevengado;
    private BigDecimal totalDescuentos;
    private BigDecimal totalNeto;
    private EstadoPlanilla estado;

    // Detalles en memoria mientras se arma el cálculo
    private final List<PlanillaDetalle> detalles = new ArrayList<>();

    public PlanillaCabecera() {
        this.totalDevengado = BigDecimal.ZERO;
        this.totalDescuentos = BigDecimal.ZERO;
        this.totalNeto = BigDecimal.ZERO;
        this.estado = EstadoPlanilla.BORRADOR;
    }

    // Constructor para iniciar una corrida nueva
    public PlanillaCabecera(Integer idPeriodo, Integer idUsuarioGenero) {
        this();
        this.idPeriodo = idPeriodo;
        this.idUsuarioGenero = idUsuarioGenero;
    }

    public Integer getIdPlanilla() {
        return idPlanilla;
    }

    public void setIdPlanilla(Integer idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getIdUsuarioGenero() {
        return idUsuarioGenero;
    }

    public void setIdUsuarioGenero(Integer idUsuarioGenero) {
        this.idUsuarioGenero = idUsuarioGenero;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public EstadoPlanilla getEstado() {
        return estado;
    }

    public void setEstado(EstadoPlanilla estado) {
        this.estado = estado;
    }

    public List<PlanillaDetalle> getDetalles() {
        return detalles;
    }

    // Arma la cabecera antes de persistir
    public void agregarDetalle(PlanillaDetalle detalle) {
        detalles.add(detalle);
    }
}
