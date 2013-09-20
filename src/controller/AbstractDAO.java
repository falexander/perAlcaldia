/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import util.HibernateUtil;
/**
 *
 * @author luis
 */
public class AbstractDAO {
    protected SessionFactory sessionFactory = getSessionFactory();
    private Transaction transaction;

    protected SessionFactory getSessionFactory() {
        try {
            return HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Could not locate SessionFactory in JNDI");
        }
    }

    public void save(Object obj) {
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().save(obj);
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void update(Object obj) {
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().update(obj);
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void delete(Object obj) {
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().delete(obj);
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public List findAll(Class clazz) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " order by id");
            objects = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public List findByWhereStatement(Class clazz, String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " where " + whereStatement + " order by id");
            objects = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }
    
    public Object findByWhereStatementoneobj(Class clazz, String whereStatement) {
        Object objects = null;
        List lista=null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " where " + whereStatement);
            lista = query.list();            
            Iterator it = lista.iterator();            
                while(it.hasNext()){
                    objects = (Object) it.next();
                }                    
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }
    
    public Object findByWhereStatementmaximoobjeto(Class clazz, String whereStatement) {
        Object objects = null;
        List lista=null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " where " + whereStatement + " ORDER BY id ASC LIMIT 1");
            lista = query.list();            
            Iterator it = lista.iterator();            
                while(it.hasNext()){
                    objects = (Object) it.next();
                }                    
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }

}
