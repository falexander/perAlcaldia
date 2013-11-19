/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 *
 * @author alex
 */
/*Repositorio General de Datos que Extiende el Repositorio Principal, Pero que permite a su Vez la Obtencion de Datos
 Especificos de la Entidad Mapeada de la BD sin necesidad de Recuperar la Instancia Completa*/
public class UserDAO extends AbstractDAO {

    /*Metodo que permite la recuperación de las propiedades basicas de los usuarios para su visualizacion en el sistema
      y su posterior busqueda, a traves del uso de la entidad mapeada*/
    public List find_usuarios() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui  from Contribuyentes as b inner join b.usuarios as a order by a.id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que permite la recuperación de las propiedades basicas de los usuarios para su visualizacion en el sistema
      y su posterior busqueda, a traves del uso de la entidad mapeada pero Restringida en Base a una Condicion Especifica*/
    public List find_usuarios_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui  from Contribuyentes as b inner join b.usuarios as a where " + whereStatement + " order by a.id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que permite la recuperacion de la propiedad alias de los usuarios ingresados en el sistema a través de la entidad
      Mapeada, pero aplicando una restriccion de entrada*/
    public List find_alias_usuarios(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select alias from Usuarios where " + whereStatement + " order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }    
}
