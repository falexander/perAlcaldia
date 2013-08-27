package peralcaldia.model;
// Generated 08-10-2013 10:26:32 AM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Montosimpuestos generated by hbm2java
 */
@Entity
@Table(name="montosimpuestos"
    ,schema="public"
)
public class Montosimpuestos  implements java.io.Serializable {


     private int id;
     private Estados estados;
     private Impuestos impuestos;
     private BigDecimal monto;
     private Date fechainicio;
     private Date fechafin;

    public Montosimpuestos() {
    }

	
    public Montosimpuestos(int id) {
        this.id = id;
    }
    public Montosimpuestos(int id, Estados estados, Impuestos impuestos, BigDecimal monto, Date fechainicio, Date fechafin) {
       this.id = id;
       this.estados = estados;
       this.impuestos = impuestos;
       this.monto = monto;
       this.fechainicio = fechainicio;
       this.fechafin = fechafin;
    }
   
    @Id 
    @SequenceGenerator(name="secuencia_montosimpuestos",sequenceName="montosimpuestos_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_montosimpuestos")    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estados_id")
    public Estados getEstados() {
        return this.estados;
    }
    
    public void setEstados(Estados estados) {
        this.estados = estados;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="impuestos_id")
    public Impuestos getImpuestos() {
        return this.impuestos;
    }
    
    public void setImpuestos(Impuestos impuestos) {
        this.impuestos = impuestos;
    }
    
    @Column(name="monto")
    public BigDecimal getMonto() {
        return this.monto.setScale(2);
    }
    
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="fechainicio", length=13)
    public Date getFechainicio() {
        return this.fechainicio;
    }
    
    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="fechafin", length=13)
    public Date getFechafin() {
        return this.fechafin;
    }
    
    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }




}


