/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author aalex
 */
public class Cliente {
    private int idcliente;
    private String nombres,dniRUC,telefono,correo;
    
    public Cliente(){}

    public Cliente(int idcliente, String nombres, String dniRUC, String correo) {
        this.idcliente = idcliente;
        this.nombres = nombres;
        this.dniRUC = dniRUC;
        this.correo = correo;
    }

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDniRUC() {
        return dniRUC;
    }

    public void setDniRUC(String dniRUC) {
        this.dniRUC = dniRUC;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String mostrarDatos(){
        return nombres + " - "+dniRUC;
    }
    
    
}
