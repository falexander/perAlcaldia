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
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j=0; j<b.length; j++) {
           buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
           buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
     }
    
}
