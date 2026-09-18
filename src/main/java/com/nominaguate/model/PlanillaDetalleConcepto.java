package com.nominaguate.model;

import java.math.BigDecimal;

/**
 * Línea de desglose.
 */
public class PlanillaDetalleConcepto {

    private Integer idDetalleConcepto;
    private Integer idDetalle;
    private Integer idConcepto;
    private String codigoConcepto; // evita recargar el catálogo al mostrar
    private BigDecimal monto;

    public PlanillaDetalleConcepto() {
    }

    // Constructor de uso frecuente al armar el cálculo
    public PlanillaDetalleConcepto(Integer idConcepto, String codigoConcepto, BigDecimal monto) {
        this.idConcepto = idConcepto;
        this.codigoConcepto = codigoConcepto;
        this.monto = monto;
    }

    public Integer getIdDetalleConcepto() {
        return idDetalleConcepto;
    }

    public void setIdDetalleConcepto(Integer idDetalleConcepto) {
        this.idDetalleConcepto = idDetalleConcepto;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
