/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

/**
 *
 * @author alex
 */
/*Componente Personalizado, Destinado Unica y Exclusivamente al Ingreso de Valores Numericos de Tipo
  Entero que son Necesarios en el Desarrollo de varias pantallas dentro del aplicativo*/
public class NumberJTextField extends JTextField{
    public NumberJTextField() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(final java.awt.event.KeyEvent evt) {
                JTextField tf = (JTextField) evt.getComponent();
                final char caracter = evt.getKeyChar();
                //comprobando que sea el largo adecuado
                if (tf.getText().length() >= 25) {
                    evt.consume();
                }
                if (((caracter < '0') || (caracter > '9')) && (caracter != 127 /* * DELETE/BACK_SPACE */)) {
                    evt.consume();
                }
            }
        });
    }
}
