package peralcaldia.model;
// Generated 08-10-2013 10:26:32 AM by Hibernate Tools 3.2.1.GA


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
 * Impuestosinmuebles generated by hbm2java
 */
@Entity
@Table(name="impuestosinmuebles"
    ,schema="public"
)
public class Impuestosinmuebles  implements java.io.Serializable {


     private int id;
     private Inmuebles inmuebles;
     private Impuestos impuestos;

    public Impuestosinmuebles() {
    }

	
    public Impuestosinmuebles(int id) {
        this.id = id;
    }
    public Impuestosinmuebles(int id, Inmuebles inmuebles, Impuestos impuestos) {
       this.id = id;
       this.inmuebles = inmuebles;
       this.impuestos = impuestos;
    }
   
    @Id 
    @SequenceGenerator(name="secuencia_impuestosinmuebles",sequenceName="impuestosinmuebles_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_impuestosinmuebles")    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inmuebles_id")
    public Inmuebles getInmuebles() {
        return this.inmuebles;
    }
    
    public void setInmuebles(Inmuebles inmuebles) {
        this.inmuebles = inmuebles;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="impuestos_id")
    public Impuestos getImpuestos() {
        return this.impuestos;
    }
    
    public void setImpuestos(Impuestos impuestos) {
        this.impuestos = impuestos;
    }




}


