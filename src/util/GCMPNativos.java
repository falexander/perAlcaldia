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
/*Clase utilizada para la generalizacion de componentes nativos tales como Jtables, y Jcombobox*/
public class GCMPNativos {
    
    /*Metodo para llenar los modelos correspondientes a los Jcombobox*/
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
    
    /*Metodo para limpiar los modelos correspondientes a los JCombobox, sin dejar ningun item en el modelo*/
    public DefaultComboBoxModel limpiarmodelo (DefaultComboBoxModel modelo){
        DefaultComboBoxModel mimodelo = modelo;        
        if (mimodelo.getSize() > 0) {
            for (int i = mimodelo.getSize() - 1; i >= 0; i--) {
                mimodelo.removeElementAt(i);
            }
        }
        return mimodelo;
    }
    
    /*Metodo para limpiar los modelos correspondientes a los JCombobox, dejando el item de indice cero*/
    public DefaultComboBoxModel limpiarmodelo2(DefaultComboBoxModel modelo) {
        DefaultComboBoxModel mimodelo = modelo;
        if (mimodelo.getSize() > 0) {
            for (int i = mimodelo.getSize() - 1; i > 0; i--) {
                mimodelo.removeElementAt(i);
            }
        }
        return mimodelo;
    }
    
    /*Metodo para centrar las cabeceras o nombres de las columnas de un Jtable*/
    public JTable centrarcabeceras(JTable tabla){
        JTable mitabla = tabla;
        DefaultTableCellRenderer alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        mitabla.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);
        return mitabla;
    }
    
    /*Metodo para centrar las columnas de un Jtable segun las indicaciones durante la Invocacion*/
    public JTable centrarcolumnas(JTable tabla, int ncolumnas){
        JTable mitabla = tabla;
        DefaultTableCellRenderer alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < ncolumnas; i++) {
            mitabla.getColumnModel().getColumn(i).setCellRenderer(alinearCentro);
        }                        
        return mitabla;        
    }
    
    /*Metodo para Centrar las Cabeceras de un Jtable dejando libre la primer columna*/
    public JTable centrarcolumnasdesde2(JTable tabla, int ncolumnas) {
        JTable mitabla = tabla;
        DefaultTableCellRenderer alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < ncolumnas; i++) {
            mitabla.getColumnModel().getColumn(i).setCellRenderer(alinearCentro);
        }
        return mitabla;
    }
    
    /*Metodo utilizado para limpiar los modelos correspondiente a los JTableModel*/
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
