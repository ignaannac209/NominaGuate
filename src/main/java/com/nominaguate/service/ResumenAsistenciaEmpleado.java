package com.nominaguate.service;

import java.math.BigDecimal;

/**
 * Resumen de asistencia de un empleado para el periodo a calcular.
 */
public record ResumenAsistenciaEmpleado(
        Integer idEmpleado,
        BigDecimal diasLaborados,
        BigDecimal horasExtra
) {
}
