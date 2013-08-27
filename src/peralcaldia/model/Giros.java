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
@Table(name="giros"
    ,schema="public"
)
public class Giros {
    private int id;
    private Estados estados;
    private String descripcion;
    private Set<Negocios> negocioses = new HashSet<Negocios>(0);

    public Giros() {
    }

    public Giros(int id, Estados estados, String descripcion, Set negocioses) {
        this.id = id;
        this.estados = estados;
        this.descripcion = descripcion;
        this.negocioses = negocioses;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_giros",sequenceName="giros_id_seq", allocationSize=1)    
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_giros")                    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estados_id")        
    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }
    
    @Column(name="descripcion")    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="giros")
    public Set<Negocios> getNegocioses() {
        return negocioses;
    }

    public void setNegocioses(Set<Negocios> negocioses) {
        this.negocioses = negocioses;
    }

}
