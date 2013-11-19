/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import javax.swing.AbstractAction;
import org.hibernate.Query;

/**
 *
 * @author alex
 */
/*Repositorio General de Datos que Extiende el Repositorio Principal, Pero que permite a su Vez la Obtencion de Datos
 Especificos de la Entidad Mapeada de la BD sin necesidad de Recuperar la Instancia Completa*/
public class TpComDAO extends AbstractDAO {
    
    /*Metodo que permite la Recuperación de la propiedad Actividad Economica de los Diferentes Tipos de Comercio Ingresados
      en la BD a traves del sistema usando la Entidad Mapeada*/
    public List find_all_tiposcomercio() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select actividadeconomica from Tiposcomercio order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que permite la Recuperación de la propiedad Actividad Economica de los Diferentes Tipos de Comercio Ingresados
      en la BD a traves del sistema usando la Entidad Mapeada pero restringiendo en base a una condición especifica*/
    public List find_tiposcomercio_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select actividadeconomica from Tiposcomercio where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
