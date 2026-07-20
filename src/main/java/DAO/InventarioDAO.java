/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import java.sql.*;
import Modelo.Inventario;
import Modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import util.*;


/**
 *
 * @author aalex
 */
public class InventarioDAO {
    
    public int obtenerStockActual(Connection con, int idProducto) throws SQLException {

        String sql = "SELECT stock_actual FROM inventario WHERE id_producto=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("stock_actual");
        }

        return 0;
    }

    public void actualizarStockConConexion(Connection con, int idInventario, int nuevoStock) throws SQLException {

        String sql = "UPDATE inventario SET stock_actual=? WHERE id_inventario=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, nuevoStock);
        ps.setInt(2, idInventario);

        ps.executeUpdate();
    }
    public Inventario obtenerPorProducto(int idProducto) {

        String sql = "SELECT * FROM inventario WHERE id_producto=?";

        try (Connection con = ConnectionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Inventario inv = new Inventario();
                Producto p = new Producto();

                p.setIdproducto(rs.getInt("id_producto"));
                inv.setProducto(p);
                inv.setStockActual(rs.getInt("stock_actual"));
                

                return inv;
            }

        } catch (SQLException e) {
            System.out.println("Error obtener inventario: " + e.getMessage());
        }

        return null;
    }
    
    public Inventario obtenerPorProductoConConexion(Connection con,int idProducto) throws SQLException{
    
        String sql = "SELECT * FROM inventario WHERE id_producto = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Inventario inv = new Inventario();
            inv.setIdinventario(rs.getInt("id_inventario"));
            inv.setStockActual(rs.getInt("stock_actual"));
            return inv;
    }

    return null;
    };
    
    public List<Inventario> listar() {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario";

        try (Connection con = ConnectionMySQL.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Inventario i = new Inventario();
                Producto p =new Producto();
                p.setIdproducto(rs.getInt("id_producto"));
                i.setProducto(p);
                i.setStockActual(rs.getInt("stock_actual"));
                lista.add(i);
            }

        } catch (SQLException e) {
            System.out.println("Error listar inventario: " + e.getMessage());
        }

        return lista;
    }
    
    public void actualizarStock(int idProducto, int nuevoStock) {

        String sql = "UPDATE inventario SET stock_actual=? WHERE id_producto=?";

        try (Connection con = ConnectionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error actualizar stock: " + e.getMessage());
        }
    }

    public int obtenerIdInventario(Connection con, int idProducto) throws SQLException {

        String sql = "SELECT id_inventario FROM inventario WHERE id_producto = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("id_inventario");
        }

        return -1;
    }    
    
    public void actualizarStockRV(Connection con, int idProducto, int nuevaCantidad) throws SQLException {

        String sql = "UPDATE inventario SET stock_actual = ? WHERE id_producto = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        }
    }
    public void crearInventario(Connection con, int idProducto, int stock) throws SQLException {

    String sql = "INSERT INTO inventario(id_producto, stock_actual) VALUES(?,?)";

    PreparedStatement ps = con.prepareStatement(sql);

    ps.setInt(1, idProducto);
    ps.setInt(2, stock);

    ps.executeUpdate();
}
    
    
}
