/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
/**
 *
 * @author aalex
 */
public class ConnectionMySQL {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/sistemaAbsol";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args) {

        Connection con = ConnectionMySQL.getConexion();

        if (con != null) {
            System.out.println("Conexión exitosa ");
        } else {
            System.out.println("Error ");
        }
    }
}



