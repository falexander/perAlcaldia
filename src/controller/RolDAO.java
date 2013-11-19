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
public class RolDAO extends AbstractDAO {

    /*Metodo que Permite la Recuperación de la propiedad nombre de cada uno de los roles
     ingresados en el sistema a traves de la entidad Mapeada*/
    public List find_all_roles() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select rol from Roles order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que Permite la Recuperación de la propiedad nombre de cada uno de los roles
     ingresados en el sistema a traves de la entidad Mapeada pero en base a una condicion especifica*/
    public List find_roles_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select rol from Roles where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
