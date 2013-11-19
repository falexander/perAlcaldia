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
public class GrDAO extends AbstractDAO{

    /*Metodo que Permite la Recuperación de la descripcion de todos los Giros Ingresados en el Sistema*/
    public List find_all_giros() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select descripcion from Giros order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que Permite la Recuperación de la descripcion de todos los Giros Ingresados en el Sistema en Base
      a una Condicion Especificada*/
    public List find_giros_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select descripcion from Giros where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
