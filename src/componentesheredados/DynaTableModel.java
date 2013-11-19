/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
/*Clase Utilizada en la Generalizacion de la Carga de los JTable
  Es por Medio de Esta clase que se Realiza el Seteado de los Valores
  de los Modelos, asi como se determina si el Contenido de una Celda
  Sera Editable Basandose en el tipo de Valor que esta trae Las Unicas
  Celdas que seran editables seran aquellas que contengan valores de 
  Tipo Boolean es decir que representa JCheckbox, Para los Procesos de Seleccion*/
public class DynaTableModel extends DefaultTableModel {
    
    public DynaTableModel() {
        super();
    }
    
    /*Recuperando los Valores de las cabeceras asi como el contenido a ingresar en el
      Modelo a traves del uso e implementacion de la clase DynaTableBean*/
    public DynaTableModel(final DynaTableBean dynaBean) {
        super(dynaBean.getContent(), dynaBean.getHeaders());
    }    
    
    /*Determinando si la Celda sera Editable o no*/
    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0 && getValueAt(0, col).getClass() == Boolean.class )    
        {
            return true;
        } else {
            return false;
        }
    }
    
    /*Setean el Valor a Asignar en la Columna por cada Recorrido*/
    @Override
    public void setValueAt(Object value, int row, int column) {
        super.setValueAt(value, row, column);
    }    
    
    /*Obteniendo la Clase de la Columna que se Esta Recorriendo*/
    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

}
