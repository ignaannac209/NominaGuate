package com.nominaguate.service;

import com.nominaguate.dto.PeriodoFiltroDTO;
import com.nominaguate.dto.PlanillaResumenDTO;
import com.nominaguate.dto.ResumenDashboardDTO;
import com.nominaguate.dto.TotalDepartamentoDTO;
import com.nominaguate.repository.DashboardRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio del Dashboard Gerencial. Por ahora es un paso directo
 * hacia el repository (las vistas de BD ya hacen el trabajo pesado de los
 * JOIN/agregación); aquí es donde se agregarían reglas de negocio futuras
 * (por ejemplo, filtrar por departamento visible según el rol del usuario).
 */
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public ResumenDashboardDTO obtenerResumenGeneral(Integer idPeriodo) throws SQLException {
        return dashboardRepository.obtenerResumenGeneral(idPeriodo);
    }

    public List<PlanillaResumenDTO> listarPlanillasGeneradas() throws SQLException {
        return dashboardRepository.listarPlanillasGeneradas();
    }

    public List<TotalDepartamentoDTO> obtenerTotalesPorDepartamento(Integer idPeriodo) throws SQLException {
        return dashboardRepository.obtenerTotalesPorDepartamento(idPeriodo);
    }

    public List<PeriodoFiltroDTO> listarPeriodosConDatos() throws SQLException {
        return dashboardRepository.listarPeriodosConDatos();
    }
}
