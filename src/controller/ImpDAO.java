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
public class ImpDAO extends AbstractDAO {

    /*Metodo que Permite la Recuperación de todos los nombres de los Impuestos Ingresados en la BD a traves de la Entidad
      Mapeada*/
    public List find_all_impuestos() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select nombre from Impuestos order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que Permite la Recuperacion de todos los nombres de los Impuestos Ingresados en la BD a traves de la Entidad
      Mapeada Pero en Base a Una Condición especificada.*/
    public List find_impuestos_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select nombre from Impuestos where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
