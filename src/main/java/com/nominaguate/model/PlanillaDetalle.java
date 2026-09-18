package com.nominaguate.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Boleta de cálculo de un empleado dentro de una corrida de planilla.
 */
public class PlanillaDetalle {

    public enum EstadoDetalle { CALCULADO, PAGADO, ANULADO }

    private Integer idDetalle;
    private Integer idPlanilla;
    private Integer idEmpleado;
    private BigDecimal salarioBase;
    private BigDecimal diasLaborados;
    private BigDecimal horasExtra;
    private BigDecimal montoHorasExtra;
    private BigDecimal bonificacionIncentivo;
    private BigDecimal totalOtrosDevengados;
    private BigDecimal totalDevengado;
    private BigDecimal igssLaboral;
    private BigDecimal isr;
    private BigDecimal totalOtrosDescuentos;
    private BigDecimal totalDescuentos;
    private BigDecimal salarioNeto;
    private EstadoDetalle estado;

    // Desglose en memoria, se persiste aparte
    private final List<PlanillaDetalleConcepto> conceptos = new ArrayList<>();

    public PlanillaDetalle() {
        this.salarioBase = BigDecimal.ZERO;
        this.diasLaborados = BigDecimal.ZERO;
        this.horasExtra = BigDecimal.ZERO;
        this.montoHorasExtra = BigDecimal.ZERO;
        this.bonificacionIncentivo = BigDecimal.ZERO;
        this.totalOtrosDevengados = BigDecimal.ZERO;
        this.totalDevengado = BigDecimal.ZERO;
        this.igssLaboral = BigDecimal.ZERO;
        this.isr = BigDecimal.ZERO;
        this.totalOtrosDescuentos = BigDecimal.ZERO;
        this.totalDescuentos = BigDecimal.ZERO;
        this.salarioNeto = BigDecimal.ZERO;
        this.estado = EstadoDetalle.CALCULADO;
    }

    // Constructor de arranque del cálculo por empleado
    public PlanillaDetalle(Integer idEmpleado, BigDecimal salarioBase) {
        this();
        this.idEmpleado = idEmpleado;
        this.salarioBase = salarioBase;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdPlanilla() {
        return idPlanilla;
    }

    public void setIdPlanilla(Integer idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public BigDecimal getDiasLaborados() {
        return diasLaborados;
    }

    public void setDiasLaborados(BigDecimal diasLaborados) {
        this.diasLaborados = diasLaborados;
    }

    public BigDecimal getHorasExtra() {
        return horasExtra;
    }

    public void setHorasExtra(BigDecimal horasExtra) {
        this.horasExtra = horasExtra;
    }

    public BigDecimal getMontoHorasExtra() {
        return montoHorasExtra;
    }

    public void setMontoHorasExtra(BigDecimal montoHorasExtra) {
        this.montoHorasExtra = montoHorasExtra;
    }

    public BigDecimal getBonificacionIncentivo() {
        return bonificacionIncentivo;
    }

    public void setBonificacionIncentivo(BigDecimal bonificacionIncentivo) {
        this.bonificacionIncentivo = bonificacionIncentivo;
    }

    public BigDecimal getTotalOtrosDevengados() {
        return totalOtrosDevengados;
    }

    public void setTotalOtrosDevengados(BigDecimal totalOtrosDevengados) {
        this.totalOtrosDevengados = totalOtrosDevengados;
    }

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getIgssLaboral() {
        return igssLaboral;
    }

    public void setIgssLaboral(BigDecimal igssLaboral) {
        this.igssLaboral = igssLaboral;
    }

    public BigDecimal getIsr() {
        return isr;
    }

    public void setIsr(BigDecimal isr) {
        this.isr = isr;
    }

    public BigDecimal getTotalOtrosDescuentos() {
        return totalOtrosDescuentos;
    }

    public void setTotalOtrosDescuentos(BigDecimal totalOtrosDescuentos) {
        this.totalOtrosDescuentos = totalOtrosDescuentos;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getSalarioNeto() {
        return salarioNeto;
    }

    public void setSalarioNeto(BigDecimal salarioNeto) {
        this.salarioNeto = salarioNeto;
    }

    public EstadoDetalle getEstado() {
        return estado;
    }

    public void setEstado(EstadoDetalle estado) {
        this.estado = estado;
    }

    public List<PlanillaDetalleConcepto> getConceptos() {
        return conceptos;
    }

    // Agrega una línea al desglose auditable
    public void agregarConcepto(PlanillaDetalleConcepto concepto) {
        conceptos.add(concepto);
    }
}
