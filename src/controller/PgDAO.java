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
public class PgDAO extends AbstractDAO{
    
    public List fin_all_pagos(){
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select 'false', mespagado, monto, CASE when estados_id = 5 THEN 'CANCELADO' WHEN estados_id = 6 THEN 'PENDIENTE DE CANCELACIÓN' ELSE '' END from Pagos order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
        
        
    }
    
    public List fin_all_pagos_whereStatement(String whereStatement){
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select 'false' as SELECCIONAR, mespagado as PERIODO, monto as MONTO_IMPUESTOS, CASE when estados_id = 5 THEN 'CANCELADO' WHEN estados_id = 6 THEN 'PENDIENTE DE CANCELACIÓN' ELSE '' END as ESTADO_DEL_COBRO from Pagos where " + whereStatement + " order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;        
    }
    
}
