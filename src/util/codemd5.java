/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.security.MessageDigest;

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
    
}
