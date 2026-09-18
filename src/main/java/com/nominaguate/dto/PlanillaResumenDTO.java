package com.nominaguate.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fila de la tabla "Planillas generadas" del Dashboard: una corrida de
 * planilla ya consolidada (totales y cantidad de empleados incluidos).
 */
public record PlanillaResumenDTO(
        Integer idPlanilla,
        String nombrePeriodo,
        LocalDateTime fechaGeneracion,
        String estadoPlanilla,
        long cantidadEmpleados,
        BigDecimal totalDevengado,
        BigDecimal totalDescuentos,
        BigDecimal totalNeto
) {
}
