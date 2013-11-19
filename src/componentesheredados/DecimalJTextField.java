/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import javax.swing.JTextField;

/**
 *
 * @author alex
 */
/*Clase Destinada a la Personalizacion de JTextField Permitiendo ingresar unicamente valores de Tipo Numerico (Decimales)*/
public class DecimalJTextField extends JTextField {

    public DecimalJTextField() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTextField tf = (JTextField) evt.getComponent();
                char caracter = evt.getKeyChar();
                if ((caracter == 46 /* DECIMAL DOT '.' */) && tf.getText().contains(".")) {
                    evt.consume();
                }
                if (((caracter < '0') || (caracter > '9')) && (caracter != 46 /* DECIMAL DOT '.' */) && (caracter != 127 /*DELETE/BACK_SPACE*/)) {
                    evt.consume();
                }
            }
        });
    }
}
