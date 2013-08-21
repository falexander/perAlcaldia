/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;

/**
 *
 * @author alex
 */
public class verpagos {
    private String mespagado;
    private BigDecimal monto;
    private boolean estapagado;

    public verpagos() {
    }

    public verpagos(String mespagado, BigDecimal monto, boolean estapagado) {
        this.mespagado = mespagado;
        this.monto = monto;
        this.estapagado = estapagado;        
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

    public boolean isEstapagado() {
        return estapagado;
    }

    public void setEstapagado(boolean estapagado) {
        this.estapagado = estapagado;
    }      
    
}
