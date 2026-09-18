package com.nominaguate.dto;

import java.math.BigDecimal;

/**
 * KPIs consolidados para las tarjetas superiores del Dashboard Gerencial.
 * Se calcula agregando vw_boletas_planilla (ver DashboardRepository).
 */
public record ResumenDashboardDTO(
        long totalPlanillasGeneradas,
        long totalBoletasEmitidas,
        BigDecimal totalDevengado,
        BigDecimal totalDescuentos,
        BigDecimal totalNeto,
        BigDecimal promedioSalarioNeto
) {
}
