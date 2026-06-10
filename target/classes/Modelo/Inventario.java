/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author aalex
 */
public class Inventario {
    private int idinventario,stockActual;
    private Producto producto;

    public Inventario() {
    }
    
    

    public Inventario(int idinventario, int stockActual, Producto producto) {
        this.idinventario = idinventario;
        this.stockActual = stockActual;
        
        this.producto = producto;
    }

    public int getIdinventario() {
        return idinventario;
    }

    public void setIdinventario(int idinventario) {
        this.idinventario = idinventario;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public void aumentarStock(int cantidad) {
        stockActual += cantidad;
    }

    public boolean reducirStock(int cantidad) {
        if (stockActual >= cantidad) {
            stockActual -= cantidad;
            return true;
        }
        return false;
    }
    

    
}
