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
    private Date fechainicio;
    private Date fechafin;

    public totalesimpuestosinmuebles() {
    }

    public totalesimpuestosinmuebles(String NImpuesto, BigDecimal totalimpuesto, Date fechainicio, Date fechafin) {
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

          
}