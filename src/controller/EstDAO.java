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
/*Repositorio General de Datos que Extiende del Repositorio Principal, Pero que permite a su Vez la Obtencion de Datos
  Especificos de las Entidades Mapeadas de la BD sin necesidad de Recuperar la Instancia Completa*/
public class EstDAO extends AbstractDAO {
    
    /*Metodo que Permite la Recuperación de la Descripcion de Todos los Estados desde la Entidad Mapeada de la Base de 
      Datos*/
    public List find_all_estados() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select estado from Estados order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
    /*Metodo que Permite la Recuperación de la Descripcion de Todos los Estados desde la Entidad Mapeada de la Base de
      Datos, de Acuerdo a Una condición en Especifico*/
    public List find_estados_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select estado from Estados where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
