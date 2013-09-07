/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author alex
 */
public class totalesimpuestosinmuebles {
    private String NImpuesto;
    private BigDecimal totalimpuesto;
    private String fechainicio;
    private String fechafin;

    public totalesimpuestosinmuebles() {
    }

    public totalesimpuestosinmuebles(String NImpuesto, BigDecimal totalimpuesto, String fechainicio, String fechafin) {
        this.NImpuesto = NImpuesto;
        this.totalimpuesto = totalimpuesto;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
    }

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

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

          
}