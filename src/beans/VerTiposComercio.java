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
/*Clase Destinada a la Visualización de los Distintos Tipos de Comercio Agregados en el Sistema,
 Ademas Implementa La Interfaz ImodelTable Destinada a la Generalización de la Carga en los Jtable*/
public class VerTiposComercio implements IModelTable{
    private String ACTIVIDAD_ECONOMICA;
    private String GIRO;
    private String ESTADO;

    public VerTiposComercio() {
    }

    public VerTiposComercio(String ACTIVIDAD_ECONOMICA, String GIRO, String ESTADO) {
        this.ACTIVIDAD_ECONOMICA = ACTIVIDAD_ECONOMICA;
        this.GIRO = GIRO;
        this.ESTADO = ESTADO;
    }

    public String getACTIVIDAD_ECONOMICA() {
        return ACTIVIDAD_ECONOMICA;
    }

    public void setACTIVIDAD_ECONOMICA(String ACTIVIDAD_ECONOMICA) {
        this.ACTIVIDAD_ECONOMICA = ACTIVIDAD_ECONOMICA;
    }

    public String getGIRO() {
        return GIRO;
    }

    public void setGIRO(String GIRO) {
        this.GIRO = GIRO;
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
            ACTIVIDAD_ECONOMICA,
            GIRO,
            ESTADO
        };
    }    
    
}
