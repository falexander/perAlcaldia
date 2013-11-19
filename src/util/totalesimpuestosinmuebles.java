/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import java.util.Date;
import peralcaldia.model.Impuestos;

/**
 *
 * @author alex
 */
/*Clase utilizada para almacenar el detalle de los totales impuestos generados durante la 
 * evaluacion de las diferentes formulas asignadas a los distintos impuestos ingresados 
 * en el sistema*/
public class totalesimpuestosinmuebles {
    private String NImpuesto;
    private int idimpuesto;
    private Impuestos impuesto;
    private BigDecimal totalimpuesto;
    private Date fechainicio;
    private Date fechafin;

    /*Constructores*/
    public totalesimpuestosinmuebles() {
    }

    public totalesimpuestosinmuebles(String NImpuesto, BigDecimal totalimpuesto, Date fechainicio, Date fechafin, int idimpuesto, Impuestos impuesto) {
        this.NImpuesto = NImpuesto;
        this.totalimpuesto = totalimpuesto;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
        this.idimpuesto = idimpuesto;
        this.impuesto = impuesto;
    }

    /*Getter y Setters*/
    public String getNImpuesto() {
        return NImpuesto;
    }

    public void setNImpuesto(String NImpuesto) {
        this.NImpuesto = NImpuesto;
    }

    public BigDecimal getTotalimpuesto() {
        return totalimpuesto;
    }

    public void setTotalimpuesto(BigDecimal totalimpuesto) {
        this.totalimpuesto = totalimpuesto;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public int getIdimpuesto() {
        return idimpuesto;
    }

    public void setIdimpuesto(int idimpuesto) {
        this.idimpuesto = idimpuesto;
    }

    public Impuestos getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuestos impuesto) {
        this.impuesto = impuesto;
    }
}