package com.nominaguate.model;

import java.math.BigDecimal;

/**
 * Catálogo de devengados y descuentos (IGSS, ISR, Bono Incentivo, etc).
 */
public class ConceptoNomina {

    public enum TipoConcepto { DEVENGADO, DESCUENTO }
    public enum NaturalezaConcepto { FIJO, VARIABLE, PORCENTUAL }
    public enum EstadoConcepto { ACTIVO, INACTIVO }

    private Integer idConcepto;
    private String codigoConcepto;
    private String nombreConcepto;
    private TipoConcepto tipoConcepto;
    private NaturalezaConcepto naturaleza;
    private BigDecimal porcentaje;
    private EstadoConcepto estado;

    public ConceptoNomina() {
    }

    // Constructor para dar de alta un concepto nuevo
    public ConceptoNomina(String codigoConcepto, String nombreConcepto, TipoConcepto tipoConcepto,
                           NaturalezaConcepto naturaleza, BigDecimal porcentaje) {
        this.codigoConcepto = codigoConcepto;
        this.nombreConcepto = nombreConcepto;
        this.tipoConcepto = tipoConcepto;
        this.naturaleza = naturaleza;
        this.porcentaje = porcentaje;
        this.estado = EstadoConcepto.ACTIVO;
    }

    public Integer getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(Integer idConcepto) {
        this.idConcepto = idConcepto;
    }

    public String getCodigoConcepto() {
        return codigoConcepto;
    }

    public void setCodigoConcepto(String codigoConcepto) {
        this.codigoConcepto = codigoConcepto;
    }

    public String getNombreConcepto() {
        return nombreConcepto;
    }

    public void setNombreConcepto(String nombreConcepto) {
        this.nombreConcepto = nombreConcepto;
    }

    public TipoConcepto getTipoConcepto() {
        return tipoConcepto;
    }

    public void setTipoConcepto(TipoConcepto tipoConcepto) {
        this.tipoConcepto = tipoConcepto;
    }

    public NaturalezaConcepto getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(NaturalezaConcepto naturaleza) {
        this.naturaleza = naturaleza;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public EstadoConcepto getEstado() {
        return estado;
    }

    public void setEstado(EstadoConcepto estado) {
        this.estado = estado;
    }
}
