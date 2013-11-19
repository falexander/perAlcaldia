/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import interfaces.IModelTable;

/**
 *
 * @author alex
 */
/*Clase Destinada Para la Visualización de los Diferentes Giros Ingresados en el Sistema, Implementando La Interfaz ImodelTable 
 Destinada a la Generalización de la Carga en los Jtable*/
public class VerGiros implements IModelTable{
    private String NOMBRE_GIRO;
    private String ESTADO;

    public VerGiros() {
    }

    public VerGiros(String NOMBRE_GIRO, String ESTADO) {
        this.NOMBRE_GIRO = NOMBRE_GIRO;
        this.ESTADO = ESTADO;
    }

    public String getNOMBRE_GIRO() {
        return NOMBRE_GIRO;
    }

    public void setNOMBRE_GIRO(String NOMBRE_GIRO) {
        this.NOMBRE_GIRO = NOMBRE_GIRO;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }
    
    @Override
    public Object[] obtainFields() {
        return new Object[]{
            NOMBRE_GIRO,
            ESTADO
        };
    }    
}
