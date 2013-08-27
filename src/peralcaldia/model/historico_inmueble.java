/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.model;

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
@Table(name="historico_inmueble"
    ,schema="public"
)
public class historico_inmueble {
 private int id;
 private Inmuebles inmuebleshid;
 private Contribuyentes contribuyenteshid;
 private Date fecha_inicio;
 private Date fecha_fin;

    public historico_inmueble() {
    }

    public historico_inmueble(int id) {
        this.id = id;
    }

    public historico_inmueble(int id, Inmuebles inmuebleshid, Contribuyentes contribuyenteshid, Date fecha_inicio, Date fecha_fin) {
        this.id = id;
        this.inmuebleshid = inmuebleshid;
        this.contribuyenteshid = contribuyenteshid;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }
 
    @Id 
    @SequenceGenerator(name="secuencia_historico_inmueble",sequenceName="historico_inmueble_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_historico_inmueble")    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inmuebles_id")    
    public Inmuebles getInmuebleshid() {
        return inmuebleshid;
    }

    public void setInmuebleshid(Inmuebles inmuebleshid) {
        this.inmuebleshid = inmuebleshid;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contribuyentes_id")        
    public Contribuyentes getContribuyenteshid() {
        return contribuyenteshid;
    }

    public void setContribuyenteshid(Contribuyentes contribuyenteshid) {
        this.contribuyenteshid = contribuyenteshid;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_inicio", length=13)    
    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_fin", length=13)        
    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
    

}
