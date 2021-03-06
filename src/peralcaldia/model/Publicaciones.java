package peralcaldia.model;
// Generated 08-10-2013 10:26:32 AM by Hibernate Tools 3.2.1.GA


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
 * Publicaciones generated by hbm2java
 */
/*Clase Publicaciones que representa a la entidad publicaciones de la Base de Datos modelo de trabajo de  
 * hibernate con anotaciones*/
@Entity
@Table(name="publicaciones"
    ,schema="public"
)
public class Publicaciones  implements java.io.Serializable {


     private int id;
     private Usuarios usuarios;
     private Categorias categorias;
     private String tema;
     private String contenido;
     private Date fechapublicacion;
     private Date fechamodificacion;
     private Set<Comentarios> comentarioses = new HashSet<Comentarios>(0);

    public Publicaciones() {
    }

	
    public Publicaciones(int id) {
        this.id = id;
    }
    public Publicaciones(int id, Usuarios usuarios, Categorias categorias, String tema, String contenido, Date fechapublicacion, Date fechamodificacion, Set comentarioses) {
       this.id = id;
       this.usuarios = usuarios;
       this.categorias = categorias;
       this.tema = tema;
       this.contenido = contenido;
       this.fechapublicacion = fechapublicacion;
       this.fechamodificacion = fechamodificacion;
       this.comentarioses = comentarioses;
    }
   
    @Id 
    @SequenceGenerator(name="secuencia_publicaciones", sequenceName="Publicaciones_Id_seq", allocationSize=1)
    @Column(name="id", columnDefinition="serial", unique=true, nullable=false, precision=22, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="secuencia_publicaciones")        
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
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="categorias_id")
    public Categorias getCategorias() {
        return this.categorias;
    }
    
    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }
    
    @Column(name="tema")
    public String getTema() {
        return this.tema;
    }
    
    public void setTema(String tema) {
        this.tema = tema;
    }
    
    @Column(name="contenido")
    public String getContenido() {
        return this.contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="fechapublicacion", length=13)
    public Date getFechapublicacion() {
        return this.fechapublicacion;
    }
    
    public void setFechapublicacion(Date fechapublicacion) {
        this.fechapublicacion = fechapublicacion;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="fechamodificacion", length=13)
    public Date getFechamodificacion() {
        return this.fechamodificacion;
    }
    
    public void setFechamodificacion(Date fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="publicaciones")
    public Set<Comentarios> getComentarioses() {
        return this.comentarioses;
    }
    
    public void setComentarioses(Set<Comentarios> comentarioses) {
        this.comentarioses = comentarioses;
    }




}


