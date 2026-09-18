package com.nominaguate.dto;

import java.math.BigDecimal;

/**
 * Total de salario neto pagado por departamento, para el gráfico de barras
 * del Dashboard.
 */
public record TotalDepartamentoDTO(
        String nombreDepartamento,
        BigDecimal totalNeto
) {
}
