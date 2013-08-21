package peralcaldia.model;
// Generated 08-10-2013 10:26:32 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Impuestos generated by hbm2java
 */
@Entity
@Table(name="impuestos"
    ,schema="public"
)
public class Impuestos  implements java.io.Serializable {


     private int id;
     private String nombre;
     private String formula;
     private Set<Montosimpuestos> montosimpuestoses = new HashSet<Montosimpuestos>(0);
     private Set<Impuestosinmuebles> impuestosinmuebleses = new HashSet<Impuestosinmuebles>(0);

    public Impuestos() {
    }

	
    public Impuestos(int id) {
        this.id = id;
    }
    public Impuestos(int id, String nombre, String formula, Set montosimpuestoses, Set impuestosinmuebleses) {
       this.id = id;
       this.nombre = nombre;
       this.formula = formula;
       this.montosimpuestoses = montosimpuestoses;
       this.impuestosinmuebleses = impuestosinmuebleses;
    }
   
    @Id 
    @SequenceGenerator(name="secuencia_impuestos",sequenceName="impuestos_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_impuestos")    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="nombre")
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Column(name="formula")    
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }    
    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="impuestos")
    public Set<Montosimpuestos> getMontosimpuestoses() {
        return this.montosimpuestoses;
    }    
    
    public void setMontosimpuestoses(Set<Montosimpuestos> montosimpuestoses) {
        this.montosimpuestoses = montosimpuestoses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="impuestos")
    public Set<Impuestosinmuebles> getImpuestosinmuebleses() {
        return this.impuestosinmuebleses;
    }
    
    public void setImpuestosinmuebleses(Set<Impuestosinmuebles> impuestosinmuebleses) {
        this.impuestosinmuebleses = impuestosinmuebleses;
    }




}


