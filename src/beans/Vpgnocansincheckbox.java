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
/*Clase Destinada a la Visualizacion y Seleccion de los Pagos sin Cancelar de Inmuebles y Negocios
  Implementando ademas la interfaz IModelTable para la Generalizacion de la Carga de Datos en los JTable*/
public class Vpgnocansincheckbox implements IModelTable{
    private String PERIODO;
    private BigDecimal MONTO_IMPUESTOS;
    private String ESTADO_DEL_COBRO;

    public Vpgnocansincheckbox() {
    }

    public Vpgnocansincheckbox(String PERIODO, BigDecimal MONTO_IMPUESTOS, String ESTADO_DEL_COBRO) {
        this.PERIODO = PERIODO;
        this.MONTO_IMPUESTOS = MONTO_IMPUESTOS;
        this.ESTADO_DEL_COBRO = ESTADO_DEL_COBRO;
    }

    public String getPERIODO() {
        return PERIODO;
    }

    public void setPERIODO(String PERIODO) {
        this.PERIODO = PERIODO;
    }

    public String getMONTO_IMPUESTOS() {
        return MONTO_IMPUESTOS.setScale(2,RoundingMode.HALF_EVEN).toString();
    }

    public void setMONTO_IMPUESTOS(BigDecimal MONTO_IMPUESTOS) {
        this.MONTO_IMPUESTOS = MONTO_IMPUESTOS.setScale(2,RoundingMode.HALF_EVEN);
    }

    public String getESTADO_DEL_COBRO() {
        return ESTADO_DEL_COBRO;
    }

    public void setESTADO_DEL_COBRO(String ESTADO_DEL_COBRO) {
        this.ESTADO_DEL_COBRO = ESTADO_DEL_COBRO;
    }
    
    @Override
    public Object[] obtainFields() {
        return new Object[]{
            PERIODO,
            MONTO_IMPUESTOS,
            ESTADO_DEL_COBRO
        };
    }    
    
}
