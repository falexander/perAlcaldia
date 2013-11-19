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
public class ForDAO extends AbstractDAO{
  
    /*Metodo que Permite Recobrar Todas las Formulas establecidas en la Base de Datos Mediante la Entidad Mapeada
      Sin Necesidad de Recuperar las Instancias Completas*/
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
    
    /*Metodo que Permite Recuperar Todas las Formulas Establecidas en la Base de Datos Mediante la Entidad Mapeada
      Bansandose en una Condición Especifica y a su vez No Hay Necesidad de Recuperar las Instancias Completas*/
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
