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
@Entity
@Table(name="tiposcomercio"
    ,schema="public"
)
public class Tiposcomercio {
    private int id;
    private String actividadeconomica;
    private Set<Negocios> negocioses = new HashSet<Negocios>(0);

    public Tiposcomercio() {
    }

    public Tiposcomercio(int id, String actividadeconomica, Set negocioses) {
        this.id = id;
        this.actividadeconomica = actividadeconomica;
        this.negocioses = negocioses;
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
}
