/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentesheredados;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author alex
 */
/*Componente Personalizado, Diseñado para el ingreso de Correos Electronicos
  Este Componente se encarga de realizar la verificacion del texto ingresado
  y Una vez se empiece la digitacion no permitira que se pierda el foco hasta
  que el valor ingresado tenga un formato de Email Valido.*/
public class MailTextField extends JTextField {

    public MailTextField() {
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }
            /*Verificando que el Valor Introducido tenga un Formato Valido*/
            @Override
            public void focusLost(FocusEvent e) {
                JTextField tf = (JTextField) e.getComponent();
                if (tf.getText().length() > 0) {
                    Boolean val;
                    val = checkEmail(tf.getText());
                    if (val) {
                        tf.transferFocus();
                    } else {
                        tf.requestFocus();
//                        JOptionPane.showMessageDialog(null, "Email no valido");
                    }
                }
            }
        });
    }
    /*Funcion Desarrollada con el Objetivo de Comprobar que el valor de los Correos
      Electronicos Ingresados Tengan el Formato Correcto, Para Efectuar de Manera 
      Clara y Comprensible la Comprobacion de estos se Utilizan las Denominadas
      Expresiones Regulares.*/
    public static boolean checkEmail(String email) {
        // Establecer el patron
        Pattern p = Pattern.compile("[-\\w\\.]+@\\w+\\.\\w+");
        // Asociar el string al patron
        Matcher m = p.matcher(email);
        // Comprobar si encaja
        if (!m.matches()) {
            Pattern pp = Pattern.compile("[-\\w\\.]+@\\w+\\.\\w+\\w+\\.\\w+");
            // Asociar el string al patron
            Matcher mm = pp.matcher(email);
            return mm.matches();
        }
        // Comprobar si encaja
        return m.matches();
    }
}
