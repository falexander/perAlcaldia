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
 * Zonas generated by hbm2java
 */
@Entity
@Table(name="zonas"
    ,schema="public"
)
public class Zonas  implements java.io.Serializable {


     private int id;
     private String zona;
     private String zonecode;
     private int correlativozona;
     private Set<Inmuebles> inmuebleses = new HashSet<Inmuebles>(0);

    public Zonas() {
    }

	
    public Zonas(int id) {
        this.id = id;
    }
    public Zonas(int id, String zona, Set inmuebleses, String zonecode, int correlativozona) {
       this.id = id;
       this.zona = zona;
       this.inmuebleses = inmuebleses;
       this.zonecode = zonecode;
       this.correlativozona = correlativozona;
    }
   
    @Id 
    @SequenceGenerator(name="secuencia_zonas",sequenceName="zonas_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_zonas")    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="zona")
    public String getZona() {
        return this.zona;
    }
    
    public void setZona(String zona) {
        this.zona = zona;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="zonas")
    public Set<Inmuebles> getInmuebleses() {
        return this.inmuebleses;
    }
    
    public void setInmuebleses(Set<Inmuebles> inmuebleses) {
        this.inmuebleses = inmuebleses;
    }
    @Column(name="zone_code")
    public String getZonecode() {
        return zonecode;
    }

    public void setZonecode(String zonecode) {
        this.zonecode = zonecode;
    }
    
    @Column(name="correlativo_zona")
    public int getCorrelativozona() {
        return correlativozona;
    }

    public void setCorrelativozona(int correlativozona) {
        this.correlativozona = correlativozona;
    }


}


