package com.nominaguate.dto;

public class ResumenAsistenciaDTO {
    private int empleadoId;
    private double totalHorasTrabajadas;
    private int totalAusencias;

    public ResumenAsistenciaDTO() {
    }

    public ResumenAsistenciaDTO(int empleadoId, double totalHorasTrabajadas, int totalAusencias) {
        this.empleadoId = empleadoId;
        this.totalHorasTrabajadas = totalHorasTrabajadas;
        this.totalAusencias = totalAusencias;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public double getTotalHorasTrabajadas() {
        return totalHorasTrabajadas;
    }

    public void setTotalHorasTrabajadas(double totalHorasTrabajadas) {
        this.totalHorasTrabajadas = totalHorasTrabajadas;
    }

    public int getTotalAusencias() {
        return totalAusencias;
    }

    public void setTotalAusencias(int totalAusencias) {
        this.totalAusencias = totalAusencias;
    }
}