/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Impuestosinmuebles;
import peralcaldia.model.Formulas;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Montosimpuestos;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;

/**
 *
 * @author alex
 */
public class cobrosiniciales {
    AbstractDAO dao = new AbstractDAO();    
    
    public void cargainicialinmuebles(){
        //Inicializando variables para el calculo e inserción de los pagos/cobros.
        Estados est = new Estados();
        Contribuyentes contribuyente = new Contribuyentes();
        List<Inmuebles> listainmuebles = null;
        Inmuebles inmueble = new Inmuebles();        
        Set<Impuestosinmuebles> listaimpuestosinmuebles = null;
        Impuestosinmuebles impinmueble = new Impuestosinmuebles();
        Impuestos impuesto = new Impuestos();
        List<Pagos> listapagos = null;
        Pagos pago = new Pagos();
        Set<Formulas> listaformulas = null;
        Formulas xformula = new Formulas();
        Set<Montosimpuestos> listamontos = null;
        Montosimpuestos tmpmontoim = new Montosimpuestos();
        totalesimpuestosinmuebles adding = new totalesimpuestosinmuebles();
        List<totalesimpuestosinmuebles> listatotalesimp = new ArrayList<totalesimpuestosinmuebles>();
        //Variables fechas formula para obtener el monto a aplicar durante el periodo de validez de la formula.
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM-yyyy");
        Date finifor = new Date();
        Date ffinfor = new Date();
//        Date finimonto = new Date();
//        Date ffinmonto = new Date();
        Calendar c1 = new GregorianCalendar();
        Calendar c2 = new GregorianCalendar();
//        Calendar c3 = new GregorianCalendar();
//        Calendar c4 = new GregorianCalendar();
        //Variables para interpretar formulas
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");        
        String formula;
        Object resultado = new Object();        
        
        //verificando si la carga inicial no ha sido generada anteriormente.
        try {
            listapagos = dao.findByWhereStatement(Pagos.class, "id in (1,2,3,4,5)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un Error en la comprobación inicial");
            e.printStackTrace();
        }
        //recuperando todos los inmuebles que se encuentran activos
        if (listapagos.isEmpty()) {
            try {                
                listainmuebles = dao.findByWhereStatement(Inmuebles.class, "estados_id = 1");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Hubo un error en la carga de inmuebles");
            }
            //Si la lista de inmuebles no esta vacía procedemos a sacar y procesar cada inmueble con sus respectivos impuestos.
            if (!listainmuebles.isEmpty()) {
                Iterator it = listainmuebles.iterator();
                while (it.hasNext()) {
                    //Recuperando Inmueble y lista de impuestos asignados
                    inmueble = (Inmuebles) it.next();
                    listaimpuestosinmuebles = inmueble.getImpuestosinmuebleses();
                    //Si la lista de impuestos no esta vacía iteramos la lista de impuestos para procesar cada impuesto.
                    if(!listaimpuestosinmuebles.isEmpty()){
                        //Iterando lista de impuestos inmuebles
                        Iterator iteradorimpuestosinmuebles = listaimpuestosinmuebles.iterator();                        
                        while (iteradorimpuestosinmuebles.hasNext()) {
                            //Recuperando cada impuestoinmueble y cada impuesto asociado al inmueble
                            impinmueble = (Impuestosinmuebles) iteradorimpuestosinmuebles.next();
                            impuesto = impinmueble.getImpuestos();
                            //Recuperando lista de formulas y montos asociadas al impuesto
                            listaformulas = impuesto.getFormulaes();
                            listamontos = impuesto.getMontosimpuestoses();
                            //Iterando las formulas y calculando el monto por perido.
                            Iterator formit = listaformulas.iterator();
                            while (formit.hasNext()) {
                                xformula = (Formulas) formit.next();                                                                
                                //Formula a Ejecutar
                                formula = xformula.getFormula();
                                //Comprobando si el impuesto tiene o no formula a interpretar
                                if (formula.equals("0")) {
                                    Iterator itmntimpuestos = listamontos.iterator();
                                    while (itmntimpuestos.hasNext()) {
                                        tmpmontoim = (Montosimpuestos) itmntimpuestos.next();
                                        try {
                                            finifor = formatoDelTexto.parse(xformula.getFechainicio());
                                            ffinfor = new Date();
                                            c2.setTime(ffinfor);
                                            c2.set(c2.get(c2.YEAR),c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            ffinfor = c2.getTime();
                                            if ((tmpmontoim.getFechainicio().compareTo(finifor) == 0 || tmpmontoim.getFechainicio().compareTo(finifor)>0) && (tmpmontoim.getFechafin().compareTo(ffinfor)<0 || tmpmontoim.getFechafin().compareTo(ffinfor)==0)) {                                                                                                
                                                adding = new totalesimpuestosinmuebles();
                                                adding.setFechainicio(tmpmontoim.getFechainicio().toString());
                                                adding.setFechafin(tmpmontoim.getFechafin().toString());
                                                adding.setTotalimpuesto(tmpmontoim.getMonto());
                                                listatotalesimp.add(adding);
                                            }                                            
                                        } catch (Exception e) {
                                            JOptionPane.showMessageDialog(null, "Error procesando datos en montos fijos");
                                            e.printStackTrace();
                                        }
                                    }                                                                                                            
                                }
                                //En el caso de interpretación de formulas
                                else{
                                    if (!xformula.getFechafin().isEmpty()) {
                                        try {
                                        finifor = formatoDelTexto.parse(xformula.getFechainicio());
                                        ffinfor = formatoDelTexto.parse(xformula.getFechafin());
                                        c2.setTime(ffinfor);
                                        c2.set(c2.get(c2.YEAR),c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                        ffinfor = c2.getTime();
                                        } catch (Exception e) {
                                            System.out.println("Error transformando las fechas inicio y fin de las formulas ");
                                            e.printStackTrace();
                                        }                                        
                                    }
                                    else{
                                        try {
                                            finifor = formatoDelTexto.parse(xformula.getFechainicio());
                                            ffinfor = new Date();
                                            c2.setTime(ffinfor);
                                            c2.set(c2.get(c2.YEAR),c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            ffinfor = c2.getTime();                                            
                                        } catch (Exception e) {
                                            System.out.println("Error transformando las fechas inicio y fin de las formulas ");
                                            e.printStackTrace();
                                        }
                                    }
                                    if ((tmpmontoim.getFechainicio().compareTo(finifor) == 0 || tmpmontoim.getFechainicio().compareTo(finifor)>0) && (tmpmontoim.getFechafin().compareTo(ffinfor)<0 || tmpmontoim.getFechafin().compareTo(ffinfor)==0)) {
                                        //Intepretando la formula de acuerdo al monto dado
                                    }
                                }
                            }

                            
                            
                        }
                    }
                    
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Aun no hay inmuebles registrados");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "La Carga incial ya ha sido generada, Generar cobros Mensuales");
        }
        
    }
    
}
