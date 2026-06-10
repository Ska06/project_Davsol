/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.ClienteDAO;
import Modelo.Cliente;
import Vista.Sistema;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aalex
 */
public class ClienteController {
    private Sistema vista;
    private ClienteDAO clienteDAO;

    public ClienteController(Sistema vista) {
        this.vista = vista;
        this.clienteDAO= new ClienteDAO();
    }
   
    public void agregarCliente() {
        
        Cliente c = new Cliente();
        c.setDniRUC(vista.txtF_dni.getText());
        c.setNombres(vista.txtF_name.getText());
        c.setTelefono(vista.txtF_telefono.getText());
        c.setCorreo(vista.txtF_correo.getText());

        DefaultTableModel modelo = (DefaultTableModel) vista.t_cliente.getModel();

        modelo.addRow(new Object[]{
            c.getIdcliente(),
            c.getDniRUC(),
            c.getNombres(),
            c.getTelefono(),
            c.getCorreo()
        });

        limpiarCampos();
    }

    private void limpiarCampos() {
        vista.txtF_dni.setText("");
        vista.txtF_name.setText("");
        vista.txtF_telefono.setText("");
        vista.txtF_correo.setText("");
    }
    
    public void actualizarCliente() {

        int fila = vista.t_cliente.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla");
            return;
        }

        int id = (int) vista.t_cliente.getValueAt(fila, 0);

        Cliente c = new Cliente();
        c.setIdcliente(id);
        c.setDniRUC(vista.txtF_dni.getText());
        c.setNombres(vista.txtF_name.getText());
        c.setTelefono(vista.txtF_telefono.getText());
        c.setCorreo(vista.txtF_correo.getText());

        boolean resultado = clienteDAO.actualizarCliente(c);

        if (resultado) {
            listarClientes();
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "Cliente actualizado");
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar");
        }
    }
    
    public void eliminarCliente() {

        int fila = vista.t_cliente.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
            return;
        }

        int id = (int) vista.t_cliente.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Seguro que desea eliminar?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            boolean resultado = clienteDAO.eliminarCliente(id);

            if (resultado) {
                listarClientes();
                limpiarCampos();
                JOptionPane.showMessageDialog(null, "Cliente eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar");
            }
        }
    }
    
    public void seleccionarClienteTabla() {

        int fila = vista.t_cliente.getSelectedRow();
        if (fila != -1) {
            
            vista.txtF_dni.setText(vista.t_cliente.getValueAt(fila, 1).toString());
            vista.txtF_name.setText(vista.t_cliente.getValueAt(fila, 2).toString());
            vista.txtF_telefono.setText(vista.t_cliente.getValueAt(fila, 3).toString());
            vista.txtF_correo.setText(vista.t_cliente.getValueAt(fila, 4).toString());
        }
    }
    
    public void listarClientes() {
    List<Cliente> lista = clienteDAO.listar();

    DefaultTableModel modelo = (DefaultTableModel) vista.t_cliente.getModel();
    modelo.setRowCount(0);

    for (Cliente c : lista) {
        modelo.addRow(new Object[]{
            c.getIdcliente(),
            c.getDniRUC(),
            c.getNombres(),
            c.getTelefono(),
            c.getCorreo()
        });
    }
}
    
}
