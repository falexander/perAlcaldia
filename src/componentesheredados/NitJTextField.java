/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

/**
 *
 * @author alex
 */
/*Componente Personalizado, Destinado al Ingreso y Verificacion del Formato
  Para los NIT de tipo Natural y Juridico para las pantallas en las que este
  sea necesario*/
public class NitJTextField extends JFormattedTextField {

    public NitJTextField() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(final java.awt.event.KeyEvent evt) {
                JFormattedTextField tf = (JFormattedTextField) evt.getComponent();
                final char caracter = evt.getKeyChar();
                //comprobando que sea el largo adecuado
                if (tf.getText().length() >= 18) {
                    evt.consume();
                }
                if (((caracter < '0') || (caracter > '9')) && (caracter != 127 /* * DELETE/BACK_SPACE */)) {
                    evt.consume();
                }

                if (caracter == 13) {
                    tf.transferFocus();
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
                    if (tf.getText().length() == 17) {
                        tf.transferFocus();
                    } else {
                        tf.requestFocus();
                    }
                }

            }
        });        
    }
}
