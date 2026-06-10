/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;


import DAO.ProductoDAO;
import Modelo.Producto;
import Vista.Sistema;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.ConnectionMySQL;


/**
 *
 * @author aalex
 */
public class ProductoController {
    private Sistema vista;
    private ProductoDAO productoDAO;
    private List<Producto> listaProductos;
    

    
    
    

    public ProductoController(Sistema vista) {
        this.vista = vista;
        this.productoDAO=new ProductoDAO();
    }

    public void registrarProducto() {

        Producto p = new Producto();
        p.setNombreProducto(vista.txtF_nombreproduct.getText());
        p.setDescripcion(vista.txtF_descripcion.getText());
        p.setPrecio(Double.parseDouble(vista.txtF_precioProduct.getText()));

        boolean resultado = productoDAO.insertar(p);

        if (resultado) {
            listarProductos(); // recarga desde BD
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar producto");
        }
    }

    private void limpiarCampos() {
        vista.txtF_nombreproduct.setText("");
        vista.txtF_descripcion.setText("");
        vista.txtF_precioProduct.setText("");
    }
    
    public void listarProductos() {
        List<Producto> lista = productoDAO.listar();

        DefaultTableModel modelo = (DefaultTableModel) vista.t_product.getModel();
        modelo.setRowCount(0);

        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getIdproducto(),
                p.getNombreProducto(),
                p.getDescripcion(),
                p.getPrecio()
            });
        }
    }
    
    
    
    public void actualizarProducto() {

        int fila = vista.t_product.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla");
            return;
        }

        int id = (int) vista.t_product.getValueAt(fila, 0);

        Producto p = new Producto();
        p.setIdproducto(id);
        p.setNombreProducto(vista.txtF_nombreproduct.getText());
        p.setDescripcion(vista.txtF_descripcion.getText());
        p.setPrecio(Double.parseDouble(vista.txtF_precioProduct.getText()));

        boolean resultado = productoDAO.actualizar(p);

        if (resultado) {
            listarProductos();
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "Producto actualizado");
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar");
        }
    }
    
    public void eliminarProducto() {

        int fila = vista.t_product.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
            return;
        }

        int id = (int) vista.t_product.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Seguro que desea eliminar?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            boolean resultado = productoDAO.eliminar(id);

            if (resultado) {
                listarProductos();
                limpiarCampos();
                JOptionPane.showMessageDialog(null, "Producto eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar");
            }
        }
    }
    
    public void seleccionarProductoTabla() {

        int fila = vista.t_product.getSelectedRow();
        if (fila != -1) {

            vista.txtF_nombreproduct.setText(
                    vista.t_product.getValueAt(fila, 1).toString());

            vista.txtF_descripcion.setText(
                    vista.t_product.getValueAt(fila, 2).toString());

            vista.txtF_precioProduct.setText(
                    vista.t_product.getValueAt(fila, 3).toString());
        }
    }
}
