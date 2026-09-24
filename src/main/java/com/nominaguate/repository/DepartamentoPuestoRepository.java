/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nominaguate.repository;

import com.nominaguate.config.DataBaseConnection;
import com.nominaguate.model.Departamento;
import com.nominaguate.model.DepartamentoEstado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro
 */
public class DepartamentoPuestoRepository {
    
    private final Connection con;

    public DepartamentoPuestoRepository() throws SQLException {
        this.con = DataBaseConnection.getConnectionDataBase();
    }

    public List<Departamento> listaDepartamentos() throws SQLException {
        List<Departamento> departamentos = new ArrayList<>();

        String sql = "SELECT id_departamento, nombre_departamento, estado FROM departamentos";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {

                Departamento departamento = new Departamento(resultSet.getInt("id_departamento"), 
                        resultSet.getString("nombre_departamento"), 
                        DepartamentoEstado.valueOf(resultSet.getString("estado")));

                departamentos.add(departamento);
            }
        }

        return departamentos;
    }
    
    public boolean crearDepto(Departamento depto) throws SQLException{
        String sql = "insert into departamentos (nombre_departamento,estado) values(?,?)";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setString(1, depto.getNombreDepartamento());
            statement.setString(2, depto.getEstado().toString());
            
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
