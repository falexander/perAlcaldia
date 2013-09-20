/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author alex
 */
public class codemd5 {
    private static final char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
       public static String getmd5(String record) {
//    	String input = "";
    	String outputString="";
    	try{
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		md.update(record.getBytes()); 
        	byte[] output = md.digest();
        	outputString=bytesToHex(output);
    	}catch(Exception e){
    		
    	}
    	return outputString;
    }
    
    public static String bytesToHex(byte[] b) {
//        StringBuffer buf = new StringBuffer();
//        for (int j=0; j<b.length; j++) {
//           buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
//           buf.append(hexDigit[b[j] & 0x0f]);
//        }
        try {
            StringBuilder strbCadenaMD5 = new StringBuilder(2 * b.length);
            for (int i = 0; i < b.length; i++){
                int bajo = (int)(b[i] & 0x0f);
                int alto = (int)((b[i] & 0xf0) >> 4);
                strbCadenaMD5.append(hexDigit[alto]);
                strbCadenaMD5.append(hexDigit[bajo]);
            }
            return strbCadenaMD5.toString();            
        } catch (Exception e) {
            return null;
        }
//        return buf.toString();
     }
 
    public static boolean esnumero(String Cadena){
        try {
            Double.parseDouble(Cadena);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Date formatearfechazerohoras(String fecha){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM-yyyy");
        Date nuevafecha = new Date();
        Calendar c1 = new GregorianCalendar();
        try {
            nuevafecha = formatoDelTexto.parse(fecha);
            c1.setTime(nuevafecha);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en el formateo de la fecha");
        }
        return nuevafecha;
    }
    
    public static Date formatearfechaultimahora(String fecha){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM-yyyy");
        Date nuevafecha = new Date();
        Calendar c1 = new GregorianCalendar();
        try {
            nuevafecha = formatoDelTexto.parse(fecha);
            c1.setTime(nuevafecha);
            c1.set(Calendar.HOUR_OF_DAY, 23);
            c1.set(Calendar.SECOND, 59);
            c1.set(Calendar.MILLISECOND, 59);
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en el formateo de la fecha");
        }
        return nuevafecha;        
    }
    
    public static Date formatearfechazerohoras(Date fecha) {
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en el formateo de la fecha");
        }
        return nuevafecha;
    }
    
    public static Date formatearfechaultimahora(Date fecha){
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(Calendar.HOUR_OF_DAY, 23);
            c1.set(Calendar.SECOND, 59);
            c1.set(Calendar.MILLISECOND, 59);
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en el formateo de la fecha");
        }
        return nuevafecha;        
    }
    
    public static boolean compararangofechas(Date fechainiprrango, Date fechafinprrango, Date fechainiscrango, Date fechafinscrango){
        if ((fechainiprrango.compareTo(fechainiscrango) > 0 || fechainiprrango.compareTo(fechainiscrango) == 0) && (fechafinprrango.compareTo(fechafinscrango) < 0 || fechafinprrango.compareTo(fechafinscrango) == 0)) {
            return true;
        }
        else{
            return false;
        }                
    }
    
    public static int getanioregistro(Date fecharegistro){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        int anio;
        anio = Integer.parseInt(dateFormat.format(fecharegistro));                
        return anio;
    }
    
    public static int getmesregistro(Date fecharegistro){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        int mes;
        mes = Integer.parseInt(dateFormat.format(fecharegistro));                
        return mes;        
    }
    
    public static int getanioactual(){
    int anio;
    Calendar c1 = new GregorianCalendar();
    c1 = Calendar.getInstance();
    anio = c1.get(Calendar.YEAR);    
    return anio;    
    }
    
    public static Date obteniendoultimodiames(Date fecha){
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(c1.get(c1.YEAR), c1.get(c1.MONTH), c1.getActualMaximum(c1.DAY_OF_MONTH));
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en el formateo de la fecha");
        }
        return nuevafecha;        
    }
    
    public static Date obteniendoprimerdiames(Date fecha){
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(c1.get(c1.YEAR), c1.get(c1.MONTH), c1.getActualMinimum(c1.DAY_OF_MONTH));
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en el formateo de la fecha");
        }
        return nuevafecha;        
    }
}
