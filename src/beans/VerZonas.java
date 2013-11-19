/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import interfaces.IModelTable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author alex
 */
/*Clase Destinada a la Visualizacion de las Zonas Ingresadas en el Sistema
 Ademas Implementa La Interfaz ImodelTable Destinada a la Generalización de la Carga en los Jtable*/
public class VerZonas implements IModelTable{
    private String CODIGO_ZONA;
    private String DESCRIPCIÓN;
    private int NUMERO_DE_INMUEBLES_ACTUALES;

    public VerZonas() {
    }

    public VerZonas(String CODIGO_ZONA, String DESCRIPCIÓN, int NUMERO_DE_INMUEBLES_ACTUALES) {
        this.CODIGO_ZONA = CODIGO_ZONA;
        this.DESCRIPCIÓN = DESCRIPCIÓN;
        this.NUMERO_DE_INMUEBLES_ACTUALES = NUMERO_DE_INMUEBLES_ACTUALES;
    }

    public String getCODIGO_ZONA() {
        return CODIGO_ZONA;
    }

    public void setCODIGO_ZONA(String CODIGO_ZONA) {
        this.CODIGO_ZONA = CODIGO_ZONA;
    }

    public String getDESCRIPCIÓN() {
        return DESCRIPCIÓN;
    }

    public void setDESCRIPCIÓN(String DESCRIPCIÓN) {
        this.DESCRIPCIÓN = DESCRIPCIÓN;
    }

    public int getNUMERO_DE_INMUEBLES_ACTUALES() {
        return NUMERO_DE_INMUEBLES_ACTUALES;
    }

    public void setNUMERO_DE_INMUEBLES_ACTUALES(int NUMERO_DE_INMUEBLES_ACTUALES) {
        this.NUMERO_DE_INMUEBLES_ACTUALES = NUMERO_DE_INMUEBLES_ACTUALES;
    }

    @Override
    public Object[] obtainFields() {
        return new Object[]{
            CODIGO_ZONA,
            DESCRIPCIÓN,
            NUMERO_DE_INMUEBLES_ACTUALES
        };
    }    

}
