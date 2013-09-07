/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import peralcaldia.model.Estados;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;

/**
 *
 * @author alex
 */
public class verpagos {
    private String mespagado;
    private BigDecimal monto;
    private Estados estapagado;
    private Inmuebles inmueble;
    private Negocios negocio;
    

    public verpagos() {
    }

    public verpagos(String mespagado, BigDecimal monto, Estados estapagado, Inmuebles inmueble, Negocios negocio) {
        this.mespagado = mespagado;
        this.monto = monto;
        this.estapagado = estapagado;        
        this.inmueble = inmueble;
        this.negocio = negocio;
    }

    public String getMespagado() {
        return mespagado;
    }

    public void setMespagado(String mespagado) {
        this.mespagado = mespagado;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Estados getEstapagado() {
        return estapagado;
    }

    public void setEstapagado(Estados estapagado) {
        this.estapagado = estapagado;
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
