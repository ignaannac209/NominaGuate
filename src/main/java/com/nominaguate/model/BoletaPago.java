package com.nominaguate.model;

import java.time.LocalDateTime;

/**
 * Comprobante final de pago entregado al empleado.
 */
public class BoletaPago {

    public enum EstadoBoleta { EMITIDA, ANULADA }

    private Integer idBoleta;
    private Integer idDetalle;
    private String codigoBoleta;
    private LocalDateTime fechaEmision;
    private EstadoBoleta estado;
    private String observaciones;

    public BoletaPago() {
    }

    // Constructor de emisión
    public BoletaPago(Integer idDetalle, String codigoBoleta) {
        this.idDetalle = idDetalle;
        this.codigoBoleta = codigoBoleta;
        this.estado = EstadoBoleta.EMITIDA;
    }

    public Integer getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(Integer idBoleta) {
        this.idBoleta = idBoleta;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getCodigoBoleta() {
        return codigoBoleta;
    }

    public void setCodigoBoleta(String codigoBoleta) {
        this.codigoBoleta = codigoBoleta;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public EstadoBoleta getEstado() {
        return estado;
    }

    public void setEstado(EstadoBoleta estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
