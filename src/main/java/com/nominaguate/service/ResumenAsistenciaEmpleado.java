package com.nominaguate.service;

import java.math.BigDecimal;

/**
 * Resumen de asistencia de un empleado para el periodo a calcular.
 * Se obtiene del módulo de Asistencia (vw_resumen_asistencia_mensual)
 * antes de invocar el cálculo de planilla.
 */
public record ResumenAsistenciaEmpleado(
        Integer idEmpleado,
        BigDecimal diasLaborados,
        BigDecimal horasExtra
) {
}
