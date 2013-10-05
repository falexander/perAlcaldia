/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import javax.swing.JFormattedTextField;

/**
 *
 * @author alex
 */
public class PhoneJTextField extends JFormattedTextField {

    public PhoneJTextField() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(final java.awt.event.KeyEvent evt) {
                JFormattedTextField tf = (JFormattedTextField) evt.getComponent();
                final char caracter = evt.getKeyChar();
                //comprobando que sea el largo adecuado
                if (tf.getText().length() >= 10) {
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
    }
}
