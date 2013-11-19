/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.model;

import java.math.BigDecimal;
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
/*Clase Montosimpuestosnegocios que representa a la entidad montosimpuestosnegocios de la Base de Datos modelo de trabajo de  
 * hibernate con anotaciones*/
@Entity
@Table(name="montosimpuestosnegocios"
    ,schema="public"
)
public class Montosimpuestosnegocios {
    private int id;
    private Negocios negocios;
    private Estados estados;
    private BigDecimal monto;
    private String fechainicio;
    private String fechafin;
    private String uso;

    public Montosimpuestosnegocios() {
    }

    public Montosimpuestosnegocios(int id, Negocios negocios, Estados estados, BigDecimal monto, String fechainicio, String fechafin, String uso) {
        this.id = id;
        this.negocios = negocios;
        this.estados = estados;
        this.monto = monto;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
        this.uso = uso;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_montosimpuestosnegocios",sequenceName="montosimpuestosnegocios_id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_montosimpuestosnegocios")            
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    @Column(name="monto")    
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
    @Column(name="fecha_inicio")
    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }
    
    @Column(name="fecha_fin")    
    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }
    
    @Column(name="uso")
    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }
    
}
