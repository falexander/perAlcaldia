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
/*Clase Central destinada al acceso a datos, para el caso es parte del conjunto intermediario entre la Base
  de Datos y el aplicativo, ya que se generaliza el acceso a los datos a través de la generalización de las
  entidades*/
public class AbstractDAO {
    protected SessionFactory sessionFactory = getSessionFactory();
    private Transaction transaction;

    /*Objeto SessionFactory el cual es el que permite realizar las diferentes transacciones
      sobre cualquier objeto siempre que este se encuentre en estado abierto, se cierra de forma
      automatica despues de realizar una transaccion*/
    protected SessionFactory getSessionFactory() {
        try {
            return HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Could not locate SessionFactory in JNDI");
        }
    }

    /*Metodo que permite el Guardado de cualquier tipo de objeto mapeado de la Base de Datos*/
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

    /*Metodo que permite la Actualización de cualquier tipo de objeto mapeado de la Base de Datos*/
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

    /*Metodo que permite el Borrado de cualquier tipo de objeto mapeado de la Base de Datos*/
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
    /*Metodo Para Encontrar Todos los Objetos en General de una Entidad Mapeada de la Base de Datos en Especifico*/
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

    /*Metodo Para Encontrar Todos los Objetos de Una Entidad Mapeada de la Base de Datos Pero que Cumplen Ciertas
      Condiciones indicadas*/
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
    
    /*Metodo Para Encontrar Un objeto en Especifico de una Entidad Mapeada de la Base de Datos de Acuerdo a Una Condicion que
      Identifica Como Unico el Objeto a Buscar*/
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
    
    /*Metodo Para Encontrar un Objeto en Especifico de una Entidad Mapeada de la Base de Datos de Acuerdo
      a una Condición que Implica una Union de Varias Entidades através del Lenguaje HQL*/
    public Object findByWhereStatementOneJoinObj(Class clazz, String whereStatement) {
        Object objects = null;
        Object[] second = null;
        List lista = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + whereStatement);
            lista = query.list();
            Iterator it = lista.iterator();
            while (it.hasNext()) {
                second = (Object[]) it.next();
            }
//                objects = second[0];
            for (Object objeto : second) {
                objects = objeto;
                break;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }
    
    /*Metodo Para Encontrar el Ultimo Objeto Ingresado de una Entidad Mapeada de Acuerdo a una 
      Condicion en Especifico.*/
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

    /*Metodo que Permite la Seleccion General de Objetos y/o Propiedades de las Entidades Mapeadas de la Base de Datos*/
    public List findByWhereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery(whereStatement + " order by id");
            objects = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
