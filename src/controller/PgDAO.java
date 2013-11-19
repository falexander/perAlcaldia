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
 Especificos de la Entidad Mapeada de la BD sin necesidad de Recuperar la Instancia Completa*/
public class PgDAO extends AbstractDAO {

    /*Metodo que Permite la Recuperación de la Información Basica de Todos los Cobros Ingresados en la Base de Datos para su
     Posterior Visualización y Seleccion en el sistema a traves de la entidad mapeada*/
    public List fin_all_pagos() {
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

    /*Metodo que permite la recuperación de la informacion basica de los cobros generados en el sistema para su posterior
     visualización y seleccion en el Sistema a traves de la entidad mapeada pero en base a una condicion especifica*/
    public List fin_all_pagos_whereStatement(String whereStatement) {
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

    /*Metodo que permite la recuperación de la informacion basica de los cobros generados en el sistema unica y exclusivamente
     para su visualizacion a traves de la entidad mapeada pero en base a una condicion especifica.*/
    public List fin_pagos_whereStatement(String whereStatement) {
        List objects = null;
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select mespagado as PERIODO, monto as MONTO_IMPUESTOS, CASE when estados_id = 5 THEN 'CANCELADO' WHEN estados_id = 6 THEN 'PENDIENTE DE CANCELACIÓN' ELSE '' END as ESTADO_DEL_COBRO from Pagos where " + whereStatement + " order by id");
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }

    /*Metodo que Permite la Recuperación y definicion de los contribuyentes que se encuentran en Mora o con Pagos Pendientes
     dentro del sistema junto con todos los datos personales de contribuyente y usuario a través de las Entidades Mapeadas
     usando clausulas inner join con HQL*/
    public int GetContribuyentesMora() {
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("select count (c.id)as Total,c.dui\n"
                    + " from Inmuebles i inner join Pagos p\n"
                    + "     on i.id = p.inmuebles_id\n"
                    + "     inner join Contribuyentes c\n"
                    + "     on c.id = i.contribuyentes_id\n"
                    + "     inner join Usuarios u\n"
                    + "     on u.id = c.usuarios_id\n"
                    + " where p.estados_id = 6\n"
                    + " group by c.dui\n");
            List objects = query.list();
            return objects.size();
        } catch (Exception ex) {
            return 0;
        }
    }

    public List GetPagosDetails(int inmuebleId) {
        List objects = null;
        try {
            String sql = "select sum (cast(pd.monto_impuesto as double)), i.nombre \n"
                        +"from Detallepagos as pd inner join pd.impuestos as i   \n"
                        +"inner join pd.pagos as p \n"
                        +"where p.inmuebles = "+ inmuebleId +"\n"
                        +"and p.estadosid = 6 \n"
                        +"group by i.nombre";
            sessionFactory.getCurrentSession().beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            objects = query.list();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objects;
    }
}
