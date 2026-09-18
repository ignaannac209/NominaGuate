package com.nominaguate.service;

import com.nominaguate.model.Empleado;
import com.nominaguate.repository.EmpleadoRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public List<Empleado> listarTodos() throws SQLException {   
        return empleadoRepository.listarTodos();
    }

    public List<Empleado> listarActivos() throws SQLException {
        return empleadoRepository.listarActivos();
    }

    public Empleado registrar(Empleado empleado) throws SQLException {
        validar(empleado);
        return empleadoRepository.insertar(empleado);
    }

    public void editar(Empleado empleado) throws SQLException {
        if (empleado.getIdEmpleado() == null) {
            throw new IllegalArgumentException("El empleado a editar no tiene un id válido");
        }
        validar(empleado);
        empleadoRepository.actualizar(empleado);
    }

    public void eliminar(int idEmpleado) throws SQLException {
        empleadoRepository.eliminar(idEmpleado);
    }

    private void validar(Empleado empleado) {
        if (empleado.getNombreCompleto() == null || empleado.getNombreCompleto().isBlank()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio");
        }
        if (empleado.getPuesto() == null || empleado.getPuesto().isBlank()) {
            throw new IllegalArgumentException("El puesto es obligatorio");
        }
        if (empleado.getSalarioActual() == null || empleado.getSalarioActual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El salario base debe ser mayor a 0");
        }
        if (empleado.getFechaContratacion() == null) {
            throw new IllegalArgumentException("La fecha de contratación es obligatoria");
        }
    }
}