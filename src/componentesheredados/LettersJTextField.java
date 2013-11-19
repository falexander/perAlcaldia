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
/*Componente Personalizado Dedicado Exclusivamente al Ingreso de Caracteres de Tipo
  Letras y/o Pertenecientes al Alfabeto excluyendo numeros y caracteres especiales*/
public class LettersJTextField extends JTextField {

    public LettersJTextField() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JTextField tf = (JTextField) evt.getComponent();
                char caracter = evt.getKeyChar();
                if (Character.isLetter(caracter) || Character.isISOControl(caracter) || caracter == 32) {

                    evt = evt;

                } else {
                    evt.consume();

                }
            }
        });
    }
}
