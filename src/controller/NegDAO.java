/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author alex
 */
/*Repositorio General de Datos que Extiende el Repositorio Principal, Pero que permite a su Vez la Obtencion de Datos
  Especificos de la Entidad Mapeada de la BD sin necesidad de Recuperar la Instancia Completa*/
public class NegDAO extends AbstractDAO{
    
    /*Metodo que Permite la Recuperación de la Propiedad Nombre de la Empresa de Todos los Negocios Ingresados en el Sistema
      a través de la Entidad Mapeada*/
    public List findnames(){
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select nombreempresa from Negocios order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }                        
        return objects;
    }
    /*Metodo que Permite la Recuperación de la Propiedad Nombre de la Empresa de Todos los Negocios Ingresados en el Sistema
      a través de la Entidad Mapeada Pero en Base a una Condicion Especifica*/
    public List findnameswhereStatement(String whereStatement){
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select nombreempresa from Negocios where "  +  whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }                        
        return objects;
    }
    
    /*Metodo que Permite la Recuperación de la Propiedad Direccion de Todos los Negocios Ingresados en el Sistema
      a través de la Entidad Mapeada Pero en Base a una Condicion Especifica*/
    public List finddireccionwhereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select direccion from Negocios where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
    
}
