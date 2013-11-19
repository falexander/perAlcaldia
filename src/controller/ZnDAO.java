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
 * Especificos de la Entidad Mapeada de la BD sin necesidad de Recuperar la Instancia Completa*/
public class ZnDAO extends AbstractDAO{

    /*Metodo que permite la recuperacion de la propiedad nombre de la zona de todas las zonas
     * ingresadas a la BD por medio de la apliaacion y usando la entidad mapeada correspondiente
     */
    public List find_all_zonas() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select zona from Zonas order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

     /*Metodo que permite la recuperacion de la propiedad nombre de la zona de todas las zonas
     * ingresadas a la BD por medio de la apliaacion y usando la entidad mapeada correspondiente
     * Pero a su vez restringiendo en base a una condición de entrada*/
    public List find_zonas_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select zona from Zonas where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
