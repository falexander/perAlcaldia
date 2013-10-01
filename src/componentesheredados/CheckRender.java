/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author alex
 */
public class CheckRender extends JCheckBox implements TableCellRenderer {
    
    private JComponent component = new JCheckBox();

    /** Constructor de clase */
    public CheckRender() {
        setOpaque(true);
    }
   
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Color de fondo de la celda
        ((JCheckBox) component).setBackground(new Color(0, 200, 0));
        //obtiene valor boolean y coloca valor en el JCheckBo
        boolean b = ((Boolean) value).booleanValue();
        ((JCheckBox) component).setSelected(b);
        ((JCheckBox) component).setHorizontalAlignment(SwingConstants.CENTER);
        if ((Boolean)value) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        return ((JCheckBox) component);
    }
    
}
