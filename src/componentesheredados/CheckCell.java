/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author alex
 */
public class CheckCell extends DefaultCellEditor implements TableCellRenderer, TableCellEditor, ItemListener{
    
    private JComponent component = new JCheckBox();    
    private boolean value = false;
    private CheckRender vr = new CheckRender();

    public CheckCell() {
        super(new JCheckBox());        
        vr.addItemListener(this);
    }
    
     /** retorna valor de celda */
    @Override
    public Object getCellEditorValue() {
        return ((JCheckBox)component).isSelected();        
    }

    /** Segun el valor de la celda selecciona/deseleciona el JCheckBox */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Color de fondo en modo edicion
        ( (JCheckBox) component).setBackground( new Color(200,200,0) );
        //obtiene valor de celda y coloca en el JCheckBox
        boolean b = ((Boolean) value).booleanValue();
        if (b) {
            table.setValueAt(Boolean.FALSE, row, column);
        }
        else{
            table.setValueAt(Boolean.TRUE, row, column);
        }
        ( (JCheckBox) component).setSelected( b );
        ( (JCheckBox) component).setHorizontalAlignment(SwingConstants.CENTER); 
        return ( (JCheckBox) component);     
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        this.fireEditingStopped();
    }    

    /** cuando termina la manipulacion de la celda */
    @Override
    public boolean stopCellEditing() {        
        value = ((Boolean)getCellEditorValue()).booleanValue() ;
        ((JCheckBox)component).setSelected( value );
        return super.stopCellEditing();
    }
    
      /** retorna componente */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
            return null;         
         return ( (JCheckBox) component );
    }
       
}
