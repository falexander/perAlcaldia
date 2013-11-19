/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import peralcaldia.model.Usuarios;

/**
 *
 * @author alex
 */
/*Clase utilizada como repositorio general.*/
public class  GenericRepository {
    AbstractDAO dao = new AbstractDAO();
    
    public int GetUserId()
    {
        try {
                BufferedReader br = new BufferedReader(new FileReader("userLog.txt"));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                br.close();
                int id = Integer.parseInt(line);
                return id;
            } catch (Exception ex) {

                return 0;
            }
    }
    /*Obteniendo el rol del usuario logueado*/
    public int GetRole()
    {         
        try {
            BufferedReader br = new BufferedReader(new FileReader("userLog.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            br.close();
            int id = Integer.parseInt(line);            
            Usuarios u = new Usuarios();
            u = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "id = "+ id);
            return u.getRoles().getId();
        } catch (Exception ex) {
            
            return 0;
        }
    }
    
    public int ChangePassword(String oldPassword, String newPassword)
    {
        int userId = this.GetUserId();
        Usuarios u = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "id = "+ userId);
        if(u.getPassword().toUpperCase().equals(codemd5.getmd5(oldPassword).toUpperCase()))
        {
            u.setPassword(codemd5.getmd5(newPassword));
            dao.save(u);
            return 1;
        }
        else{
            return 0;
        }
    }    
    /*Conexion utilizada para los reportes*/
    public Connection GetConnection()
    {
        Connection conn=null;
        try
        {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bdAlcaldia","postgres","password");
            return conn;
        }       
        catch(Exception ex)
        {
            return null;
        }
        
    }
}
