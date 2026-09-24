/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nominaguate.service;

import com.nominaguate.model.Departamento;
import com.nominaguate.repository.DepartamentoPuestoRepository;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author allan
 */
public class DepartamentoPuestoService {
    private final DepartamentoPuestoRepository departamentoPuestoRepo;

    public DepartamentoPuestoService() throws SQLException {
        this.departamentoPuestoRepo = new DepartamentoPuestoRepository();
    }
    
    public List<Departamento> listarDepartamentos() throws SQLException{
        return this.departamentoPuestoRepo.listaDepartamentos();
    }

    public Boolean crearDepartamento(Departamento depto) throws SQLException {
        //validaciones
        return this.departamentoPuestoRepo.crearDepto(depto);
    }
}
