/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import interfaces.IModelTable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class GCMPNativos {
    
    public DefaultComboBoxModel llenarmodelo(DefaultComboBoxModel modelo, List objects){
        DefaultComboBoxModel mimodelo = modelo;        
        Iterator it = objects.iterator();
        limpiarmodelo(mimodelo);
        mimodelo.addElement("-");
        while (it.hasNext()) {
            Object object = it.next();
            mimodelo.addElement(object);
        }        
        return mimodelo;
    }
    
    public DefaultComboBoxModel limpiarmodelo (DefaultComboBoxModel modelo){
        DefaultComboBoxModel mimodelo = modelo;        
        if (mimodelo.getSize() > 0) {
            for (int i = mimodelo.getSize() - 1; i >= 0; i--) {
                mimodelo.removeElementAt(i);
            }
        }
        return mimodelo;
    }
    
    public JTable centrarcabeceras(JTable tabla){
        JTable mitabla = tabla;
        DefaultTableCellRenderer alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        mitabla.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);
        
        return mitabla;
    }
    
    public DefaultTableModel limpiartabla(DefaultTableModel modelo){
        DefaultTableModel mimodelo = modelo;
        try {
            for (int i = mimodelo.getRowCount()-1; i >= 0; i--)
            {
            mimodelo.removeRow(i);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return mimodelo;
    }
    
    public DefaultTableModel llenartabla(DefaultTableModel modelo, List objects, Class clazz){
        DefaultTableModel mimodelo = modelo;
        Controller ctr = new Controller();
        mimodelo = ctr.getModel(objects,clazz);
        return mimodelo;
    }
    
}
