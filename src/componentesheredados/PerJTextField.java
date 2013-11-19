/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFormattedTextField;

/**
 *
 * @author alex
 */
/*Componente Personalizado, Destinado al Ingreso de Periodos de Tiempo por Parte
  del Usuario, Refiriendonos Unica y Exclusivamente a Periodos en el Formato mes-anio*/
public class PerJTextField extends JFormattedTextField{
    
    public PerJTextField(){
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(final java.awt.event.KeyEvent evt) {
                JFormattedTextField tf = (JFormattedTextField) evt.getComponent();
                final char caracter = evt.getKeyChar();
                //comprobando que sea el largo adecuado
                if (tf.getText().length() >= 8) {
                    evt.consume();
                }
                if (((caracter < '0') || (caracter > '9')) && (caracter != 127 /* * DELETE/BACK_SPACE */)) {
                    evt.consume();
                }
            }
        });
        
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                JFormattedTextField tf = (JFormattedTextField) e.getComponent();
                if (tf.getText().length() > 0) {
                    if (tf.getText().length() == 7) {
                        tf.transferFocus();
                    } else {
                        tf.requestFocus();
                    }
                }

            }
        });       
    }
    
}
