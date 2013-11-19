/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import peralcaldia.model.Usuarios;

/**
 *
 * @author alex
 */
/*Clase utilizada para el inicio de sesion de los usuarios del sistema*/
public class LoginRepository 
{
    AbstractDAO dao = new AbstractDAO();
    public boolean Login(String user, String password)
    {
        Usuarios u = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "upper(alias) = '"+ user.toUpperCase() +"' and upper(password) = '"+ codemd5.getmd5(password).toUpperCase()+"' and roles_id not in (1,3)");
        if(u!=null)
        {
            String text = String.valueOf(u.getId());
            try {
                File file = new File("userLog.txt");		            
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(text);
                output.close();
            } catch ( Exception e ) {
               e.printStackTrace();
            }
            return true;
        }        
        return false;
    }
}
