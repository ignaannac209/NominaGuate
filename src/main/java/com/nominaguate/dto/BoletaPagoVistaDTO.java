package com.nominaguate.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Proyección de solo lectura de la vista vw_boletas_planilla.
 * Se usa para mostrar la boleta consolidada en pantalla, sin volver a
 * consultar empleados, puestos ni departamentos por separado.
 */
public record BoletaPagoVistaDTO(
        Integer idBoleta,
        String codigoBoleta,
        LocalDateTime fechaEmision,
        String estadoBoleta,
        Integer idPeriodo,
        String nombrePeriodo,
        Integer idPlanilla,
        String estadoPlanilla,
        Integer idEmpleado,
        String empleado,
        String nombreDepartamento,
        String nombrePuesto,
        BigDecimal salarioBase,
        BigDecimal diasLaborados,
        BigDecimal horasExtra,
        BigDecimal montoHorasExtra,
        BigDecimal bonificacionIncentivo,
        BigDecimal totalDevengado,
        BigDecimal igssLaboral,
        BigDecimal isr,
        BigDecimal totalDescuentos,
        BigDecimal salarioNeto,
        String desgloseConceptos
) {
}
