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
/*Clase Destinada a la Visualización de los Impuestos Ingresados en el Sistema así como sus respectivas propiedades, monto y formula actual, 
  Además Implementa La Interfaz ImodelTable Destinada a la Generalización de la Carga en los Jtable*/
public class VerImpuestos implements IModelTable{
    private String  NOMBRE_IMPUESTO;
    private BigDecimal MONTO_ACTUAL;
    private String FORMULA_ACTUAL;

    public VerImpuestos() {
    }

    public VerImpuestos(String NOMBRE_IMPUESTO, BigDecimal MONTO_ACTUAL, String FORMULA_ACTUAL) {
        this.NOMBRE_IMPUESTO = NOMBRE_IMPUESTO;
        this.MONTO_ACTUAL = MONTO_ACTUAL;
        this.FORMULA_ACTUAL = FORMULA_ACTUAL;
    }

    public String getNOMBRE_IMPUESTO() {
        return NOMBRE_IMPUESTO;
    }

    public void setNOMBRE_IMPUESTO(String NOMBRE_IMPUESTO) {
        this.NOMBRE_IMPUESTO = NOMBRE_IMPUESTO;
    }

    public String getMONTO_ACTUAL() {
        return MONTO_ACTUAL.setScale(2,RoundingMode.HALF_EVEN).toString();
    }

    public void setMONTO_ACTUAL(BigDecimal MONTO_ACTUAL) {
        this.MONTO_ACTUAL = MONTO_ACTUAL;
    }

    public String getFORMULA() {
        return FORMULA_ACTUAL;
    }

    public void setFORMULA(String FORMULA_ACTUAL) {
        this.FORMULA_ACTUAL = FORMULA_ACTUAL;
    }
    
    @Override
    public Object[] obtainFields() {
        return new Object[]{
            NOMBRE_IMPUESTO,
            MONTO_ACTUAL,
            FORMULA_ACTUAL,
        };
    }    
    
    
}
