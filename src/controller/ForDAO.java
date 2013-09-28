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
public class ForDAO extends AbstractDAO{
    
    public List find_all_formulas(){
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select formula from Formulas order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }                        
        return objects;
    }
    
    public List find_formulas_whereStatement(String whereStatement){
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select formula from Formulas where "  +  whereStatement + "order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }                        
        return objects;
    }        
    
}
