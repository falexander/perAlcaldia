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
/*Clase Negocios que representa a la entidad negocios de la Base de Datos modelo de trabajo de  
 * hibernate con anotaciones*/
@Entity
@Table(name="negocios"
    ,schema="public"
)
public class Negocios {
    private int id;
    private Contribuyentes contribuyentes;
    private Estados estados;
    private Giros giros;
    private Tiposcomercio tipos;
    private String nombreempresa;
    private String telefono;
    private Date fecha_registro;
    private Set<Pagos> pagoes = new HashSet<Pagos>(0);
    private Set<Montosimpuestosnegocios> mntimpuestoes = new HashSet<Montosimpuestosnegocios>(0);
    private Set<Pagosadelantados> padelantados = new HashSet<Pagosadelantados>(0);
    private Set<Boleta> boletaes = new HashSet<Boleta>(0);
    private String direccion;
    private String nis;
    private String cuentacorriente;

    public Negocios() {
    }

    public Negocios(int id, Contribuyentes contribuyentes, Estados estados, Giros giros, Tiposcomercio tipos, String nombreempresa, String telefono, Date fecha_registro, Set pagoes, Set mntimpuestoes, Set padelantados, Set boletaes, String nis, String cuentacorriente) {
        this.id = id;
        this.contribuyentes = contribuyentes;
        this.estados = estados;
        this.giros = giros;
        this.tipos = tipos;
        this.nombreempresa = nombreempresa;
        this.telefono = telefono;
        this.fecha_registro = fecha_registro;
        this.pagoes = pagoes;
        this.mntimpuestoes = mntimpuestoes;
        this.padelantados= padelantados;
        this.boletaes = boletaes;
        this.nis = nis;
        this.cuentacorriente = cuentacorriente;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_negocios",sequenceName="negocios_id_seq", allocationSize=1)    
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_negocios")                    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contribuyentes_id")            
    public Contribuyentes getContribuyentes() {
        return contribuyentes;
    }

    public void setContribuyentes(Contribuyentes contribuyentes) {
        this.contribuyentes = contribuyentes;
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
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tiposcomercio_id")                        
    public Tiposcomercio getTipos() {
        return tipos;
    }

    public void setTipos(Tiposcomercio tipos) {
        this.tipos = tipos;
    }
    
    @Column(name="nombreempresa")        
    public String getNombreempresa() {
        return nombreempresa;
    }

    public void setNombreempresa(String nombreempresa) {
        this.nombreempresa = nombreempresa;
    }
    
    @Column(name="telefono")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_registro", length=13)    
    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="negocios")
    public Set<Pagos> getPagoes() {
        return pagoes;
    }

    public void setPagoes(Set<Pagos> pagoes) {
        this.pagoes = pagoes;
    }

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="negocios")
    public Set<Montosimpuestosnegocios> getMntimpuestoes() {
        return mntimpuestoes;
    }

    public void setMntimpuestoes(Set<Montosimpuestosnegocios> mntimpuestoes) {
        this.mntimpuestoes = mntimpuestoes;
    }

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="negocios")
    public Set<Pagosadelantados> getPadelantados() {
        return padelantados;
    }

    public void setPadelantados(Set<Pagosadelantados> padelantados) {
        this.padelantados = padelantados;
    }

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="negocios")
    public Set<Boleta> getBoletaes() {
        return boletaes;
    }

    public void setBoletaes(Set<Boleta> boletaes) {
        this.boletaes = boletaes;
    }

    @Column(name="direccion")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }    
    
    @Column(name="nis")
    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }
    
    @Column(name="cuentacorriente")
    public String getCuentacorriente() {
        return cuentacorriente;
    }

    public void setCuentacorriente(String cuentacorriente) {
        this.cuentacorriente = cuentacorriente;
    }
    
}
