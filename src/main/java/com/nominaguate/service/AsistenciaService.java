package com.nominaguate.service;

import com.nominaguate.dto.ResumenAsistenciaDTO;
import com.nominaguate.repository.AsistenciaRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AsistenciaService {

    private final AsistenciaRepository repository;

    public AsistenciaService() {
        this.repository = new AsistenciaRepository();
    }

    public boolean marcarEntrada(int empleadoId) throws Exception {
        if (empleadoId <= 0) {
            throw new IllegalArgumentException("El ID de empleado no es válido.");
        }
        return repository.registrarEntrada(empleadoId);
    }

    public boolean marcarSalida(int empleadoId) throws Exception {
        if (empleadoId <= 0) {
            throw new IllegalArgumentException("El ID de empleado no es válido.");
        }
        return repository.registrarSalida(empleadoId);
    }

    // Este método lo puede invocar el encargado del módulo de Nómina desde PlanillaService
    public List<ResumenAsistenciaDTO> consultarAsistenciaParaNomina(LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        return repository.obtenerResumenNomina(fechaInicio, fechaFin);
    }
}