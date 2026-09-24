/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nominaguate.model;
import com.nominaguate.model.Departamento;
import com.nominaguate.repository.DepartamentoPuestoRepository;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Alejandro Marroquin
 */
public class Departamento {
  private Integer idDepartamento;
    private String nombreDepartamento;
    private DepartamentoEstado estado;
    
    public Departamento() {
        this.estado = DepartamentoEstado.ACTIVO;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public DepartamentoEstado getEstado() {
        return estado;
    }

    public void setEstado(DepartamentoEstado estado) {
        this.estado = estado;
    }

    public Departamento( String nombreDepartamento, DepartamentoEstado estado) {
        this.nombreDepartamento = nombreDepartamento;
        this.estado = estado;
    }

    public Departamento(Integer idDepartamento, String nombreDepartamento, DepartamentoEstado estado) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.estado = estado;
    }
    
    
    
}
