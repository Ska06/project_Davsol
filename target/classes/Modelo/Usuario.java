/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author aalex
 */
public class Usuario {
    private int idusuario;
    private String nombreUsuario,password,rol;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String password, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }
    
    
    
    public boolean Autenticar(String user, String pass){
        return this.nombreUsuario.equals(user) && this.password.equals(pass);
    }
    
    
    
    
    
    
}
