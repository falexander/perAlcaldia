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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Contribuyentes generated by hbm2java
 */
@Entity
@Table(name="contribuyentes"
    ,schema="public"
)
public class Contribuyentes  implements java.io.Serializable {


     private int id;
     private Usuarios usuarios;
     private String dui;
     private String nit;
     private String niss;
     private String nitjuridico;
     private Set<Inmuebles> inmuebleses = new HashSet<Inmuebles>(0);
     private Set<historico_inmueble> historicosconinm = new HashSet<historico_inmueble>(0);
     private Set<Negocios> negocioses = new HashSet<Negocios>(0);
     

    public Contribuyentes() {
    }

	
    public Contribuyentes(int id) {
        this.id = id;
    }
    public Contribuyentes(int id, Usuarios usuarios, String dui, String nit, String niss, String nitjuridico, Set inmuebleses, Set historicosconinm, Set negocioses) {
       this.id = id;
       this.usuarios = usuarios;
       this.dui = dui;
       this.nit = nit;
       this.niss = niss;
       this.nitjuridico = nitjuridico;
       this.inmuebleses = inmuebleses;
       this.historicosconinm = historicosconinm;
       this.negocioses = negocioses;
    }
   
    @Id 
    @SequenceGenerator(name="secuencia_contribuyentes",sequenceName="contribuyentes_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_contribuyentes")
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuarios_id")
    public Usuarios getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }
    
    @Column(name="dui")
    public String getDui() {
        return this.dui;
    }
    
    public void setDui(String dui) {
        this.dui = dui;
    }
    
    @Column(name="nit")
    public String getNit() {
        return this.nit;
    }
    
    public void setNit(String nit) {
        this.nit = nit;
    }
    
    @Column(name="niss")
    public String getNiss() {
        return this.niss;
    }
    
    public void setNiss(String niss) {
        this.niss = niss;
    }
    
    @Column(name="nitjuridico")
    public String getNitjuridico() {
        return this.nitjuridico;
    }
    
    public void setNitjuridico(String nitjuridico) {
        this.nitjuridico = nitjuridico;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contribuyentes")
    public Set<Inmuebles> getInmuebleses() {
        return this.inmuebleses;
    }
    
    public void setInmuebleses(Set<Inmuebles> inmuebleses) {
        this.inmuebleses = inmuebleses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contribuyenteshid")
    public Set<historico_inmueble> getHistoricosconinm() {
        return historicosconinm;
    }

    public void setHistoricosconinm(Set<historico_inmueble> historicosconinm) {
        this.historicosconinm = historicosconinm;
    }

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contribuyentes")
    public Set<Negocios> getNegocioses() {
        return negocioses;
    }

    public void setNegocioses(Set<Negocios> negocioses) {
        this.negocioses = negocioses;
    }
    

}


