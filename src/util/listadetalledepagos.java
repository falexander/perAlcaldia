/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;

/**
 *
 * @author alex
 */
/*Clase utilizada para almacenar el detalle de los pagos previo a su almacenamiento en la BD*/
public class listadetalledepagos {
    private String periodoevaluar;
    private totalesimpuestosinmuebles totalimpuesto;
    private Inmuebles inmueble;
    private Negocios negocio;
    
    /*Constructores*/
    public listadetalledepagos() {
    }

    public listadetalledepagos(String periodoevaluar, totalesimpuestosinmuebles totalimpuesto, Inmuebles inmueble, Negocios negocio) {
        this.periodoevaluar = periodoevaluar;
        this.totalimpuesto = totalimpuesto;
        this.inmueble = inmueble;
        this.negocio = negocio;
    }

    /*Getters y Setters*/
    public String getPeriodoevaluar() {
        return periodoevaluar;
    }

    public void setPeriodoevaluar(String periodoevaluar) {
        this.periodoevaluar = periodoevaluar;
    }

    public totalesimpuestosinmuebles getTotalimpuesto() {
        return totalimpuesto;
    }

    public void setTotalimpuesto(totalesimpuestosinmuebles totalimpuesto) {
        this.totalimpuesto = totalimpuesto;
    }

    public Inmuebles getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmuebles inmueble) {
        this.inmueble = inmueble;
    }

    public Negocios getNegocio() {
        return negocio;
    }

    public void setNegocio(Negocios negocio) {
        this.negocio = negocio;
    }
}
