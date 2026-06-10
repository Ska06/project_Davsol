/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author aalex
 */
public class ComprobantePago {
    private int idComprobante;
    private String tipoComprobante,numero;
    private Date fechaEmision;
    private double montoTotal;

    public ComprobantePago(String tipoComprobante, String numero, double montoTotal) {
        this.tipoComprobante = tipoComprobante;
        this.numero = numero;
        this.montoTotal = montoTotal;
        this.fechaEmision=new Date();
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public String getNumero() {
        return numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }
    
    
    
    
    
}
