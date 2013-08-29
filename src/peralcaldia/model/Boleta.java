/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Table(name="boleta"
    ,schema="public"
)
public class Boleta {
    private int id;
    private Inmuebles inmuebles;
    private Negocios negocios;
    private Estados estados;
    private int mesesapagar;
    private BigDecimal montototal;
    private Date fechacancelacion;
    private Set<Pagos> pagoes = new HashSet<Pagos>(0);
    private String recibo;

    public Boleta() {
    }
    
    public Boleta(int id, Inmuebles inmuebles, Negocios negocios, Estados estados, int mesesapagar, BigDecimal montototal, Date fechacancelacion, Set pagoes, String recibo) {
        this.id = id;
        this.inmuebles = inmuebles;
        this.negocios = negocios;
        this.estados = estados;
        this.mesesapagar = mesesapagar;
        this.montototal = montototal;
        this.fechacancelacion = fechacancelacion;
        this.pagoes = pagoes;
        this.recibo = recibo;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_boletas",sequenceName="boleta_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_boletas")                    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inmuebles_id")                                    
    public Inmuebles getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Inmuebles inmuebles) {
        this.inmuebles = inmuebles;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="negocios_id")                                        
    public Negocios getNegocios() {
        return negocios;
    }

    public void setNegocios(Negocios negocios) {
        this.negocios = negocios;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estados_id")
    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }
    
    @Column(name="mesesapagar")            
    public int getMesesapagar() {
        return mesesapagar;
    }

    public void setMesesapagar(int mesesapagar) {
        this.mesesapagar = mesesapagar;
    }
    
    @Column(name="montototal")    
    public BigDecimal getMontototal() {
        return montototal.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setMontototal(BigDecimal montototal) {
        this.montototal = montototal;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_cancelacion", length=13)    
    public Date getFechacancelacion() {
        return fechacancelacion;
    }

    public void setFechacancelacion(Date fechacancelacion) {
        this.fechacancelacion = fechacancelacion;
    }

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="boletas")
    public Set<Pagos> getPagoes() {
        return pagoes;
    }

    public void setPagoes(Set<Pagos> pagoes) {
        this.pagoes = pagoes;
    }
    
    @Column(name="recibo")        
    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

}
