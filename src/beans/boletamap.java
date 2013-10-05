/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alex
 */
public class boletamap {
    private String contribuyente;
    private int idboleta;
    private Date fechageneracion;
    private String inmneg;
    private int mesespg;
    private BigDecimal total;
    Map<String, Object> parameters = new HashMap();

    public boletamap() {
        
    }

    public boletamap(String contribuyente, int idboleta, Date fechageneracion, String inmneg, int mesespg, BigDecimal total) {
        this.contribuyente = contribuyente;
        this.idboleta = idboleta;
        this.fechageneracion = fechageneracion;
        this.inmneg = inmneg;
        this.mesespg = mesespg;
        this.total = total;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public int getIdboleta() {
        return idboleta;
    }

    public void setIdboleta(int idboleta) {
        this.idboleta = idboleta;
    }

    public Date getFechageneracion() {
        return fechageneracion;
    }

    public void setFechageneracion(Date fechageneracion) {
        this.fechageneracion = fechageneracion;
    }

    public String getInmneg() {
        return inmneg;
    }

    public void setInmneg(String inmneg) {
        this.inmneg = inmneg;
    }

    public int getMesespg() {
        return mesespg;
    }

    public void setMesespg(int mesespg) {
        this.mesespg = mesespg;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Map<String, Object> getParameters() {
        parameters.put("contribuyente", this.getContribuyente());
        parameters.put("idboleta", this.getIdboleta());
        parameters.put("fechageneracion", this.getFechageneracion());
        parameters.put("inmneg", this.getInmneg());
        parameters.put("mesepg", this.getMesespg());
        parameters.put("total", this.getTotal());
        return parameters;
    }
    

    
}
