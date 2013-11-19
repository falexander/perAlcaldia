/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.model;

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
/**
 *
 * @author alex
 */
/*Clase Tiposcomercio que representa a la entidad tiposcomercio de la Base de Datos modelo de trabajo de  
 * hibernate con anotaciones*/
@Entity
@Table(name="tiposcomercio"
    ,schema="public"
)
public class Tiposcomercio {
    private int id;
    private String actividadeconomica;
    private Set<Negocios> negocioses = new HashSet<Negocios>(0);
    private Estados estados;
    private Giros giros;

    public Tiposcomercio() {
    }

    public Tiposcomercio(int id, String actividadeconomica, Set negocioses, Estados estados, Giros giros) {
        this.id = id;
        this.actividadeconomica = actividadeconomica;
        this.negocioses = negocioses;
        this.estados = estados;
        this.giros = giros;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_tiposcomercio",sequenceName="tiposcomercio_id_seq", allocationSize=1)    
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_tiposcomercio")                
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="actividadeconomica")    
    public String getActividadeconomica() {
        return actividadeconomica;
    }

    public void setActividadeconomica(String actividadeconomica) {
        this.actividadeconomica = actividadeconomica;
    }    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="tipos")    
    public Set<Negocios> getNegocioses() {
        return negocioses;
    }

    public void setNegocioses(Set<Negocios> negocioses) {
        this.negocioses = negocioses;
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
    @JoinColumn(name="giros_id")
    public Giros getGiros() {
        return giros;
    }

    public void setGiros(Giros giros) {
        this.giros = giros;
    }

}
