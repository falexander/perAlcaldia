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
@Table(name="formulas"
    ,schema="public"
)
public class Formulas {  
    private int id;
    private Impuestos impuestos;
    private Estados estados;
    private String fechainicio;
    private String fechafin;
    private String formula;

    public Formulas() {
    }

    public Formulas(int id, Impuestos impuestos, Estados estados, String fechainicio, String fechafin, String formula) {
        this.id = id;
        this.impuestos = impuestos;
        this.estados = estados;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
        this.formula = formula;
    }
    
    @Id 
    @SequenceGenerator(name="secuencia_formulas",sequenceName="formulas_id_seq", allocationSize=1)    
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_formulas")            
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="impuestos_id")    
    public Impuestos getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Impuestos impuestos) {
        this.impuestos = impuestos;
    }

@ManyToOne(fetch=FetchType.LAZY)    
    @JoinColumn(name="estados_id")        
    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }
    
    @Column(name="fechainicio")
    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }
    
    @Column(name="fechafin")    
    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }
    
    @Column(name="formula")    
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }    
}
