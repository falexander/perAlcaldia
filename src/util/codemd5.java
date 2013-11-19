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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author alex
 */
/*Clase utilizada como repositorio de utilidades varias utilizadas con frecuencia en el aplicativo.*/
public class codemd5 {

    /*Coleccion Hexadecimal*/
    private static final char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /*Generador de un codigo hash-md5 para el guardado de la contraseña en la base de datos*/
    public static String getmd5(String record) {
        String outputString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(record.getBytes());
            byte[] output = md.digest();
            outputString = bytesToHex(output);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return outputString;
    }

    /*Transformando una coleccion de bytes a hexadecimal*/
    public static String bytesToHex(byte[] b) {
        try {
            StringBuilder strbCadenaMD5 = new StringBuilder(2 * b.length);
            for (int i = 0; i < b.length; i++) {
                int bajo = (int) (b[i] & 0x0f);
                int alto = (int) ((b[i] & 0xf0) >> 4);
                strbCadenaMD5.append(hexDigit[alto]);
                strbCadenaMD5.append(hexDigit[bajo]);
            }
            return strbCadenaMD5.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    /*Comprobacion si un string representa un numero*/
    public static boolean esnumero(String Cadena) {
        try {
            Double.parseDouble(Cadena);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*Convirtiendo un string a fecha con periodo de cero horas*/
    public static Date formatearfechazerohoras(String fecha) {
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
            System.out.println("Error en el formateo de la fecha");
            System.out.println(e.toString());
        }
        return nuevafecha;
    }

    /*Convirtiendo un string a fecha con periodo maximo de horas*/
    public static Date formatearfechaultimahora(String fecha) {
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
            System.out.println("Error en el formateo de la fecha");
            System.out.println(e.toString());
        }
        return nuevafecha;
    }

    /*Formateando una fecha al inicio del dia*/
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
            System.out.println("Error en el formateo de la fecha");
            System.out.println(e.toString());
        }
        return nuevafecha;
    }

    /*formateando una fecha al final del dia (hora maxima)*/
    public static Date formatearfechaultimahora(Date fecha) {
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(Calendar.HOUR_OF_DAY, 23);
            c1.set(Calendar.SECOND, 59);
            c1.set(Calendar.MILLISECOND, 59);
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            System.out.println("Error en el formateo de la fecha");
            System.out.println(e.toString());
        }
        return nuevafecha;
    }

    /*Comparando un rango de fechas*/
    public static boolean compararangofechas(Date fechainiprrango, Date fechafinprrango, Date fechainiscrango, Date fechafinscrango) {
        if ((fechainiprrango.compareTo(fechainiscrango) > 0 || fechainiprrango.compareTo(fechainiscrango) == 0) && (fechafinprrango.compareTo(fechafinscrango) < 0 || fechafinprrango.compareTo(fechafinscrango) == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /*Obteniendo el anio de un periodo en especifico*/
    public static int getanioregistro(Date fecharegistro) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        int anio;
        anio = Integer.parseInt(dateFormat.format(fecharegistro));
        return anio;
    }

    /*Obteniendo el mes de un periodo en especifico*/
    public static int getmesregistro(Date fecharegistro) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        int mes;
        mes = Integer.parseInt(dateFormat.format(fecharegistro));
        return mes;
    }

    /*Recuperando Anio Actual*/
    public static int getanioactual() {
        int anio;
        Calendar c1 = new GregorianCalendar();
        c1 = Calendar.getInstance();
        anio = c1.get(Calendar.YEAR);
        return anio;
    }

    /*Formateando un periodo al ultimo dia de un mes*/
    public static Date obteniendoultimodiames(Date fecha) {
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(c1.get(c1.YEAR), c1.get(c1.MONTH), c1.getActualMaximum(c1.DAY_OF_MONTH));
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            System.out.println("Error en el formateo de la fecha");
            System.out.println(e.toString());
        }
        return nuevafecha;
    }

    /*Formateando un periodo al primer dia de un mes*/
    public static Date obteniendoprimerdiames(Date fecha) {
        Date nuevafecha = fecha;
        Calendar c1 = new GregorianCalendar();
        try {
            c1.setTime(nuevafecha);
            c1.set(c1.get(c1.YEAR), c1.get(c1.MONTH), c1.getActualMinimum(c1.DAY_OF_MONTH));
            nuevafecha = c1.getTime();
        } catch (Exception e) {
            System.out.println("Error en el formateo de la fecha");
            System.out.println(e.toString());
        }
        return nuevafecha;
    }

    /*metodo para evaluar una formula introducida*/
    public static Boolean evaluarformula(String formula) {
        //Variables para interpretar formulas
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");
        Object resultado = new Object();
        //Intepretando la formula de acuerdo al monto dado
        interprete.put("ML", 5);
        interprete.put("MC", 4);
        interprete.put("MONTO", 3);
        interprete.put("NM", 2);
        try {
            resultado = interprete.eval(formula);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    /*Obteniendo un periodo mes - anio de una fecha en especifico*/
    public static String getmesanio(Date fechafor) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");
        String fecha;
        fecha = dateFormat.format(fechafor) + "-" + dateFormat1.format(fechafor);
        return fecha;
    }
    
    /*Obteniendo el mes anterior a una fecha en especifico obteniendo el resultado en formato mes - anio*/
    public static String GetMesAnteriorAnio(Date fechafor) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");
        String fecha;
        int mes;
        mes = Integer.parseInt(dateFormat.format(fechafor)) - 1;
        fecha = Integer.toString(mes)+ "-" + dateFormat1.format(fechafor);
        return fecha;        
    }
    
    /*Obteniendo la fecha con un mes anterior a la fecha proporcionada*/
    public static Date RestarUnMesAFecha(Date fechafor) {
        Calendar c1 = new GregorianCalendar();
        Date fecha;
        //Iniciando la Variable al valor de la fecha proveida.
        c1.setTime(fechafor);
        c1.add(Calendar.MONTH, -1);
        fecha = c1.getTime();
        return fecha;        
    } 
    
    /*Obteniendo una fecha en formato dd-mm-yyyy a partir de una fecha proporcionada.*/
    public static String GetFechaSinHora(Date fecha) {
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(fecha);
    }
    
    
}
