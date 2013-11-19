/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.io.Serializable;
import java.util.Vector;



/**
 *
 * @author alex
 */
/*Clase Utilizada en la Generalizacion de la Carga de las Tablas (JTable)
  Es Por Medio de Esta Clase que se Obtienen los valores de las Cabeceras
  y el Contenido a Ingresar en los respectivos Modelos para los JTable*/
public class DynaTableBean implements Serializable {
    private Vector headers;
    private Vector content;

    public DynaTableBean() {
    }

    public Vector getHeaders() {
        return headers;
    }

    public void setHeaders(Vector headers) {
        this.headers = headers;
    }

    public Vector getContent() {
        return content;
    }

    public void setContent(Vector content) {
        this.content = content;
    }
}
