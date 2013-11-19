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
/*Clase Destinada a la Visualización de los Impuestos Asignados a Cada Inmueble, En la pantalla de Asignacion y Remocion de Impuestos por Inmuebles
 Ademas Implementa La Interfaz ImodelTable Destinada a la Generalización de la Carga en los Jtable*/
public class VerImpuestosPorInmuebles  implements IModelTable{
    private String IMPUESTOS_ASIGNADOS_AL_INMUEBLE;

    public VerImpuestosPorInmuebles() {
    }

    public VerImpuestosPorInmuebles(String IMPUESTOS_ASIGNADOS_AL_INMUEBLE) {
        this.IMPUESTOS_ASIGNADOS_AL_INMUEBLE = IMPUESTOS_ASIGNADOS_AL_INMUEBLE;
    }

    public String getIMPUESTOS_ASIGNADOS_AL_INMUEBLE() {
        return IMPUESTOS_ASIGNADOS_AL_INMUEBLE;
    }

    public void setIMPUESTOS_ASIGNADOS_AL_INMUEBLE(String IMPUESTOS_ASIGNADOS_AL_INMUEBLE) {
        this.IMPUESTOS_ASIGNADOS_AL_INMUEBLE = IMPUESTOS_ASIGNADOS_AL_INMUEBLE;
    }
    
    @Override
    public Object[] obtainFields() {
        return new Object[]{
            IMPUESTOS_ASIGNADOS_AL_INMUEBLE,
        };
    }    
    
}
