/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.*;
import Modelo.Cliente;
import Modelo.DetalleVenta;
import Modelo.Producto;
import Modelo.Venta;
import Vista.Sistema;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import util.ConnectionMySQL;

/**
 *
 * @author aalex
 */
public class VentaController {
    private Venta venta;
    private Sistema vista;
    private Cliente cl;
    
    
    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
    private InventarioDAO inventarioDAO = new InventarioDAO();
    private MovimientoInventarioDAO movimientoDAO = new MovimientoInventarioDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoController productoController;
    private ProductoDAO productoDAO;
    private int stockOriginal = 0;
    private double Totalpagar =0.00;
    private List<Producto> listaProductos;
    private Map<String, Producto> mapaProductos = new HashMap<>();
    private int item;

    public VentaController(Sistema vista, ProductoController productoController) {
        this.vista = vista;
        this.productoController = productoController;
        this.venta = new Venta();
        this.ventaDAO = new VentaDAO();
        this.productoDAO = new ProductoDAO();
    }
    
    public void cargarProductosEnCombo() {
        
        listaProductos= productoDAO.listar();
        vista.cbox_producto1.removeAllItems();
        mapaProductos.clear();

        for (Producto p : listaProductos) {
            vista.cbox_producto1.addItem(p.getNombreProducto());
            mapaProductos.put(p.getNombreProducto(),p);
        }
    }
    
    public Producto getProductoSeleccionadoRV() {

        String nombre = vista.cbox_producto1.getSelectedItem().toString();
        return mapaProductos.get(nombre);
    }
    
    public void TotalPagar(){
        Totalpagar=0.00;
        int fila = vista.t_regVent.getRowCount();
        for(int i=0; i <fila;i++){
            double cal= Double.parseDouble(String.valueOf(vista.t_regVent.getModel().getValueAt(i, 3)));
            Totalpagar +=cal;
            
        }
        vista.txtF_total.setText(String.format("%.2f", Totalpagar));
    }
    
    public void eliminarVenta(){
        DefaultTableModel model = (DefaultTableModel) vista.t_regVent.getModel();
        model.removeRow(vista.t_regVent.getSelectedRow());
        TotalPagar();
    
    }
    
    
    
    public void productoSeleccionado() {

        Producto producto = getProductoSeleccionadoRV();
        if (producto == null) return;

        vista.txtF_precio.setText(String.valueOf(producto.getPrecio()));

        try (Connection con = ConnectionMySQL.getConexion()) {

            stockOriginal = inventarioDAO.obtenerStockActual(con, producto.getIdproducto());

            vista.txtF_stockDis.setText(String.valueOf(stockOriginal));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Obteniendo stock");
        }

        vista.txtF_cantidad.setText("");
        vista.txtF_total.setText("0.00");
        vista.txtF_cantidad.requestFocus();

        vista.btn_agregarVenta.setEnabled(stockOriginal > 0);
        vista.btn_deleteVenta.setEnabled(stockOriginal>0);
    }
    
    

    public void agregarProducto() {
        if(!"".equals(vista.txtF_cantidad.getText())){
            int cantidad = Integer.parseInt(vista.txtF_cantidad.getText());
            Producto prod= getProductoSeleccionadoRV();
            double precio= Double.parseDouble(vista.txtF_precio.getText());
            double total= cantidad*precio;
            int stockDisponible = Integer.parseInt(vista.txtF_stockDis.getText());

            if (stockDisponible >= cantidad) {
                item +=1;
                DefaultTableModel modelo = (DefaultTableModel) vista.t_regVent.getModel();
                
                for (int i = 0; i < vista.t_regVent.getRowCount(); i++) {
                    Producto pTabla = (Producto) vista.t_regVent.getValueAt(i, 1);
                    Producto pSeleccionado = getProductoSeleccionadoRV();
                    if(pTabla.getIdproducto()==pSeleccionado.getIdproducto()){
                        JOptionPane.showMessageDialog(null, "El producto ya esta resgistrado");
                        return;
                    }
                }
                
                ArrayList lista= new ArrayList();
                lista.add(item);
                lista.add(cantidad);
                lista.add(prod);
                lista.add(precio);
                lista.add(total);
                Object[] o = new Object[4];
                o[0] = lista.get(1);
                o[1] = lista.get(2);
                o[2] = lista.get(3);
                o[3] = lista.get(4);
                
                modelo.addRow(o);
                vista.t_regVent.setModel(modelo);
                TotalPagar();
                LimpiarVenta();
            }else{
                JOptionPane.showMessageDialog(null, "Stock insuficiente");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
        }
    }
    
    public void autocompletarCliente() {

        String dni = vista.txtF_ruc.getText();

        if (dni.length() >= 8) {  // cuando tenga al menos 8 caracteres

            Cliente c = clienteDAO.buscarPorRUC(dni);

            if (c != null) {
                cl=c;
                vista.txtF_nombre.setText(""+c.getNombres());
            } else {
                cl=null;
                vista.txtF_nombre.setText("");
            }
        }
    }
    
    public void ProcesarVentaCompleta(){
            Connection con = null;

        try {
            con = ConnectionMySQL.getConexion();
            con.setAutoCommit(false); // 🔥 INICIA TRANSACCIÓN

            // 1️⃣ Crear venta
            double monto = Totalpagar;
            venta = new Venta();
            venta.setIdcliente( cl.getIdcliente());
            venta.setTotal(monto);

            int idVenta = ventaDAO.insertarConConexion(con, venta);

            if (idVenta == -1) {
                throw new SQLException("No se pudo insertar la venta");
            }

            // 2️⃣ Insertar detalles + actualizar stock
            for (int i = 0; i < vista.t_regVent.getRowCount(); i++) {

                Producto prod = (Producto) vista.t_regVent.getValueAt(i, 1);
                int cantidad = Integer.parseInt(vista.t_regVent.getValueAt(i, 0).toString());
                double precio = Double.parseDouble(vista.t_regVent.getValueAt(i, 2).toString());

                // Insertar detalle
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdVenta(idVenta);
                detalle.setIdProducto(prod.getIdproducto());
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precio);

                detalleDAO.insertarConConexion(con, detalle);

                // Actualizar stock
                boolean stockOk = ventaDAO.registrarSalidaStock(con, prod, cantidad);

                if (!stockOk) {
                    throw new SQLException("Stock insuficiente para: " + prod.getNombreProducto());
                }
            }

            // 3️⃣ Confirmar todo
            con.commit();
            JOptionPane.showMessageDialog(null, "Venta registrada correctamente");

        } catch (Exception e) {

            try {
                if (con != null) {
                    con.rollback(); // 🔥 Revierte todo
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Error en la venta: " + e.getMessage());

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private void LimpiarVenta(){
        vista.txtF_cantidad.setText("");
        vista.txtF_stockDis.setText("");
        vista.txtF_precio.setText("");
    }
}
