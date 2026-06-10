/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Inventario;
import Vista.Sistema;
import Modelo.MovimientoAlmacen;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import DAO.InventarioDAO;
import DAO.MovimientoInventarioDAO;
import DAO.ProductoDAO;
import Modelo.Producto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.ConnectionMySQL;

/**
 *
 * @author aalex
 */
public class InventarioController {
    private Sistema vista;
    private InventarioDAO inventarioDAO = new InventarioDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private MovimientoInventarioDAO movimientoDAO = new MovimientoInventarioDAO();
    private List<Producto> listaProductos;
    private Map<String, Producto> mapaProductosINV = new HashMap<>();

    public InventarioController(Sistema vista) {
        this.vista = vista;
    }
    
    public void cargarProductos() {
        
        listaProductos= productoDAO.listar();
        vista.cbox_producto.removeAllItems();
        mapaProductosINV.clear();

        for (Producto p : listaProductos) {
            vista.cbox_producto.addItem(p.getNombreProducto());
            mapaProductosINV.put(p.getNombreProducto(),p);
        }
    }
    
    public Producto getProductoSeleccionadoINV() {

        String nombre = vista.cbox_producto.getSelectedItem().toString();
        return mapaProductosINV.get(nombre);
    }
    
    public void cargarStockProducto(){
        Producto p= getProductoSeleccionadoINV();
        if(p == null)return;
        
        Inventario inv= inventarioDAO.obtenerPorProducto(p.getIdproducto());
        
        if(inv != null){
            vista.txtF_stock.setText(String.valueOf(inv.getStockActual()));
        }else{
           vista.txtF_stock.setText("0");
        }
        
    }

    /*public void procesarMovimiento() {

        try {

            // 1️⃣ Obtener datos desde vista
            int idProducto = obtenerIdProductoSeleccionado();
            String tipo = vista.cbox_movimiento.getSelectedItem().toString();
            int cantidad = Integer.parseInt(vista.txtF_cantidadInventario.getText());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad inválida");
                return;
            }

            Connection con = ConnectionMySQL.getConexion();
            con.setAutoCommit(false);

            try {

                // 2️⃣ Obtener stock actual
                int stockActual = inventarioDAO.obtenerStockActual(con, idProducto);
                int nuevoStock = stockActual;

                if (tipo.equalsIgnoreCase("Entrada")) {
                    nuevoStock += cantidad;

                } else if (tipo.equalsIgnoreCase("Salida")) {

                    if (stockActual < cantidad) {
                        JOptionPane.showMessageDialog(null, "Stock insuficiente");
                        con.rollback();
                        return;
                    }

                    nuevoStock -= cantidad;
                }

                // 3️⃣ Actualizar inventario
                inventarioDAO.actualizarStockConConexion(con, idProducto, nuevoStock);

                // 4️⃣ Registrar movimiento
                movimientoDAO.insertarConConexion(con, idProducto, tipo, cantidad);

                con.commit();

                // 5️⃣ Actualizar vista
                vista.txtF_stock.setText(String.valueOf(nuevoStock));
                JOptionPane.showMessageDialog(null, "Movimiento realizado correctamente");

            } catch (Exception e) {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Error al procesar movimiento");
            } finally {
                con.close();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado");
        }
    }*/
    public void procesarMovimiento() {

        Connection con = null;

        try {

            Producto p = getProductoSeleccionadoINV();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto");
                return;
            }

            int cantidad = Integer.parseInt(vista.txtF_cantidadInventario.getText());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad inválida");
                return;
            }

            String tipo = vista.cbox_movimiento.getSelectedItem().toString().toUpperCase();

            con = ConnectionMySQL.getConexion();
            con.setAutoCommit(false);

            // Obtener inventario del producto
            Inventario inv = inventarioDAO.obtenerPorProductoConConexion(con, p.getIdproducto());

            if (inv == null) {
                JOptionPane.showMessageDialog(null, "No existe inventario para este producto");
                con.rollback();
                return;
            }

            int stockActual = inv.getStockActual();
            int nuevoStock;

            // Calcular nuevo stock
            if (tipo.equals("ENTRADA")) {
                nuevoStock = stockActual + cantidad;
            } else { // SALIDA
                if (stockActual < cantidad) {
                    JOptionPane.showMessageDialog(null, "Stock insuficiente");
                    con.rollback();
                    return;
                }
                nuevoStock = stockActual - cantidad;
            }

            // Actualizar stock usando id_inventario
            inventarioDAO.actualizarStockConConexion(con, inv.getIdinventario(), nuevoStock);

            // Insertar movimiento usando id_inventario
            movimientoDAO.insertarConConexion(con, inv.getIdinventario(), tipo, cantidad);

            con.commit();

            // Actualizar vista
            vista.txtF_stock.setText(String.valueOf(nuevoStock));
            vista.txtF_cantidadInventario.setText("");

            listarMovimientos();

            JOptionPane.showMessageDialog(null, "Movimiento realizado correctamente");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
                    e.printStackTrace(); // 🔥 MUY IMPORTANTE
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            } catch (Exception ex) {}
            JOptionPane.showMessageDialog(null, "Error en transacción");
        } finally {
            try {
                if (con != null) con.close();
            } catch (Exception ex) {}
        }
    }
    
        
    public void listarMovimientos() {

        DefaultTableModel modelo = (DefaultTableModel) vista.t_inventario.getModel();
        modelo.setRowCount(0);

        String sql = """
            SELECT m.id_movimiento, m.fecha, p.nombre, 
                               m.tipo_movimiento, m.cantidad, i.stock_actual
                        FROM movimiento_inventario m
                        inner JOIN inventario i ON m.id_inventario = i.id_inventario
                        inner JOin producto p ON i.id_producto = p.id_producto
        """;

        try (Connection con = ConnectionMySQL.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6)
                });
            }

        } catch (Exception e) {
            System.out.println("Error listar movimientos: " + e.getMessage());
        }
    }

    private int obtenerIdProductoSeleccionado() {
        Producto p = (Producto) vista.cbox_producto1.getSelectedItem();
        return p.getIdproducto();
    }
}

