/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nominaguate.controller;
import com.nominaguate.service.DepartamentoPuestoService;
import com.nominaguate.model.Departamento;
import com.nominaguate.model.DepartamentoEstado;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author allan
 */
public class DepartamentoPuestoController  {

    @FXML ComboBox<DepartamentoEstado> cmbEstado;
    @FXML TextField txtNombreDepto;
    @FXML TextField txtIdDepto;
    @FXML TableView<Departamento> tblDepartamentos;
    @FXML TableColumn<Departamento, Integer> colIdDepto;
    @FXML TableColumn<Departamento, String> colNombreDepto;    
    @FXML TableColumn<Departamento, String> colEstado;

    
    private final DepartamentoPuestoService departamentoService;
   

    public DepartamentoPuestoController() throws SQLException {
        this.departamentoService = new DepartamentoPuestoService();
    }
    
    /**
     * Initializes the controller class.
     * @throws java.sql.SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        // TODO
        txtNombreDepto.setText("");
        txtIdDepto.setDisable(true);
        
        cmbEstado.setItems(FXCollections.observableArrayList(DepartamentoEstado.values()));
        
        cmbEstado.getSelectionModel().selectFirst();
        
        colIdDepto.setCellValueFactory(
            cellData -> new SimpleIntegerProperty(
                cellData.getValue().getIdDepartamento()
            ).asObject()           
        );
        
        colNombreDepto.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getNombreDepartamento()
                )
        );
        
        colEstado.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getEstado().toString()
                )
        );
        
        loadDepartamentos();
    }
    
    private void loadDepartamentos() throws SQLException{
        try {
            List<Departamento> departamentos = departamentoService.listarDepartamentos();
            
            tblDepartamentos.setItems(
                    FXCollections.observableArrayList(departamentos)
            );
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onAgregar(ActionEvent event){
        String nombreDepto = txtNombreDepto.getText();
        DepartamentoEstado estado = cmbEstado.getValue();
        
        if(nombreDepto == null || nombreDepto.isBlank()){
            return;
        }
        
        try{
            Departamento depto = new Departamento(nombreDepto, estado);
            Boolean result = this.departamentoService.crearDepartamento(depto);
            if (result == false){
                return;
            }
            mostrarInformacion();
            loadDepartamentos();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void mostrarInformacion(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText(null);
        alert.setContentText("Departametno creado con exito");
        alert.showAndWait();
    }
    
     @FXML
    public void onModificar(ActionEvent event){
        System.out.print("Testing");
    }
    
        @FXML
    public void onEliminar(ActionEvent event){
        System.out.print("Testing");
    }
    
}
