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
public class UserDAO extends AbstractDAO {

    public List find_usuarios() {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select nombres || ' ' || apellidos from Usuarios order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    public List find_usuarios_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select nombres || ' ' || apellidos from Usuarios where " + whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
