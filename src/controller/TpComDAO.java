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
public class TpComDAO extends AbstractDAO {

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
