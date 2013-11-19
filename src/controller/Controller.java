/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.IModelTable;
import componentesheredados.DynaTableBean;
import componentesheredados.DynaTableModel;

/**
 *
 * @author alex
 */
/*Clase que Permite la Generalización de la Carga en los JTable en Conjunto con las Clases
  Destinadas Para Ello*/
public class Controller {
    
    /*Metodo Para Identificar tipo de Modelo que será Cargado asi como inicializar los valores para
      Carga de los Datos Respectivos de Acuerdo a la Clase*/
    public DynaTableModel getModel(List<IModelTable> resultset, Class clazz){
        DynaTableBean bn = buildDynaTableBean(clazz, resultset);        
        return new  DynaTableModel(bn);
    }
    
    /*Metodo Para Setear el Contenido del Vector Una Vez han Sido Recuperados los Datos desde la Clase
      Correspondiente*/
    private final Vector getTableContent(final List<IModelTable> resultset) {
        Vector content = new Vector();
        
        Iterator<IModelTable> it = resultset.iterator();
        while(it.hasNext()){
            Vector rowVector = new Vector();
            IModelTable mt = it.next();
            for (Object cellElement : mt.obtainFields()) {
                    rowVector.add(cellElement);
                }
                rowVector.trimToSize();
                content.add(rowVector);
        }
        
        return content;
    }
    
    /*Metodo para Setear el Numero y Nombre de las Columnas de los Diferentes Jtable
      Una Vez Ha Sido Identificada el Tipo de Clase al que Pertenece*/
    public final Vector getTableHeaders(final Class clazz) {
        Vector headers;
        headers = new Vector();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            headers = new Vector();
            for (Field member : fields) {
                headers.add(member.getName());
            }
        }
        headers.trimToSize();
        return headers;
    }    
    
    /*Metodo Que Permite la Inicialización y Seteo de los Valores a Cargar de Acuerdo al tipo y a la lista Construida*/
    public final DynaTableBean buildDynaTableBean(final Class clazz, final List<IModelTable> resultset) {
        Vector headers = getTableHeaders(clazz);
        Vector content = getTableContent(resultset);
        DynaTableBean dynaBean = new DynaTableBean();
        dynaBean.setHeaders(headers);
        dynaBean.setContent(content);
        return dynaBean;
    }    
    
}
