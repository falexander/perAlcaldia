/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class DynaTableModel extends DefaultTableModel {
    
    public DynaTableModel() {
        super();
    }
    
    public DynaTableModel(final DynaTableBean dynaBean) {
        super(dynaBean.getContent(), dynaBean.getHeaders());
    }    
    
    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0)    
        {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        super.setValueAt(value, row, column);
    }    

}
