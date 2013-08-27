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
public class totalesimpuestosinmuebles {
    private String NImpuesto;
    private BigDecimal TImpuesto;

    public totalesimpuestosinmuebles() {
    }

    public totalesimpuestosinmuebles(String NImpuesto, BigDecimal TImpuesto) {
        this.NImpuesto = NImpuesto;
        this.TImpuesto = TImpuesto;
    }

    public String getNImpuesto() {
        return NImpuesto;
    }

    public void setNImpuesto(String NImpuesto) {
        this.NImpuesto = NImpuesto;
    }

    public BigDecimal getTImpuesto() {
        return TImpuesto;
    }

    public void setTImpuesto(BigDecimal TImpuesto) {
        this.TImpuesto = TImpuesto;
    }
    
    
    
}
