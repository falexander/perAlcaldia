/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author alex
 */
/*Clase Detallepagos que representa a la entidad detallepagos de la Base de Datos modelo de trabajo de  
 * hibernate con anotaciones*/
@Entity
@Table(name="detallepagos"
    ,schema="public"
)
public class Detallepagos implements java.io.Serializable {
    private int id;
    private Pagos pagos;
    private Impuestos impuestos;
    private BigDecimal monto_impuesto;

    public Detallepagos() {
    }

    public Detallepagos(int id) {
        this.id = id;
    }

    public Detallepagos(int id, Pagos pagos, Impuestos impuestos, BigDecimal monto_impuesto) {
        this.id = id;
        this.pagos = pagos;
        this.impuestos = impuestos;
        this.monto_impuesto = monto_impuesto;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_detalle_pagos",sequenceName="detalle_pagos_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_detalle_pagos")    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pagos_id")    
    public Pagos getPagos() {
        return pagos;
    }

    public void setPagos(Pagos pagos) {
        this.pagos = pagos;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="impuestos_id")
    public Impuestos getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Impuestos impuestos) {
        this.impuestos = impuestos;
    }

    @Column(name="monto_impuesto", precision=17, scale=17)
    public BigDecimal getMonto_impuesto() {
        return monto_impuesto;
    }

    public void setMonto_impuesto(BigDecimal monto_impuesto) {
        this.monto_impuesto = monto_impuesto;
    }
    
}
