/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.Vector;



/**
 *
 * @author alex
 */
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
