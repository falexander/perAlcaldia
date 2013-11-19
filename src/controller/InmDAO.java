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
public class InmDAO extends AbstractDAO {

    /*Metodo que Permite la Recuperación de la Propiedad Direccion de Todos los Inmuebles Ingresados en la BD
      a traves de la entidad mapeada*/
    public List find_direcciones() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select direccion from Inmuebles order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que Permite la Recuperación de la Propiedad Direccion de Todos los Inmuebles Ingresados en la BD
      a traves de la entidad mapeada pero en base a una condicion especifica*/
    public List find_direcciones_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select direccion from Inmuebles where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

}
