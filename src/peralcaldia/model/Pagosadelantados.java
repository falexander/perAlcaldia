/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author alex
 */
@Entity
@Table(name="pagosadelantados"
    ,schema="public"
)
public class Pagosadelantados {
    private int id;
    private Inmuebles inmuebles;
    private Negocios negocios;
    private BigDecimal saldo;
    private String norecibo;
    private Date fechapago;
    private BigDecimal montodelpago;
    private Estados estados;
    private Boleta boleta;

    public Pagosadelantados() {
    }

    public Pagosadelantados(int id, Inmuebles inmuebles, Negocios negocios, BigDecimal saldo, String norecibo, Date fechapago, BigDecimal montodelpago, Estados estados,
            Boleta boleta) {
        this.id = id;
        this.inmuebles = inmuebles;
        this.negocios = negocios;
        this.saldo = saldo;
        this.norecibo = norecibo;
        this.fechapago = fechapago;
        this.montodelpago = montodelpago;
        this.estados = estados;
        this.boleta = boleta;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_pagosadelantados",sequenceName="pagosadelantados_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_pagosadelantados")                
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inmuebles_id")                                
    public Inmuebles getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Inmuebles inmuebles) {
        this.inmuebles = inmuebles;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="negocios_id")
    public Negocios getNegocios() {
        return negocios;
    }

    public void setNegocios(Negocios negocios) {
        this.negocios = negocios;
    }
    
    @Column(name="saldo")        
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    @Column(name="norecibo")
    public String getNorecibo() {
        return norecibo;
    }

    public void setNorecibo(String norecibo) {
        this.norecibo = norecibo;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_pago", length=13)        
    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }
 
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estados_id")
    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="boleta_id")
    public Boleta getBoleta() {
        return boleta;
    }

    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
    }

    @Column(name="montodelpago")        
    public BigDecimal getMontodelpago() {
        return montodelpago;
    }

    public void setMontodelpago(BigDecimal montodelpago) {
        this.montodelpago = montodelpago;
    }

}
