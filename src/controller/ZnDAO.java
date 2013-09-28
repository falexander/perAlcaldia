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
public class ZnDAO extends AbstractDAO{

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
