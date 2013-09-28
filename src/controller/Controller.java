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
import util.DynaTableBean;
import util.DynaTableModel;

/**
 *
 * @author alex
 */
public class Controller {
    
    public DynaTableModel getModel(List<IModelTable> resultset, Class clazz){
        DynaTableBean bn = buildDynaTableBean(clazz, resultset);        
        return new  DynaTableModel(bn);
    }
    
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
    
    public final DynaTableBean buildDynaTableBean(final Class clazz, final List<IModelTable> resultset) {
        Vector headers = getTableHeaders(clazz);
        Vector content = getTableContent(resultset);
        DynaTableBean dynaBean = new DynaTableBean();
        dynaBean.setHeaders(headers);
        dynaBean.setContent(content);
        return dynaBean;
    }    
    
}
