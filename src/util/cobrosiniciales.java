/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import peralcaldia.model.Boleta;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Impuestosinmuebles;
import peralcaldia.model.Formulas;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Detallepagos;
import peralcaldia.model.Estados;
import peralcaldia.model.Montosimpuestos;
import peralcaldia.model.Montosimpuestosnegocios;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;
import peralcaldia.model.Pagosadelantados;

/**
 *
 * @author alex
 */
/*Clase utilizada para la generacion de los diferentes tipos de cobros generados por el sistema*/
public class cobrosiniciales {

    AbstractDAO dao = new AbstractDAO();
    Boleta valboleta = new Boleta();

    /*Metodo para Generar la Carga Inicial de los Inmuebles*/
    public boolean cargainicialinmuebles() {
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
        List<verpagos> listapagosaingresar = new ArrayList<verpagos>();
        verpagos addpagoslist = new verpagos();
        List<listadetalledepagos> ldetallepagos = new ArrayList<listadetalledepagos>();
        listadetalledepagos objetodetalle = new listadetalledepagos();
        Detallepagos detallepago = new Detallepagos();

        BigDecimal total = BigDecimal.ZERO;
        //Variables fechas formula para obtener el monto a aplicar durante el periodo de validez de la formula.
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM-yyyy");
        Date finifor = new Date();
        Date ffinfor = new Date();
        Date finmonto = new Date();
        Date inimonto = new Date();
//        Date finimonto = new Date();
//        Date ffinmonto = new Date();
        Calendar c1 = new GregorianCalendar();
        Calendar c2 = new GregorianCalendar();
        Calendar c3 = new GregorianCalendar();
//        Calendar c4 = new GregorianCalendar();
        //Variables para interpretar formulas
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");
        String formula, periodoevaluar;
        Object resultado = new Object();

        int anio, actual, mes, mesfinal;
        Date registroinmu;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat mesformat = new SimpleDateFormat("MM");
        est = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");

        //verificando si la carga inicial no ha sido generada anteriormente.
        try {
            listapagos = dao.findByWhereStatement(Pagos.class, " id between 1 and 5 and mespagado <> 'VARIOS ADELANTADO'");
        } catch (Exception e) {
            System.out.println("Hubo un Error en la comprobación inicial");
            System.out.println(e.toString());
            return false;
        }
        //recuperando todos los inmuebles que se encuentran activos
        if (listapagos.isEmpty()) {
            try {
                listainmuebles = dao.findByWhereStatement(Inmuebles.class, "estados_id = 1");
            } catch (Exception e) {
                System.out.println("Hubo un error en la carga de inmuebles");
                System.out.println(e.toString());
                return false;
            }
            //Si la lista de inmuebles no esta vacía procedemos a sacar y procesar cada inmueble con sus respectivos impuestos.
            if (listainmuebles != null) {
                Iterator it = listainmuebles.iterator();
                while (it.hasNext()) {
                    //Recuperando Inmueble y lista de impuestos asignados
                    inmueble = (Inmuebles) it.next();
                    listaimpuestosinmuebles = inmueble.getImpuestosinmuebleses();
                    //Si la lista de impuestos no esta vacía iteramos la lista de impuestos para procesar cada impuesto.
                    if (listaimpuestosinmuebles != null) {
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
                                if (formula.equals("0") && !xformula.getUso().equals("OBSOLETO")) {
                                    Iterator itmntimpuestos = listamontos.iterator();
                                    while (itmntimpuestos.hasNext()) {
                                        tmpmontoim = (Montosimpuestos) itmntimpuestos.next();
                                        try {
                                            //inicio
                                            finifor = formatoDelTexto.parse(xformula.getFechainicio());
                                            c2.setTime(finifor);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            finifor = c2.getTime();
                                            finifor = codemd5.obteniendoprimerdiames(finifor);

                                            //fin
                                            ffinfor = new Date();
                                            c2.setTime(ffinfor);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            ffinfor = c2.getTime();


                                            //si el monto no tiene fecha de vencimiento.
                                            if (tmpmontoim.getFechafin() == null) {
                                                //inicio
                                                inimonto = tmpmontoim.getFechainicio();
                                                c2.setTime(inimonto);
                                                c2.set(Calendar.HOUR_OF_DAY, 0);
                                                c2.set(Calendar.SECOND, 0);
                                                c2.set(Calendar.MILLISECOND, 0);
                                                inimonto = c2.getTime();
                                                inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                                //fin
                                                finmonto = new Date();
                                                c2.setTime(finmonto);
                                                c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                                c2.set(Calendar.HOUR_OF_DAY, 23);
                                                c2.set(Calendar.SECOND, 59);
                                                c2.set(Calendar.MILLISECOND, 59);
                                                finmonto = c2.getTime();
                                            } else {
                                                //inicio
                                                inimonto = tmpmontoim.getFechainicio();
                                                c2.setTime(inimonto);
                                                c2.set(Calendar.HOUR_OF_DAY, 0);
                                                c2.set(Calendar.SECOND, 0);
                                                c2.set(Calendar.MILLISECOND, 0);
                                                inimonto = c2.getTime();
                                                inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                                //fin
                                                finmonto = tmpmontoim.getFechafin();
                                                c2.setTime(finmonto);
                                                c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                                c2.set(Calendar.HOUR_OF_DAY, 23);
                                                c2.set(Calendar.SECOND, 59);
                                                c2.set(Calendar.MILLISECOND, 59);
                                                finmonto = c2.getTime();
                                            }
                                            //procesando
                                            if ((inimonto.compareTo(finifor) > 0 || inimonto.compareTo(finifor) == 0) && (finmonto.compareTo(ffinfor) < 0 || finmonto.compareTo(ffinfor) == 0) && !tmpmontoim.getUso().equals("OBSOLETO")) {
                                                adding = new totalesimpuestosinmuebles();
                                                adding.setImpuesto(impuesto);
                                                adding.setFechainicio(tmpmontoim.getFechainicio());
                                                adding.setFechafin(tmpmontoim.getFechafin());
                                                adding.setTotalimpuesto(tmpmontoim.getMonto());
                                                listatotalesimp.add(adding);
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Error procesando datos en montos fijos");
                                            System.out.println(e.toString());
                                            return false;
                                        }
                                    }
                                } //En el caso de interpretación de formulas
                                else if (!xformula.getUso().equals("OBSOLETO")) {
                                    Iterator itmntimpuestos = listamontos.iterator();
                                    while (itmntimpuestos.hasNext()) {
                                        tmpmontoim = (Montosimpuestos) itmntimpuestos.next();
                                        if (xformula.getFechafin() != null) {
                                            try {
                                                //inicio
                                                finifor = formatoDelTexto.parse(xformula.getFechainicio());
                                                c2.setTime(finifor);
                                                c2.set(Calendar.HOUR_OF_DAY, 0);
                                                c2.set(Calendar.SECOND, 0);
                                                c2.set(Calendar.MILLISECOND, 0);
                                                finifor = c2.getTime();
                                                finifor = codemd5.obteniendoprimerdiames(finifor);
                                                //fin
                                                ffinfor = formatoDelTexto.parse(xformula.getFechafin());
                                                c2.setTime(ffinfor);
                                                c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                                c2.set(Calendar.HOUR_OF_DAY, 23);
                                                c2.set(Calendar.SECOND, 59);
                                                c2.set(Calendar.MILLISECOND, 59);
                                                ffinfor = c2.getTime();
                                            } catch (Exception e) {
                                                System.out.println("Error transformando las fechas inicio y fin de las formulas ");
                                                System.out.println(e.toString());
                                                return false;
                                            }
                                        } else {
                                            try {
                                                //inicio
                                                finifor = formatoDelTexto.parse(xformula.getFechainicio());
                                                c2.setTime(finifor);
                                                c2.set(Calendar.HOUR_OF_DAY, 0);
                                                c2.set(Calendar.SECOND, 0);
                                                c2.set(Calendar.MILLISECOND, 0);
                                                finifor = c2.getTime();
                                                finifor = codemd5.obteniendoprimerdiames(finifor);
                                                //fin
                                                ffinfor = new Date();
                                                c2.setTime(ffinfor);
                                                c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                                c2.set(Calendar.HOUR_OF_DAY, 23);
                                                c2.set(Calendar.SECOND, 59);
                                                c2.set(Calendar.MILLISECOND, 59);
                                                ffinfor = c2.getTime();
                                            } catch (Exception e) {
                                                System.out.println("Error transformando las fechas inicio y fin de las formulas ");
                                                System.out.println(e.toString());
                                                return false;
                                            }
                                        }
                                        //si el monto no tiene fecha de vencimiento.
                                        if (tmpmontoim.getFechafin() == null) {
                                            //inicio
                                            inimonto = tmpmontoim.getFechainicio();
                                            c2.setTime(inimonto);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            inimonto = c2.getTime();
                                            inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                            //fin
                                            finmonto = new Date();
                                            c2.setTime(finmonto);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            finmonto = c2.getTime();
                                        } else {
                                            //inicio
                                            inimonto = tmpmontoim.getFechainicio();
                                            c2.setTime(inimonto);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            inimonto = c2.getTime();
                                            inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                            //fin
                                            finmonto = tmpmontoim.getFechafin();
                                            c2.setTime(finmonto);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            finmonto = c2.getTime();
                                        }

                                        if ((inimonto.compareTo(finifor) > 0 || inimonto.compareTo(finifor) == 0) && (finmonto.compareTo(ffinfor) < 0 || finmonto.compareTo(ffinfor) == 0) && !tmpmontoim.getUso().equals("OBSOLETO")) {
                                            adding = new totalesimpuestosinmuebles();
                                            //Intepretando la formula de acuerdo al monto dado
                                            interprete.put("ML", inmueble.getMetros_lineales());
                                            interprete.put("MC", inmueble.getMetros_cuadrados());
                                            interprete.put("MONTO", tmpmontoim.getMonto());
                                            try {
                                                resultado = interprete.eval(formula);
                                            } catch (Exception e) {
                                                System.out.println("Error durante la evaluación de la formula");
                                                System.out.println(e.toString());
                                                return false;
                                            }
                                            adding.setImpuesto(impuesto);
                                            adding.setFechainicio(tmpmontoim.getFechainicio());
                                            adding.setFechafin(tmpmontoim.getFechafin());
                                            adding.setTotalimpuesto(new BigDecimal(resultado.toString()).setScale(2, RoundingMode.HALF_EVEN));
                                            listatotalesimp.add(adding);
                                        }
                                    }
                                }
                                //Fin de la Iteración de las Formulas.
                            }
                            //Fin de la Iteración de los impuestos
                        }
                        //CALCULANDO EL VALOR DE LOS PAGOS DEL INMUEBLE
                        //Determinando fechas para realizar el recorrido de determinación de meses
                        registroinmu = inmueble.getFecharegistro();

                        //Año de registro del inmueble
                        anio = Integer.parseInt(dateFormat.format(registroinmu));

                        //Mes del registro del inmueble
                        mes = Integer.parseInt(mesformat.format(registroinmu));

                        //obteniendo anio actual
                        c3 = Calendar.getInstance();
                        actual = c3.get(Calendar.YEAR);

                        //Creando Lista de Pagos a Insertar
                        for (int i = anio; i <= actual; i++) {
                            if (i != actual) {
                                //Iteración por mes
                                for (int j = mes; j <= 12; j++) {
                                    if (mes < 10) {
                                        periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                    } else {
                                        periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                    }
                                    //obteniendo del 1 al fin de cada mes
                                    try {
                                        //inicio
                                        finifor = formatoDelTexto.parse(periodoevaluar);
                                        c2.setTime(finifor);
                                        c2.set(Calendar.HOUR_OF_DAY, 0);
                                        c2.set(Calendar.SECOND, 0);
                                        c2.set(Calendar.MILLISECOND, 0);
                                        finifor = c2.getTime();
                                        finifor = codemd5.obteniendoprimerdiames(finifor);
                                        //fin
                                        ffinfor = formatoDelTexto.parse(periodoevaluar);
                                        c2.setTime(ffinfor);
                                        c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                        c2.set(Calendar.HOUR_OF_DAY, 23);
                                        c2.set(Calendar.SECOND, 59);
                                        c2.set(Calendar.MILLISECOND, 59);
                                        ffinfor = c2.getTime();
                                    } catch (Exception e) {
                                        System.out.println("Error durante el formateo de fechas");
                                        System.out.println(e.toString());
                                        return false;
                                    }
                                    //iterando listatotalesimpuestosinmuebles
                                    Iterator pg = listatotalesimp.iterator();
                                    total = BigDecimal.ZERO;
                                    while (pg.hasNext()) {
                                        adding = (totalesimpuestosinmuebles) pg.next();
                                        //si el monto no tiene fecha de vencimiento.
                                        if (adding.getFechafin() == null) {
                                            //inicio
                                            inimonto = adding.getFechainicio();
                                            c2.setTime(inimonto);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            inimonto = c2.getTime();
                                            inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                            //fin
                                            finmonto = new Date();
                                            c2.setTime(finmonto);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            finmonto = c2.getTime();
                                        } else {
                                            //inicio
                                            inimonto = adding.getFechainicio();
                                            c2.setTime(inimonto);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            inimonto = c2.getTime();
                                            inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                            //fin
                                            finmonto = adding.getFechafin();
                                            c2.setTime(finmonto);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            finmonto = c2.getTime();
                                        }
                                        objetodetalle = new listadetalledepagos();
                                        if ((finifor.compareTo(inimonto) > 0 || finifor.compareTo(inimonto) == 0) && (ffinfor.compareTo(finmonto) < 0 || ffinfor.compareTo(finmonto) == 0)) {
                                            total = total.add(adding.getTotalimpuesto());
                                            objetodetalle.setInmueble(inmueble);
                                            objetodetalle.setPeriodoevaluar(periodoevaluar);
                                            objetodetalle.setTotalimpuesto(adding);
                                            ldetallepagos.add(objetodetalle);
                                        }

                                    }
                                    try {
                                        addpagoslist = new verpagos();
                                        addpagoslist.setEstapagado(est);
                                        addpagoslist.setInmueble(inmueble);
                                        addpagoslist.setMespagado(periodoevaluar);
                                        addpagoslist.setMonto(total);
                                        listapagosaingresar.add(addpagoslist);
                                    } catch (Exception e) {
                                        System.out.println("Error durante la carga inicial de pagos");
                                        System.out.println(e.toString());
                                        return false;
                                    }
                                    mes += 1;
                                }
                            } //CALCULO PARA EL AÑO ACTUAL
                            else {
                                mesfinal = c3.get(Calendar.MONTH) + 1;
                                for (int j = mes; j <= mesfinal; j++) {
                                    if (mes < 10) {
                                        periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                    } else {
                                        periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                    }
                                    //obteniendo del 1 al fin de cada mes
                                    try {
                                        //inicio
                                        finifor = formatoDelTexto.parse(periodoevaluar);
                                        c2.setTime(finifor);
                                        c2.set(Calendar.HOUR_OF_DAY, 0);
                                        c2.set(Calendar.SECOND, 0);
                                        c2.set(Calendar.MILLISECOND, 0);
                                        finifor = c2.getTime();
                                        finifor = codemd5.obteniendoprimerdiames(finifor);
                                        //fin
                                        ffinfor = formatoDelTexto.parse(periodoevaluar);
                                        c2.setTime(ffinfor);
                                        c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                        c2.set(Calendar.HOUR_OF_DAY, 23);
                                        c2.set(Calendar.SECOND, 59);
                                        c2.set(Calendar.MILLISECOND, 59);
                                        ffinfor = c2.getTime();
                                    } catch (Exception e) {
                                        System.out.println("Error durante el formateo de fechas");
                                        System.out.println(e.toString());
                                        return false;
                                    }
                                    //iterando listatotalesimpuestosinmuebles
                                    Iterator pg = listatotalesimp.iterator();
                                    total = BigDecimal.ZERO;
                                    while (pg.hasNext()) {
                                        adding = (totalesimpuestosinmuebles) pg.next();
                                        //si el monto no tiene fecha de vencimiento.
                                        if (adding.getFechafin() == null) {
                                            //inicio
                                            inimonto = adding.getFechainicio();
                                            c2.setTime(inimonto);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            inimonto = c2.getTime();
                                            inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                            //fin
                                            finmonto = new Date();
                                            c2.setTime(finmonto);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            finmonto = c2.getTime();
                                        } else {
                                            //inicio
                                            inimonto = adding.getFechainicio();
                                            c2.setTime(inimonto);
                                            c2.set(Calendar.HOUR_OF_DAY, 0);
                                            c2.set(Calendar.SECOND, 0);
                                            c2.set(Calendar.MILLISECOND, 0);
                                            inimonto = c2.getTime();
                                            inimonto = codemd5.obteniendoprimerdiames(inimonto);
                                            //fin
                                            finmonto = adding.getFechafin();
                                            c2.setTime(finmonto);
                                            c2.set(c2.get(c2.YEAR), c2.get(c2.MONTH), c2.getActualMaximum(c2.DAY_OF_MONTH));
                                            c2.set(Calendar.HOUR_OF_DAY, 23);
                                            c2.set(Calendar.SECOND, 59);
                                            c2.set(Calendar.MILLISECOND, 59);
                                            finmonto = c2.getTime();
                                        }
                                        objetodetalle = new listadetalledepagos();
                                        if ((finifor.compareTo(inimonto) > 0 || finifor.compareTo(inimonto) == 0) && (ffinfor.compareTo(finmonto) < 0 || ffinfor.compareTo(finmonto) == 0)) {
                                            total = total.add(adding.getTotalimpuesto());
                                            objetodetalle.setInmueble(inmueble);
                                            objetodetalle.setPeriodoevaluar(periodoevaluar);
                                            objetodetalle.setTotalimpuesto(adding);
                                            ldetallepagos.add(objetodetalle);
                                        }
                                    }
                                    try {
                                        addpagoslist = new verpagos();
                                        addpagoslist.setEstapagado(est);
                                        addpagoslist.setInmueble(inmueble);
                                        addpagoslist.setMespagado(periodoevaluar);
                                        addpagoslist.setMonto(total);
                                        listapagosaingresar.add(addpagoslist);
                                    } catch (Exception e) {
                                        System.out.println("Error durante la carga inicial de pagos");
                                        System.out.println(e.toString());
                                        return false;
                                    }
                                    mes += 1;
                                }
                            }
                            mes = 1;
                        }
                        listatotalesimp = new ArrayList<totalesimpuestosinmuebles>();
                        //Fin de la condicional para calculo
                    }
                    //Fin de la iteración de la lista de impuestos inmuebles                    
                }
                if (listapagosaingresar.size() != 0) {
                    try {
                        Iterator itvp = listapagosaingresar.iterator();
                        while (itvp.hasNext()) {
                            //seteando el pago.
                            addpagoslist = (verpagos) itvp.next();
                            if (addpagoslist.getInmueble().getImpuestosinmuebleses().size() != 0) {
                                pago = new Pagos();
                                pago.setInmuebles(addpagoslist.getInmueble());
                                pago.setMespagado(addpagoslist.getMespagado());
                                pago.setMonto(addpagoslist.getMonto());
                                pago.setMontopagado(BigDecimal.ZERO);
                                pago.setDescuento(0);
                                pago.setEstadosid(addpagoslist.getEstapagado());
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getInmuebles().getId() == objetodetalle.getInmueble().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setImpuestos(objetodetalle.getTotalimpuesto().getImpuesto());
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            }
                        }
                        listapagosaingresar = new ArrayList<verpagos>();
                        ldetallepagos = new ArrayList<listadetalledepagos>();
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error en la inserción de pagos a la BD");
                        System.out.println(e.toString());
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                System.out.println("Aun no hay inmuebles registrados");
                return false;
            }
        } else {
            System.out.println("La Carga incial ya ha sido generada, Generar cobros Mensuales");
            return false;
        }

    }

    /*Metodo para Generar la Carga Inicial de los Negocios*/
    public boolean cargainicialnegocios() {
        //Declaración de Variables
        List<Negocios> lnegocios = null;
        Negocios negocio = new Negocios();
        Set<Montosimpuestosnegocios> limpuestosnegocios = null;
        Montosimpuestosnegocios mntimpnegocio = new Montosimpuestosnegocios();
        List<Pagos> lpagos = null;
        Pagos pago = new Pagos();
        List<totalesimpuestosinmuebles> ltotales = new ArrayList<totalesimpuestosinmuebles>();
        totalesimpuestosinmuebles addtotales = new totalesimpuestosinmuebles();
        List<verpagos> listadepagos = new ArrayList<verpagos>();
        verpagos addpago = new verpagos();
        Estados estado = new Estados();
        List<listadetalledepagos> ldetallepagos = new ArrayList<listadetalledepagos>();
        listadetalledepagos objetodetalle = new listadetalledepagos();
        Detallepagos detallepago = new Detallepagos();
        Date fechainimonto, fechafinmonto, fechainicioperiodo, fechafinperiodo, fecharegistronegocio;

        int anio, mes, mesfinal, anioactual, resultado;
        String periodoevaluar;
        BigDecimal total = BigDecimal.ZERO;
        //inicializando variables estaticas
        estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        //verificando si la carga inicial no ha sido generada anteriormente.
        try {
            lpagos = dao.findByWhereStatement(Pagos.class, "negocios_id between 1 and 15 and mespagado <> 'VARIOS ADELANTADO'");
        } catch (Exception e) {
            System.out.println("Hubo un Error en la comprobación inicial");

            return false;
        }
        //recuperando todos los inmuebles que se encuentran activos
        if (lpagos.isEmpty()) {
            //Desarrollo de las iteraciones para el calculo de los impuestos de los negocios.
            try {
                lnegocios = dao.findByWhereStatement(Negocios.class, "estados_id = 1");
            } catch (Exception e) {

                System.out.println("Ocurrio un Error durante la carga de los negocios");
                return false;
            }
            //comprobando la existencia de negocios activos.
            if (lnegocios != null) {
                Iterator negociositerador = lnegocios.iterator();
                //Iterando los negocios.
                while (negociositerador.hasNext()) {
                    negocio = (Negocios) negociositerador.next();
                    //lista de montos correspondiente al negocio
                    limpuestosnegocios = negocio.getMntimpuestoes();
                    //Comprobando que la lista de impuestos no se encuentre vacia
                    if (limpuestosnegocios != null) {
                        Iterator limpnegiterador = limpuestosnegocios.iterator();
                        //Iterando la lista de impuestos negocios
                        while (limpnegiterador.hasNext()) {
                            //recuperando el monto impuesto
                            mntimpnegocio = (Montosimpuestosnegocios) limpnegiterador.next();
                            //verificando si la fecha fin se encuentra llena y formateando las fechas
                            if (mntimpnegocio.getFechafin() != null) {
                                fechainimonto = codemd5.formatearfechazerohoras(mntimpnegocio.getFechainicio());
                                fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                fechafinmonto = codemd5.formatearfechaultimahora(mntimpnegocio.getFechafin());
                                fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                            } else {
                                fechainimonto = codemd5.formatearfechazerohoras(mntimpnegocio.getFechainicio());
                                fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                            }
                            //Agregando los montos impuestos del negocio
                            if (!mntimpnegocio.getUso().equals("OBSOLETO")) {
                                addtotales = new totalesimpuestosinmuebles();
                                addtotales.setNImpuesto(Integer.toString(mntimpnegocio.getNegocios().getId()));
                                addtotales.setFechainicio(fechainimonto);
                                addtotales.setFechafin(fechafinmonto);
                                addtotales.setTotalimpuesto(mntimpnegocio.getMonto());
                                ltotales.add(addtotales);
                            }
                            //Fin de la Iteración de los montos correspondientes a los impuestos del negocio.
                        }
                        //Calculando los pagos de los negocios
                        //Determinando fechas para realizar el recorrido de determinación de meses
                        fecharegistronegocio = negocio.getFecha_registro();
                        //Año de registro del inmueble
                        anio = codemd5.getanioregistro(fecharegistronegocio);
                        //Mes del registro del inmueble
                        mes = codemd5.getmesregistro(fecharegistronegocio);
                        //obteniendo anio actual
                        anioactual = codemd5.getanioactual();
                        //Creando lista de pagos a insertar
                        for (int i = anio; i <= anioactual; i++) {
                            if (i != anioactual) {
                                //iterando por mes
                                for (int j = mes; j <= 12; j++) {
                                    if (mes < 10) {
                                        periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                    } else {
                                        periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                    }
                                    //obteniendo del 1 al fin de cada mes
                                    //inicio
                                    fechainicioperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                    fechainicioperiodo = codemd5.obteniendoprimerdiames(fechainicioperiodo);
                                    //fin
                                    fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                    fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                    //Iterando los montos impuestos totales.
                                    Iterator listatotales = ltotales.iterator();
                                    total = BigDecimal.ZERO;
                                    while (listatotales.hasNext()) {
                                        addtotales = (totalesimpuestosinmuebles) listatotales.next();
                                        objetodetalle = new listadetalledepagos();
                                        if (codemd5.compararangofechas(fechainicioperiodo, fechafinperiodo, addtotales.getFechainicio(), addtotales.getFechafin())) {
                                            total = total.add(addtotales.getTotalimpuesto());
                                            objetodetalle.setNegocio(negocio);
                                            objetodetalle.setPeriodoevaluar(periodoevaluar);
                                            objetodetalle.setTotalimpuesto(addtotales);
                                            ldetallepagos.add(objetodetalle);
                                        }
                                    }
                                    try {
                                        addpago = new verpagos();
                                        addpago.setEstapagado(estado);
                                        addpago.setNegocio(negocio);
                                        addpago.setMespagado(periodoevaluar);
                                        addpago.setMonto(total);
                                        listadepagos.add(addpago);
                                    } catch (Exception e) {
                                        System.out.println("Error durante la carga inicial de pagos");
                                        System.out.println(e.toString());
                                        return false;
                                    }
                                    mes += 1;
                                }
                            } else {
                                mesfinal = codemd5.getmesregistro(new Date());
                                for (int j = mes; j <= mesfinal; j++) {
                                    if (mes < 10) {
                                        periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                    } else {
                                        periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                    }
                                    //obteniendo del 1 al fin de cada mes
                                    //inicio
                                    fechainicioperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                    fechainicioperiodo = codemd5.obteniendoprimerdiames(fechainicioperiodo);
                                    //fin
                                    fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                    fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                    //Iterando los montos impuestos totales.
                                    Iterator listatotales = ltotales.iterator();
                                    total = BigDecimal.ZERO;
                                    while (listatotales.hasNext()) {
                                        addtotales = (totalesimpuestosinmuebles) listatotales.next();
                                        objetodetalle = new listadetalledepagos();
                                        if (codemd5.compararangofechas(fechainicioperiodo, fechafinperiodo, addtotales.getFechainicio(), addtotales.getFechafin())) {
                                            total = total.add(addtotales.getTotalimpuesto());
                                            objetodetalle.setNegocio(negocio);
                                            objetodetalle.setPeriodoevaluar(periodoevaluar);
                                            objetodetalle.setTotalimpuesto(addtotales);
                                            ldetallepagos.add(objetodetalle);
                                        }
                                    }
                                    try {
                                        addpago = new verpagos();
                                        addpago.setEstapagado(estado);
                                        addpago.setNegocio(negocio);
                                        addpago.setMespagado(periodoevaluar);
                                        addpago.setMonto(total);
                                        listadepagos.add(addpago);
                                    } catch (Exception e) {
                                        System.out.println("Error durante la carga inicial de pagos");
                                        System.out.println(e.toString());
                                        return false;
                                    }
                                    mes += 1;
                                }
                            }
                            mes = 1;
                            //Finaliza la iteración de los anios.
                        }
                        ltotales = new ArrayList<totalesimpuestosinmuebles>();
                    }
                    //Fin de la iteración de la lista de negocios
                }
                //Agregando Pagos
                if (listadepagos.size() != 0) {
                    try {
                        Iterator itvp = listadepagos.iterator();
                        while (itvp.hasNext()) {
                            //seteando el pago.
                            addpago = (verpagos) itvp.next();
                            if (addpago.getNegocio().getMntimpuestoes().size() != 0) {
                                pago = new Pagos();
                                pago.setNegocios(addpago.getNegocio());
                                pago.setMespagado(addpago.getMespagado());
                                pago.setMonto(addpago.getMonto());
                                pago.setMontopagado(BigDecimal.ZERO);
                                pago.setDescuento(0);
                                pago.setEstadosid(addpago.getEstapagado());
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getNegocios().getId() == objetodetalle.getNegocio().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            }
                        }
                        listadepagos = new ArrayList<verpagos>();
                        ldetallepagos = new ArrayList<listadetalledepagos>();
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error en la inserción de pagos a la BD");
                        System.out.println(e.toString());
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                System.out.println("No existen negocios activos en este momento");
                return false;
            }
        } else {
            System.out.println("La carga inicial ya ha sido generada para los negocios");
            return false;
        }
    }

    /*Metodo Para Generar la Carga Mensual de los Nuevos Inmuebles Ingresados en el Sistema durante un periodo determinado*/
    public boolean gencbnuevosinmuebles() {
        List<Pagos> lpagos = null;
        Pagos pago = new Pagos();
        List<Inmuebles> linmuebles = null;
        Inmuebles inmueble = new Inmuebles();
        List<listadetalledepagos> ldetallepagos = new ArrayList<listadetalledepagos>();
        listadetalledepagos objetodetalle = new listadetalledepagos();
        List<totalesimpuestosinmuebles> ltotales = new ArrayList<totalesimpuestosinmuebles>();
        totalesimpuestosinmuebles addtotal = new totalesimpuestosinmuebles();
        List<verpagos> lverpagos = new ArrayList<verpagos>();
        verpagos vpg = new verpagos();
        Set<Impuestosinmuebles> limpuestosinmuebles = null;
        Impuestosinmuebles impinmueble = new Impuestosinmuebles();
        Set<Montosimpuestos> lmontosimpuestos = null;
        Montosimpuestos montoimpuesto = new Montosimpuestos();
        Set<Formulas> lformulas = null;
        Formulas xformula = new Formulas();
        Impuestos impuesto = new Impuestos();
        Estados estado = new Estados();
        Estados estcancel = new Estados();
        Detallepagos detallepago = new Detallepagos();
        Date fechainiformula, fechafinformula, fechainimonto, fechafinmonto, fechainiperiodo, fechafinperiodo, fecharegistro;
        BigDecimal montototal;
        int mes, anioactual;
        String periodoevaluar;
        BigDecimal total = BigDecimal.ZERO;
        //inicializando variables estaticas
        estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        estcancel = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");
        //Variables para interpretar formulas
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");
        String formula;
        Object resultadoformula = new Object();

        //Inicilizando periodo, para recobrar lista de inmuebles ingresados durante el mes actual
        fechainiperiodo = codemd5.formatearfechazerohoras(new Date());
        fechainiperiodo = codemd5.obteniendoprimerdiames(fechainiperiodo);
        fechafinperiodo = codemd5.formatearfechaultimahora(new Date());
        fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);

        try {
            //Cargando lista de inmuebles que han sido ingresados en el periodo actual
            linmuebles = dao.findByWhereStatement(Inmuebles.class, "fecharegistro between '" + fechainiperiodo.toString() + "' and '" + fechafinperiodo.toString() + "' and estados_id = 1");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error durante la recuperación de nuevos inmuebles");
            return false;
        }

        //Mes correspondiente al periodo
        mes = codemd5.getmesregistro(fechainiperiodo);
        //obteniendo anio actual
        anioactual = codemd5.getanioactual();
        if (mes < 10) {
            periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(anioactual);
        } else {
            periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(anioactual);
        }
        //comprando si hay nuevos inmuebles para el periodo
        if (linmuebles != null) {
            //Iterando la lista de nuevos inmuebles
            Iterator linmueblesiterador = linmuebles.iterator();
            while (linmueblesiterador.hasNext()) {
                inmueble = (Inmuebles) linmueblesiterador.next();
                try {
                    pago = (Pagos) dao.findByWhereStatementoneobj(Pagos.class, "inmuebles_id = " + inmueble.getId() + " and mespagado = '" + periodoevaluar + "'");
                } catch (Exception e) {
                    System.out.println("Error durante la comprobación de la existencia del pago");
                    System.out.println(e.toString());
                    return false;
                }
                if (pago == null) {
                    //obteniendo la lista de impuestos inmuebles asignados.
                    limpuestosinmuebles = inmueble.getImpuestosinmuebleses();
                    //comprobando que la lista no se encuentre vacia antes de iterar
                    if (limpuestosinmuebles != null) {
                        //iterando la lista de impuestos inmuebles y recuperando el impuesto por cada iteración
                        Iterator liminmiterador = limpuestosinmuebles.iterator();
                        while (liminmiterador.hasNext()) {
                            impinmueble = (Impuestosinmuebles) liminmiterador.next();
                            impuesto = impinmueble.getImpuestos();
                            //Obteniendo lista de formulas y lista de montos del impuesto recuperado.
                            lformulas = impuesto.getFormulaes();
                            lmontosimpuestos = impuesto.getMontosimpuestoses();
                            //Iterando la lista de formulas
                            Iterator formit = lformulas.iterator();
                            while (formit.hasNext()) {
                                xformula = (Formulas) formit.next();
                                //Recuperando la formula
                                formula = xformula.getFormula();
                                if (formula.equals("0") && !xformula.getUso().equals("OBSOLETO")) {
                                    //Iterando la lista de montos impuestos para validos para la formula
                                    Iterator iteradormontosimpuesto = lmontosimpuestos.iterator();
                                    while (iteradormontosimpuesto.hasNext()) {
                                        montoimpuesto = (Montosimpuestos) iteradormontosimpuesto.next();
                                        //Inicializando los periodos de la formula y del monto.
                                        //periodo formula.
                                        fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                        fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                        fechafinformula = codemd5.formatearfechaultimahora(new Date());
                                        fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                        //periodo monto.
                                        if (montoimpuesto.getFechafin() == null) {
                                            fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                            fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                            fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                            fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                        } else {
                                            fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                            fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                            fechafinmonto = codemd5.formatearfechaultimahora(montoimpuesto.getFechafin());
                                            fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                        }
                                        if (codemd5.compararangofechas(fechainimonto, fechafinmonto, fechainiformula, fechafinformula) && !montoimpuesto.getUso().equals("OBSOLETO")) {
                                            addtotal = new totalesimpuestosinmuebles();
                                            //Lista Totales
                                            addtotal.setImpuesto(impuesto);
                                            addtotal.setFechainicio(fechainimonto);
                                            addtotal.setFechafin(fechafinmonto);
                                            addtotal.setTotalimpuesto(montoimpuesto.getMonto());
                                            ltotales.add(addtotal);
                                        }
                                    }
                                } //En el caso de interpretación de formulas
                                else if (!xformula.getUso().equals("OBSOLETO")) {
                                    Iterator iteradormontosimpuesto = lmontosimpuestos.iterator();
                                    while (iteradormontosimpuesto.hasNext()) {
                                        montoimpuesto = (Montosimpuestos) iteradormontosimpuesto.next();
                                        //Inicializando Periodos de Formula y Montos
                                        //Periodo Formula
                                        if (xformula.getFechafin() != null) {
                                            fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                            fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                            fechafinformula = codemd5.formatearfechaultimahora(xformula.getFechafin());
                                            fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                        } else {
                                            fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                            fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                            fechafinformula = codemd5.formatearfechaultimahora(new Date());
                                            fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                        }
                                        //periodo monto.
                                        if (montoimpuesto.getFechafin() == null) {
                                            fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                            fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                            fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                            fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                        } else {
                                            fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                            fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                            fechafinmonto = codemd5.formatearfechaultimahora(montoimpuesto.getFechafin());
                                            fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                        }
                                        if (codemd5.compararangofechas(fechainimonto, fechafinmonto, fechainiformula, fechafinformula) && !montoimpuesto.getUso().equals("OBSOLETO")) {
                                            addtotal = new totalesimpuestosinmuebles();
                                            objetodetalle = new listadetalledepagos();
                                            //Intepretando la formula de acuerdo al monto dado
                                            interprete.put("ML", inmueble.getMetros_lineales());
                                            interprete.put("MC", inmueble.getMetros_cuadrados());
                                            interprete.put("MONTO", montoimpuesto.getMonto());
                                            try {
                                                resultadoformula = interprete.eval(formula);
                                            } catch (Exception e) {
                                                System.out.println("Error durante la evaluación de la formula");
                                                System.out.println(e.toString());
                                                return false;
                                            }
                                            //listatotales
                                            addtotal.setImpuesto(impuesto);
                                            addtotal.setFechainicio(fechainimonto);
                                            addtotal.setFechafin(fechafinmonto);
                                            addtotal.setTotalimpuesto(new BigDecimal(resultadoformula.toString()).setScale(2, RoundingMode.HALF_EVEN));
                                            ltotales.add(addtotal);
                                        }
                                    }
                                }
                            }//Fin de la iteración de Formulas                        
                        }
                        //Iterando la lista de totales, para obtener el total de los impuestos
                        Iterator ltotalesiterador = ltotales.iterator();
                        total = BigDecimal.ZERO;
                        while (ltotalesiterador.hasNext()) {
                            addtotal = (totalesimpuestosinmuebles) ltotalesiterador.next();
                            objetodetalle = new listadetalledepagos();
                            if (codemd5.compararangofechas(fechainiperiodo, fechafinperiodo, addtotal.getFechainicio(), addtotal.getFechafin())) {
                                total = total.add(addtotal.getTotalimpuesto());
                                //Guardando los detalles de los impuestos por inmueble y periodo a evaluar.
                                objetodetalle.setInmueble(inmueble);
                                objetodetalle.setPeriodoevaluar(periodoevaluar);
                                objetodetalle.setTotalimpuesto(addtotal);
                                ldetallepagos.add(objetodetalle);
                            }
                        }
                        //ingresando el pago del inmueble agregado en el presente periodo
                        try {
                            vpg = new verpagos();
                            vpg.setEstapagado(estado);
                            vpg.setInmueble(inmueble);
                            vpg.setMespagado(periodoevaluar);
                            vpg.setMonto(total);
                            lverpagos.add(vpg);

                        } catch (Exception e) {
                            System.out.println("Error durante la carga inicial de pagos");
                            System.out.println(e.toString());
                            return false;
                        }
                        ltotales = new ArrayList<totalesimpuestosinmuebles>();
                    }
                }
                //Fin de la iteración de inmuebles   
            }
            //Agregando los cobros para los nuevos inmuebles
            if (lverpagos.size() != 0) {
                try {
                    Iterator itvp = lverpagos.iterator();
                    while (itvp.hasNext()) {
                        //seteando el pago.
                        vpg = (verpagos) itvp.next();
                        if (vpg.getInmueble().getImpuestosinmuebleses().size() != 0) {
                            montototal = aplicarpgadelantado(vpg.getInmueble(), null, vpg.getMonto(), vpg.getMespagado());
                            if (montototal.compareTo(vpg.getMonto()) == 1) {
                                pago = new Pagos();
                                pago.setInmuebles(vpg.getInmueble());
                                pago.setMespagado(vpg.getMespagado());
                                pago.setMonto(vpg.getMonto());
                                pago.setMontopagado(montototal);
                                pago.setFechapago(new Date());
                                pago.setBoletas(valboleta);
                                pago.setNorecibo(valboleta.getRecibo());
                                pago.setComentario("PAGO AUTOMATICO POR PAGO ADELANTADO");
                                pago.setDescuento(0);
                                pago.setEstadosid(estcancel);
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getInmuebles().getId() == objetodetalle.getInmueble().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setImpuestos(objetodetalle.getTotalimpuesto().getImpuesto());
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }

                            } else {
                                pago = new Pagos();
                                pago.setInmuebles(vpg.getInmueble());
                                pago.setMespagado(vpg.getMespagado());
                                pago.setMonto(vpg.getMonto());
                                pago.setMontopagado(BigDecimal.ZERO);
                                pago.setDescuento(0);
                                pago.setEstadosid(vpg.getEstapagado());
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getInmuebles().getId() == objetodetalle.getInmueble().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setImpuestos(objetodetalle.getTotalimpuesto().getImpuesto());
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            }
                        }
                    }
                    lverpagos = new ArrayList<verpagos>();
                    ldetallepagos = new ArrayList<listadetalledepagos>();
                    return true;
                } catch (Exception e) {
                    System.out.println("Error en la inserción de pagos a la BD");
                    System.out.println(e.toString());
                    return false;
                }
            } else {
                return false;
            }
        } else {
            System.out.println("No se encontraron inmuebles ingresados en el mes actual");
            return false;
        }
    }

    /*Metodo Para Generar la Carga Mensual de los Nuevos Negocios Ingresados en el Sistema durante un periodo determinado*/
    public boolean gencbnuevosnegocios() {
        //Declaración de Variables
        List<Negocios> lnegocios = null;
        Negocios negocio = new Negocios();
        Set<Montosimpuestosnegocios> limpuestosnegocios = null;
        Montosimpuestosnegocios mntimpnegocio = new Montosimpuestosnegocios();
        List<Pagos> lpagos = null;
        Pagos pago = new Pagos();
        List<totalesimpuestosinmuebles> ltotales = new ArrayList<totalesimpuestosinmuebles>();
        totalesimpuestosinmuebles addtotales = new totalesimpuestosinmuebles();
        List<verpagos> listadepagos = new ArrayList<verpagos>();
        verpagos addpago = new verpagos();
        Estados estado = new Estados();
        Estados estcancel = new Estados();
        List<listadetalledepagos> ldetallepagos = new ArrayList<listadetalledepagos>();
        listadetalledepagos objetodetalle = new listadetalledepagos();
        Detallepagos detallepago = new Detallepagos();
        Date fechainimonto, fechafinmonto, fechainicioperiodo, fechafinperiodo;

        int anio, mes, mesfinal, anioactual, resultado;
        String periodoevaluar;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal montototal;
        //inicializando variables estaticas
        estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        estcancel = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");

        //Inicilizando periodo, para recobrar lista de inmuebles ingresados durante el mes actual
        fechainicioperiodo = codemd5.formatearfechazerohoras(new Date());
        fechainicioperiodo = codemd5.obteniendoprimerdiames(fechainicioperiodo);
        fechafinperiodo = codemd5.formatearfechaultimahora(new Date());
        fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);

        //Cargando lista de negocios que han sido ingresados en el periodo actual
        try {
            lnegocios = dao.findByWhereStatement(Negocios.class, "fecha_registro between '" + fechainicioperiodo.toString() + "' and '" + fechafinperiodo.toString() + "' and estados_id = 1");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error durante la recuperacion de inmuebles del periodo actual");
            return false;
        }
        //Mes correspondiente al periodo
        mes = codemd5.getmesregistro(fechainicioperiodo);
        //obteniendo anio actual
        anioactual = codemd5.getanioactual();
        if (mes < 10) {
            periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(anioactual);
        } else {
            periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(anioactual);
        }
        //comprobando la existencia de negocios activos.
        if (lnegocios != null) {
            //Iterando la lista de negocios fuera de esta iteración se realiza el ingreso de los pagos.
            Iterator negociositerador = lnegocios.iterator();
            while (negociositerador.hasNext()) {
                negocio = (Negocios) negociositerador.next();
                try {
                    pago = (Pagos) dao.findByWhereStatementoneobj(Pagos.class, "negocios_id = " + negocio.getId() + " and mespagado = '" + periodoevaluar + "'");
                } catch (Exception e) {
                    System.out.println("Error durante la comprobación de la existencia del pago");
                    System.out.println(e.toString());
                    return false;
                }
                if (pago == null) {
                    //lista de montos correspondiente al negocio
                    limpuestosnegocios = negocio.getMntimpuestoes();
                    //Comprobando que la lista de impuestos no se encuentre vacia
                    if (limpuestosnegocios != null) {
                        //iterando la lista de impuestos negocios para recuperar los montos aplicacos al negocio según calificación
                        Iterator limpnegiterador = limpuestosnegocios.iterator();
                        while (limpnegiterador.hasNext()) {
                            mntimpnegocio = (Montosimpuestosnegocios) limpnegiterador.next();
                            if (mntimpnegocio.getFechafin() != null) {
                                fechainimonto = codemd5.formatearfechazerohoras(mntimpnegocio.getFechainicio());
                                fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                fechafinmonto = codemd5.formatearfechaultimahora(mntimpnegocio.getFechafin());
                                fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                            } else {
                                fechainimonto = codemd5.formatearfechazerohoras(mntimpnegocio.getFechainicio());
                                fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                            }
                            //Agregando los montos impuestos del negocio
                            if (!mntimpnegocio.getUso().equals("OBSOLETO")) {
                                addtotales = new totalesimpuestosinmuebles();
                                addtotales.setFechainicio(fechainimonto);
                                addtotales.setFechafin(fechafinmonto);
                                addtotales.setTotalimpuesto(mntimpnegocio.getMonto());
                                ltotales.add(addtotales);
                            }
                            //Fin de la Iteración de los montos correspondientes a los impuestos del negocio.                            
                        }
                        //Iterando los montos impuestos totales.
                        Iterator listatotalesiterador = ltotales.iterator();
                        total = BigDecimal.ZERO;
                        while (listatotalesiterador.hasNext()) {
                            addtotales = (totalesimpuestosinmuebles) listatotalesiterador.next();
                            objetodetalle = new listadetalledepagos();
                            if (codemd5.compararangofechas(fechainicioperiodo, fechafinperiodo, addtotales.getFechainicio(), addtotales.getFechafin())) {
                                total = total.add(addtotales.getTotalimpuesto());
                                objetodetalle.setNegocio(negocio);
                                objetodetalle.setPeriodoevaluar(periodoevaluar);
                                objetodetalle.setTotalimpuesto(addtotales);
                                ldetallepagos.add(objetodetalle);
                            }
                        }
                        try {
                            addpago = new verpagos();
                            addpago.setEstapagado(estado);
                            addpago.setNegocio(negocio);
                            addpago.setMespagado(periodoevaluar);
                            addpago.setMonto(total);
                            listadepagos.add(addpago);
                        } catch (Exception e) {
                            System.out.println("Error durante la carga inicial de pagos");
                            System.out.println(e.toString());
                            return false;
                        }
                        //Fin de los procesos despues de comparar si la lista de montos impuestos no esta vacia.
                        ltotales = new ArrayList<totalesimpuestosinmuebles>();
                    }
                    //Fin de la verificación si el pago ya se encontraba previamente registrado.
                }
                //Fin de la iteración de la lista de negocios despues de esta iteración y dentro de la comprobación de que hayan datos en la lista de
                //negocios se agregan los pagos.
            }
            if (listadepagos.size() != 0) {
                try {
                    Iterator itvp = listadepagos.iterator();
                    while (itvp.hasNext()) {
                        //seteando el pago.
                        addpago = (verpagos) itvp.next();
                        if (addpago.getNegocio().getMntimpuestoes().size() != 0) {
                            montototal = aplicarpgadelantado(null, addpago.getNegocio(), addpago.getMonto(), addpago.getMespagado());
                            if (montototal.compareTo(addpago.getMonto()) == 1) {
                                pago = new Pagos();
                                pago.setNegocios(addpago.getNegocio());
                                pago.setMespagado(addpago.getMespagado());
                                pago.setMonto(addpago.getMonto());
                                pago.setMontopagado(montototal);
                                pago.setBoletas(valboleta);
                                pago.setNorecibo(valboleta.getRecibo());
                                pago.setFechapago(new Date());
                                pago.setComentario("PAGO AUTOMATICO POR PAGO ADELANTADO");
                                pago.setDescuento(0);
                                pago.setEstadosid(estcancel);
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getNegocios().getId() == objetodetalle.getNegocio().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            } else {
                                pago = new Pagos();
                                pago.setNegocios(addpago.getNegocio());
                                pago.setMespagado(addpago.getMespagado());
                                pago.setMonto(addpago.getMonto());
                                pago.setMontopagado(BigDecimal.ZERO);
                                pago.setDescuento(0);
                                pago.setEstadosid(addpago.getEstapagado());
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getNegocios().getId() == objetodetalle.getNegocio().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            }
                        }
                    }
                    listadepagos = new ArrayList<verpagos>();
                    ldetallepagos = new ArrayList<listadetalledepagos>();
                    return true;
                } catch (Exception e) {
                    System.out.println("Error en la inserción de pagos a la BD");
                    System.out.println(e.toString());
                    return false;
                }
            } else {
                return false;
            }
            //Fin de la verificación de la lista de negocios agregados para el presente periodo.
        } else {
            System.out.println("No existen nuevos negocios registrados en este periodo");
            return false;
        }
    }

    /*Metodo Para Generar la Carga Mensual Inmuebles*/
    public boolean generarcobrosmensualesinmuebles() {
        List<Pagos> lpagos = null;
        Pagos pago = new Pagos();
        List<Inmuebles> linmuebles = null;
        Inmuebles inmueble = new Inmuebles();
        List<totalesimpuestosinmuebles> ltotales = new ArrayList<totalesimpuestosinmuebles>();
        totalesimpuestosinmuebles addtotal = new totalesimpuestosinmuebles();
        List<verpagos> lverpagos = new ArrayList<verpagos>();
        verpagos vpg = new verpagos();
        Set<Impuestosinmuebles> limpuestosinmuebles = null;
        Impuestosinmuebles impinmueble = new Impuestosinmuebles();
        Set<Montosimpuestos> lmontosimpuestos = null;
        Montosimpuestos montoimpuesto = new Montosimpuestos();
        Set<Formulas> lformulas = null;
        Formulas xformula = new Formulas();
        Impuestos impuesto = new Impuestos();
        Estados estado = new Estados();
        Estados estcancel = new Estados();
        List<listadetalledepagos> ldetallepagos = new ArrayList<listadetalledepagos>();
        listadetalledepagos objetodetalle = new listadetalledepagos();
        Detallepagos detallepago = new Detallepagos();
        Date fechainiformula, fechafinformula, fechainimonto, fechafinmonto, fechainiperiodo, fechafinperiodo, fecharegistro;
        Date fechaultimopago;

        int anio, mes, mesfinal, anioactual;
        String periodoevaluar;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal montototal = BigDecimal.ZERO;
        //inicializando variables estaticas
        estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        estcancel = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");
        //Variables para interpretar formulas
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");
        String formula;
        Object resultadoformula = new Object();

        //Cargando la lista de inmuebles activos
        try {
            linmuebles = dao.findByWhereStatement(Inmuebles.class, "estados_id = 1");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error durante la carga de inmuebles");
            return false;
        }
        if (linmuebles != null) {
            //Iterando la lista de Inmuebles
            Iterator iteradorinmuebles = linmuebles.iterator();
            while (iteradorinmuebles.hasNext()) {
                inmueble = (Inmuebles) iteradorinmuebles.next();
                //Recogiendo el último pago del inmueble.
                pago = new Pagos();
                pago = (Pagos) dao.findByWhereStatementmaximoobjeto(Pagos.class, "inmuebles_id = " + Integer.toString(inmueble.getId()) + " and mespagado <> 'VARIOS ADELANTADO'");
                //Verificando el último pago o si el inmueble no tiene pagos asignados hasta el momento.
                if (pago != null) {
                    //formateando la fecha del ultimo pago
                    fechaultimopago = codemd5.formatearfechazerohoras(pago.getMespagado());
                    fechaultimopago = codemd5.obteniendoprimerdiames(fechaultimopago);
                    //Recuperando lista de impuestos inmuebles
                    limpuestosinmuebles = inmueble.getImpuestosinmuebleses();
                    //Iterando impuestos inmuebles
                    Iterator limpinmiterador = limpuestosinmuebles.iterator();
                    while (limpinmiterador.hasNext()) {
                        impinmueble = (Impuestosinmuebles) limpinmiterador.next();
                        impuesto = impinmueble.getImpuestos();
                        //Recuperando la lista de formulas y montos pertenicientes al impuesto.
                        lmontosimpuestos = impuesto.getMontosimpuestoses();
                        lformulas = impuesto.getFormulaes();
                        //Iterando la lista de formulas
                        Iterator formulasiterador = lformulas.iterator();
                        while (formulasiterador.hasNext()) {
                            xformula = (Formulas) formulasiterador.next();
                            //Recuperando la formula.
                            formula = xformula.getFormula();
                            //Comprobando si existe o no formula a Interpretar
                            if (formula.equals("0") && !xformula.getUso().equals("OBSOLETO")) {
                                //Iterando los montos correspondientes al impuesto
                                Iterator mntimpiterador = lmontosimpuestos.iterator();
                                while (mntimpiterador.hasNext()) {
                                    montoimpuesto = (Montosimpuestos) mntimpiterador.next();
                                    //Periodo de validez de la formula a cero.
                                    //inicio
                                    fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                    fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                    //fin
                                    fechafinformula = codemd5.formatearfechaultimahora(new Date());
                                    fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                    //Verificando si la fecha del monto tiene fecha de vencimiento
                                    if (montoimpuesto.getFechafin() == null) {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    } else {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(montoimpuesto.getFechafin());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    }

                                    //Agregando los montos a la lista de totales
                                    if (codemd5.compararangofechas(fechainimonto, fechafinmonto, fechainiformula, fechafinformula) && !montoimpuesto.getUso().equals("OBSOLETO")) {
                                        addtotal = new totalesimpuestosinmuebles();
                                        addtotal.setImpuesto(impuesto);
                                        addtotal.setNImpuesto(impuesto.getNombre());
                                        addtotal.setFechainicio(fechainimonto);
                                        addtotal.setFechafin(fechafinmonto);
                                        addtotal.setTotalimpuesto(montoimpuesto.getMonto());
                                        ltotales.add(addtotal);
                                    }
                                }
                            }//Fin de la Iteración cuando no hay formula a interpretar.
                            //Inicio del caso de Interpretación de formulas.
                            else if (!xformula.getUso().equals("OBSOLETO")) {
                                Iterator mntimpiterador = lmontosimpuestos.iterator();
                                while (mntimpiterador.hasNext()) {
                                    montoimpuesto = (Montosimpuestos) mntimpiterador.next();
                                    //Periodo de validez de las formulas.
                                    //Periodo Formula
                                    if (xformula.getFechafin() != null) {
                                        //inicio
                                        fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                        fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                        //fin
                                        fechafinformula = codemd5.formatearfechaultimahora(xformula.getFechafin());
                                        fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                    } else {
                                        //inicio
                                        fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                        fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                        //fin
                                        fechafinformula = codemd5.formatearfechaultimahora(new Date());
                                        fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                    }
                                    //periodo monto.
                                    if (montoimpuesto.getFechafin() == null) {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    } else {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(montoimpuesto.getFechafin());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    }
                                    if (codemd5.compararangofechas(fechainimonto, fechafinmonto, fechainiformula, fechafinformula) && !montoimpuesto.getUso().equals("OBSOLETO")) {
                                        addtotal = new totalesimpuestosinmuebles();
                                        //Intepretando la formula de acuerdo al monto dado
                                        interprete.put("ML", inmueble.getMetros_lineales());
                                        interprete.put("MC", inmueble.getMetros_cuadrados());
                                        interprete.put("MONTO", montoimpuesto.getMonto());
                                        try {
                                            resultadoformula = interprete.eval(formula);
                                        } catch (Exception e) {
                                            System.out.println("Error durante la evaluación de la formula");
                                            System.out.println(e.toString());
                                            return false;
                                        }
                                        addtotal.setImpuesto(impuesto);
                                        addtotal.setNImpuesto(impuesto.getNombre());
                                        addtotal.setFechainicio(fechainimonto);
                                        addtotal.setFechafin(fechafinmonto);
                                        addtotal.setTotalimpuesto(new BigDecimal(resultadoformula.toString()).setScale(2, RoundingMode.HALF_EVEN));
                                        ltotales.add(addtotal);
                                    }
                                }
                            }
                        }//Fin de la iteración de las formulas.
                    }//Fin de la iteración de la lista de impuestos inmuebles.
                    //CALCULO DE LOS PAGOS A INGRESAR
                    //mes inicial de la iteración
                    mes = codemd5.getmesregistro(fechaultimopago) + 1;
                    //año del periodo
                    anio = codemd5.getanioregistro(fechaultimopago);
                    //año actual
                    anioactual = codemd5.getanioactual();
                    //Si el ultimo pago se realizo el ultimo mes de un anio se resetea el mes y se incrementa el anio.
                    if (mes > 12) {
                        mes = 1;
                        anio = anio + 1;
                    }
                    //Iteraciones para crear la lista de pagos
                    for (int i = anio; i <= anioactual; i++) {
                        if (i != anioactual) {
                            //Iteración por mes
                            for (int j = mes; j <= 12; j++) {
                                if (mes < 10) {
                                    periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                } else {
                                    periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                }
                                //Formateando el periodo a evaluar del primero a fin de mes
                                //inicio
                                fechainiperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                fechainiperiodo = codemd5.obteniendoprimerdiames(fechainiperiodo);
                                //fin
                                fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                //Iterando la Lista de Totales Impuestos.
                                Iterator ltotiterador = ltotales.iterator();
                                total = BigDecimal.ZERO;
                                while (ltotiterador.hasNext()) {
                                    addtotal = (totalesimpuestosinmuebles) ltotiterador.next();
                                    objetodetalle = new listadetalledepagos();
                                    if (codemd5.compararangofechas(fechainiperiodo, fechafinperiodo, addtotal.getFechainicio(), addtotal.getFechafin())) {
                                        total = total.add(addtotal.getTotalimpuesto());
                                        objetodetalle.setInmueble(inmueble);
                                        objetodetalle.setPeriodoevaluar(periodoevaluar);
                                        objetodetalle.setTotalimpuesto(addtotal);
                                        ldetallepagos.add(objetodetalle);
                                    }
                                }
                                //ingresando el pago del inmueble agregado en el presente periodo
                                try {
                                    vpg = new verpagos();
                                    vpg.setEstapagado(estado);
                                    vpg.setInmueble(inmueble);
                                    vpg.setMespagado(periodoevaluar);
                                    vpg.setMonto(total);
                                    lverpagos.add(vpg);
                                } catch (Exception e) {
                                    System.out.println("Error durante la carga inicial de pagos");
                                    System.out.println(e.toString());
                                    return false;
                                }
                                mes += 1;
                            }
                        }//Termina cuando el año es diferente del actual y comienza el caso del año actual
                        else {
                            mesfinal = codemd5.getmesregistro(new Date());
                            //Iteración por mes
                            for (int j = mes; j <= mesfinal; j++) {
                                if (mes < 10) {
                                    periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                } else {
                                    periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                }
                                //Formateando el periodo a evaluar del primero a fin de mes
                                //inicio
                                fechainiperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                fechainiperiodo = codemd5.obteniendoprimerdiames(fechainiperiodo);
                                //fin
                                fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                //Iterando la Lista de Totales Impuestos.
                                Iterator ltotiterador = ltotales.iterator();
                                total = BigDecimal.ZERO;
                                while (ltotiterador.hasNext()) {
                                    addtotal = (totalesimpuestosinmuebles) ltotiterador.next();
                                    objetodetalle = new listadetalledepagos();
                                    if (codemd5.compararangofechas(fechainiperiodo, fechafinperiodo, addtotal.getFechainicio(), addtotal.getFechafin())) {
                                        total = total.add(addtotal.getTotalimpuesto());
                                        objetodetalle.setInmueble(inmueble);
                                        objetodetalle.setPeriodoevaluar(periodoevaluar);
                                        objetodetalle.setTotalimpuesto(addtotal);
                                        ldetallepagos.add(objetodetalle);
                                    }
                                }
                                //ingresando el pago del inmueble agregado en el presente periodo
                                try {
                                    vpg = new verpagos();
                                    vpg.setEstapagado(estado);
                                    vpg.setInmueble(inmueble);
                                    vpg.setMespagado(periodoevaluar);
                                    vpg.setMonto(total);
                                    lverpagos.add(vpg);
                                } catch (Exception e) {
                                    System.out.println("Error durante la carga inicial de pagos");
                                    System.out.println(e.toString());
                                    return false;
                                }
                                mes += 1;
                            }
                        }
                        mes = 1;
                    }//Fin de la iteración de calculo de cobros a ingresar
                    ltotales = new ArrayList<totalesimpuestosinmuebles>();
                }//Fin del Caso en el que el inmueble tiene pagos registrados.
                else {
                    //formateando la fecha a la Fecha de Registro
                    fecharegistro = codemd5.formatearfechazerohoras(inmueble.getFecharegistro());
                    fecharegistro = codemd5.obteniendoprimerdiames(fecharegistro);
                    //Recuperando lista de impuestos inmuebles
                    limpuestosinmuebles = inmueble.getImpuestosinmuebleses();
                    //Iterando impuestos inmuebles
                    Iterator limpinmiterador = limpuestosinmuebles.iterator();
                    while (limpinmiterador.hasNext()) {
                        impinmueble = (Impuestosinmuebles) limpinmiterador.next();
                        impuesto = impinmueble.getImpuestos();
                        //Recuperando la lista de formulas y montos pertenicientes al impuesto.
                        lmontosimpuestos = impuesto.getMontosimpuestoses();
                        lformulas = impuesto.getFormulaes();
                        //Iterando la lista de formulas
                        Iterator formulasiterador = lformulas.iterator();
                        while (formulasiterador.hasNext()) {
                            xformula = (Formulas) formulasiterador.next();
                            //Recuperando la formula.
                            formula = xformula.getFormula();
                            //Comprobando si existe o no formula a Interpretar
                            if (formula.equals("0") && !xformula.getUso().equals("OBSOLETO")) {
                                //Iterando los montos correspondientes al impuesto
                                Iterator mntimpiterador = lmontosimpuestos.iterator();
                                while (mntimpiterador.hasNext()) {
                                    montoimpuesto = (Montosimpuestos) mntimpiterador.next();
                                    //Periodo de validez de la formula a cero.
                                    //inicio
                                    fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                    fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                    //fin
                                    fechafinformula = codemd5.formatearfechaultimahora(new Date());
                                    fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                    //Verificando si la fecha del monto tiene fecha de vencimiento
                                    if (montoimpuesto.getFechafin() == null) {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    } else {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(montoimpuesto.getFechafin());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    }

                                    //Agregando los montos a la lista de totales
                                    if (codemd5.compararangofechas(fechainimonto, fechafinmonto, fechainiformula, fechafinformula) && !montoimpuesto.getUso().equals("OBSOLETO")) {
                                        addtotal = new totalesimpuestosinmuebles();
                                        addtotal.setImpuesto(impuesto);
                                        addtotal.setNImpuesto(impuesto.getNombre());
                                        addtotal.setFechainicio(fechainimonto);
                                        addtotal.setFechafin(fechafinmonto);
                                        addtotal.setTotalimpuesto(montoimpuesto.getMonto());
                                        ltotales.add(addtotal);
                                    }
                                }
                            }//Fin de la Iteración cuando no hay formula a interpretar.
                            //Inicio del caso de Interpretación de formulas.
                            else if (!xformula.getUso().equals("OBSOLETO")) {
                                Iterator mntimpiterador = lmontosimpuestos.iterator();
                                while (mntimpiterador.hasNext()) {
                                    montoimpuesto = (Montosimpuestos) mntimpiterador.next();
                                    //Periodo de validez de las formulas.
                                    //Periodo Formula
                                    if (xformula.getFechafin() != null) {
                                        //inicio
                                        fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                        fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                        //fin
                                        fechafinformula = codemd5.formatearfechaultimahora(xformula.getFechafin());
                                        fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                    } else {
                                        //inicio
                                        fechainiformula = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                                        fechainiformula = codemd5.obteniendoprimerdiames(fechainiformula);
                                        //fin
                                        fechafinformula = codemd5.formatearfechaultimahora(new Date());
                                        fechafinformula = codemd5.obteniendoultimodiames(fechafinformula);
                                    }
                                    //periodo monto.
                                    if (montoimpuesto.getFechafin() == null) {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    } else {
                                        //inicio
                                        fechainimonto = codemd5.formatearfechazerohoras(montoimpuesto.getFechainicio());
                                        fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                                        //fin
                                        fechafinmonto = codemd5.formatearfechaultimahora(montoimpuesto.getFechafin());
                                        fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                                    }
                                    if (codemd5.compararangofechas(fechainimonto, fechafinmonto, fechainiformula, fechafinformula) && !montoimpuesto.getUso().equals("OBSOLETO")) {
                                        addtotal = new totalesimpuestosinmuebles();
                                        //Intepretando la formula de acuerdo al monto dado
                                        interprete.put("ML", inmueble.getMetros_lineales());
                                        interprete.put("MC", inmueble.getMetros_cuadrados());
                                        interprete.put("MONTO", montoimpuesto.getMonto());
                                        try {
                                            resultadoformula = interprete.eval(formula);
                                        } catch (Exception e) {
                                            System.out.println("Error durante la evaluación de la formula");
                                            System.out.println(e.toString());
                                            return false;
                                        }
                                        addtotal.setImpuesto(impuesto);
                                        addtotal.setNImpuesto(impuesto.getNombre());
                                        addtotal.setFechainicio(fechainimonto);
                                        addtotal.setFechafin(fechafinmonto);
                                        addtotal.setTotalimpuesto(new BigDecimal(resultadoformula.toString()).setScale(2, RoundingMode.HALF_EVEN));
                                        ltotales.add(addtotal);
                                    }
                                }
                            }
                        }//Fin de la iteración de las formulas.
                    }//Fin de la iteración de la lista de impuestos inmuebles.
                    //CALCULO DE LOS PAGOS A INGRESAR
                    //mes inicial de la fecha de registro
                    mes = codemd5.getmesregistro(fecharegistro);
                    //año de la fecha de registro.
                    anio = codemd5.getanioregistro(fecharegistro);
                    //año actual
                    anioactual = codemd5.getanioactual();
                    //Iteraciones para crear la lista de pagos
                    for (int i = anio; i <= anioactual; i++) {
                        if (i != anioactual) {
                            //Iteración por mes
                            for (int j = mes; j <= 12; j++) {
                                if (mes < 10) {
                                    periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                } else {
                                    periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                }
                                //Formateando el periodo a evaluar del primero a fin de mes
                                //inicio
                                fechainiperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                fechainiperiodo = codemd5.obteniendoprimerdiames(fechainiperiodo);
                                //fin
                                fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                //Iterando la Lista de Totales Impuestos.
                                Iterator ltotiterador = ltotales.iterator();
                                total = BigDecimal.ZERO;
                                while (ltotiterador.hasNext()) {
                                    addtotal = (totalesimpuestosinmuebles) ltotiterador.next();
                                    objetodetalle = new listadetalledepagos();
                                    if (codemd5.compararangofechas(fechainiperiodo, fechafinperiodo, addtotal.getFechainicio(), addtotal.getFechafin())) {
                                        total = total.add(addtotal.getTotalimpuesto());
                                        objetodetalle.setInmueble(inmueble);
                                        objetodetalle.setPeriodoevaluar(periodoevaluar);
                                        objetodetalle.setTotalimpuesto(addtotal);
                                        ldetallepagos.add(objetodetalle);
                                    }
                                }
                                //ingresando el pago del inmueble agregado en el presente periodo
                                try {
                                    vpg = new verpagos();
                                    vpg.setEstapagado(estado);
                                    vpg.setInmueble(inmueble);
                                    vpg.setMespagado(periodoevaluar);
                                    vpg.setMonto(total);
                                    lverpagos.add(vpg);
                                } catch (Exception e) {
                                    System.out.println("Error durante la carga inicial de pagos");
                                    System.out.println(e.toString());
                                    return false;
                                }
                                mes += 1;
                            }
                        }//Termina cuando el año es diferente del actual y comienza el caso del año actual
                        else {
                            mesfinal = codemd5.getmesregistro(new Date());
                            //Iteración por mes
                            for (int j = mes; j <= mesfinal; j++) {
                                if (mes < 10) {
                                    periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                } else {
                                    periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                }
                                //Formateando el periodo a evaluar del primero a fin de mes
                                //inicio
                                fechainiperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                fechainiperiodo = codemd5.obteniendoprimerdiames(fechainiperiodo);
                                //fin
                                fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                //Iterando la Lista de Totales Impuestos.
                                Iterator ltotiterador = ltotales.iterator();
                                total = BigDecimal.ZERO;
                                while (ltotiterador.hasNext()) {
                                    addtotal = (totalesimpuestosinmuebles) ltotiterador.next();
                                    objetodetalle = new listadetalledepagos();
                                    if (codemd5.compararangofechas(fechainiperiodo, fechafinperiodo, addtotal.getFechainicio(), addtotal.getFechafin())) {
                                        total = total.add(addtotal.getTotalimpuesto());
                                        objetodetalle.setInmueble(inmueble);
                                        objetodetalle.setPeriodoevaluar(periodoevaluar);
                                        objetodetalle.setTotalimpuesto(addtotal);
                                        ldetallepagos.add(objetodetalle);
                                    }
                                }
                                //ingresando el pago del inmueble agregado en el presente periodo
                                try {
                                    vpg = new verpagos();
                                    vpg.setEstapagado(estado);
                                    vpg.setInmueble(inmueble);
                                    vpg.setMespagado(periodoevaluar);
                                    vpg.setMonto(total);
                                    lverpagos.add(vpg);
                                } catch (Exception e) {
                                    System.out.println("Error durante la carga inicial de pagos");
                                    System.out.println(e.toString());
                                    return false;
                                }
                                mes += 1;
                            }
                        }
                        mes = 1;
                    }//Fin de la iteración de calculo de cobros a ingresar
                    ltotales = new ArrayList<totalesimpuestosinmuebles>();
                }//Fin del Caso en el que el inmueble no tiene pagos registrados.

            }//Fin de la iteración de la lista de inmuebles
            if (lverpagos.size() != 0) {
                int ncont = 0;
                try {
                    Iterator itvp = lverpagos.iterator();
                    while (itvp.hasNext()) {
                        //seteando el pago.
                        vpg = (verpagos) itvp.next();
                        if (vpg.getInmueble().getImpuestosinmuebleses().size() != 0) {
                            montototal = aplicarpgadelantado(vpg.getInmueble(), null, vpg.getMonto(), vpg.getMespagado());
                            if (montototal.compareTo(vpg.getMonto()) == 1) {
                                pago = new Pagos();
                                pago.setInmuebles(vpg.getInmueble());
                                pago.setMespagado(vpg.getMespagado());
                                pago.setMonto(vpg.getMonto());
                                pago.setMontopagado(montototal);
                                pago.setBoletas(valboleta);
                                pago.setFechapago(new Date());
                                pago.setNorecibo(valboleta.getRecibo());
                                pago.setComentario("PAGO AUTOMATICO POR PAGO ADELANTADO");
                                pago.setDescuento(0);
                                pago.setEstadosid(estcancel);
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getInmuebles().getId() == objetodetalle.getInmueble().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setImpuestos(objetodetalle.getTotalimpuesto().getImpuesto());
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            } else {
                                pago = new Pagos();
                                pago.setInmuebles(vpg.getInmueble());
                                pago.setMespagado(vpg.getMespagado());
                                pago.setMonto(vpg.getMonto());
                                pago.setMontopagado(BigDecimal.ZERO);
                                pago.setDescuento(0);
                                pago.setEstadosid(vpg.getEstapagado());
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getInmuebles().getId() == objetodetalle.getInmueble().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setImpuestos(objetodetalle.getTotalimpuesto().getImpuesto());
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            }
                            ncont += 1;
                        }

                    }
                    lverpagos = new ArrayList<verpagos>();
                    ldetallepagos = new ArrayList<listadetalledepagos>();
                    if (ncont != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("Error en la inserción de pagos a la BD");
                    System.out.println(e.toString());
                    return false;
                }
            } else {
                return false;
            }

        }//Cierre de la comprobación de la lista de inmuebles si tiene objetos o se encuentra vacia.
        else {
            System.out.println("Error en la carga de la lista de los inmuebles activos");
            return false;
        }
    }

    /*Metodo Para Generar la Carga Mensual de los Negocios*/
    public boolean generarcobrosmensualesnegocios() {
        //Declaración de Variables
        List<Negocios> lnegocios = null;
        Negocios negocio = new Negocios();
        Set<Montosimpuestosnegocios> limpuestosnegocios = null;
        Montosimpuestosnegocios mntimpnegocio = new Montosimpuestosnegocios();
        List<Pagos> lpagos = null;
        Pagos pago = new Pagos();
        List<totalesimpuestosinmuebles> ltotales = new ArrayList<totalesimpuestosinmuebles>();
        totalesimpuestosinmuebles addtotales = new totalesimpuestosinmuebles();
        List<verpagos> listadepagos = new ArrayList<verpagos>();
        verpagos addpago = new verpagos();
        Estados estado = new Estados();
        Estados estcancel = new Estados();
        //Lista contenedora de los Detalles de los pagos.
        List<listadetalledepagos> ldetallepagos = new ArrayList<listadetalledepagos>();
        listadetalledepagos objetodetalle = new listadetalledepagos();
        Detallepagos detallepago = new Detallepagos();
        Date fechainimonto, fechafinmonto, fechainicioperiodo, fechafinperiodo, fecharegistronegocio, fechaultimopago;

        int anio, mes, mesfinal, anioactual, resultado;
        String periodoevaluar;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal montototal = BigDecimal.ZERO;
        //inicializando variables estaticas
        estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        estcancel = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");

        //Cargando la lista de negocios activos.
        try {
            lnegocios = dao.findByWhereStatement(Negocios.class, " estados_id = 1");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error durante la carga de los negocios");
            return false;
        }
        //Comprobando si la lista posee negocios a iterar.
        if (lnegocios != null) {
            //Iterando la lista de negocios.
            Iterator negociositerador = lnegocios.iterator();
            while (negociositerador.hasNext()) {
                negocio = (Negocios) negociositerador.next();
                //Recogiendo el último pago del negocio.
                pago = new Pagos();
                pago = (Pagos) dao.findByWhereStatementmaximoobjeto(Pagos.class, "negocios_id = " + Integer.toString(negocio.getId()) + " and mespagado <> 'VARIOS ADELANTADO'");
                //Verificando el último pago (caso 1) o si el negocio no tiene pagos asignados hasta el momento (caso 2).
                if (pago != null) {
                    fechaultimopago = codemd5.formatearfechazerohoras(pago.getMespagado());
                    mes = codemd5.getmesregistro(fechaultimopago) + 1;
                    anio = codemd5.getanioregistro(fechaultimopago);
                    if (mes > 12) {
                        mes = 1;
                        anio += 1;
                    }
                }//fin del caso 1 (el negocio ya posee pagos registrados.)
                //Inicio del caso en el cual el negocio no posee pagos.
                else {
                    fecharegistronegocio = codemd5.formatearfechazerohoras(negocio.getFecha_registro());
                    mes = codemd5.getmesregistro(fecharegistronegocio);
                    anio = codemd5.getanioregistro(fecharegistronegocio);
                }//finn del caso 2 (el negocio no posee pagos registrado)
                anioactual = codemd5.getanioactual();
                //lista de montos correspondiente al negocio
                limpuestosnegocios = negocio.getMntimpuestoes();
                //Comprobando que la lista de impuestos no se encuentre vacia
                if (limpuestosnegocios != null) {
                    Iterator limpnegiterador = limpuestosnegocios.iterator();
                    //Iterando la lista de impuestos negocios
                    while (limpnegiterador.hasNext()) {
                        //recuperando el monto impuesto
                        mntimpnegocio = (Montosimpuestosnegocios) limpnegiterador.next();
                        //verificando si la fecha fin se encuentra llena y formateando las fechas
                        if (mntimpnegocio.getFechafin() != null) {
                            //inicio
                            fechainimonto = codemd5.formatearfechazerohoras(mntimpnegocio.getFechainicio());
                            fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                            //fin
                            fechafinmonto = codemd5.formatearfechaultimahora(mntimpnegocio.getFechafin());
                            fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                        } else {
                            //inicio
                            fechainimonto = codemd5.formatearfechazerohoras(mntimpnegocio.getFechainicio());
                            fechainimonto = codemd5.obteniendoprimerdiames(fechainimonto);
                            //fin
                            fechafinmonto = codemd5.formatearfechaultimahora(new Date());
                            fechafinmonto = codemd5.obteniendoultimodiames(fechafinmonto);
                        }
                        //Agregando los montos impuestos del negocio
                        if (!mntimpnegocio.getUso().equals("OBSOLETO")) {
                            addtotales = new totalesimpuestosinmuebles();
                            addtotales.setNImpuesto(Integer.toString(mntimpnegocio.getNegocios().getId()));
                            addtotales.setFechainicio(fechainimonto);
                            addtotales.setFechafin(fechafinmonto);
                            addtotales.setTotalimpuesto(mntimpnegocio.getMonto());
                            ltotales.add(addtotales);
                        }
                        //Fin de la Iteración de los montos correspondientes a los impuestos del negocio.
                    }
                    //Calculando los pagos de los negocios
                    for (int i = anio; i <= anioactual; i++) {
                        if (i != anioactual) {
                            //iterando por mes
                            for (int j = mes; j <= 12; j++) {
                                if (mes < 10) {
                                    periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                } else {
                                    periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                }
                                //obteniendo del 1 al fin de cada mes
                                //inicio
                                fechainicioperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                fechainicioperiodo = codemd5.obteniendoprimerdiames(fechainicioperiodo);
                                //fin
                                fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                //Iterando los montos impuestos totales.
                                Iterator listatotales = ltotales.iterator();
                                total = BigDecimal.ZERO;
                                while (listatotales.hasNext()) {
                                    addtotales = (totalesimpuestosinmuebles) listatotales.next();
                                    objetodetalle = new listadetalledepagos();
                                    if (codemd5.compararangofechas(fechainicioperiodo, fechafinperiodo, addtotales.getFechainicio(), addtotales.getFechafin())) {
                                        total = total.add(addtotales.getTotalimpuesto());
                                        objetodetalle.setNegocio(negocio);
                                        objetodetalle.setPeriodoevaluar(periodoevaluar);
                                        objetodetalle.setTotalimpuesto(addtotales);
                                        ldetallepagos.add(objetodetalle);
                                    }
                                }
                                try {
                                    addpago = new verpagos();
                                    addpago.setEstapagado(estado);
                                    addpago.setNegocio(negocio);
                                    addpago.setMespagado(periodoevaluar);
                                    addpago.setMonto(total);
                                    listadepagos.add(addpago);
                                } catch (Exception e) {
                                    System.out.println("Error durante la carga inicial de pagos");
                                    System.out.println(e.toString());
                                    return false;
                                }
                                mes += 1;
                            }
                        } else {
                            mesfinal = codemd5.getmesregistro(new Date());
                            for (int j = mes; j <= mesfinal; j++) {
                                if (mes < 10) {
                                    periodoevaluar = "0" + Integer.toString(mes) + "-" + Integer.toString(i);
                                } else {
                                    periodoevaluar = Integer.toString(mes) + "-" + Integer.toString(i);
                                }
                                //obteniendo del 1 al fin de cada mes
                                //inicio
                                fechainicioperiodo = codemd5.formatearfechazerohoras(periodoevaluar);
                                fechainicioperiodo = codemd5.obteniendoprimerdiames(fechainicioperiodo);
                                //fin
                                fechafinperiodo = codemd5.formatearfechaultimahora(periodoevaluar);
                                fechafinperiodo = codemd5.obteniendoultimodiames(fechafinperiodo);
                                //Iterando los montos impuestos totales.
                                Iterator listatotales = ltotales.iterator();
                                total = BigDecimal.ZERO;
                                while (listatotales.hasNext()) {
                                    addtotales = (totalesimpuestosinmuebles) listatotales.next();
                                    objetodetalle = new listadetalledepagos();
                                    if (codemd5.compararangofechas(fechainicioperiodo, fechafinperiodo, addtotales.getFechainicio(), addtotales.getFechafin())) {
                                        total = total.add(addtotales.getTotalimpuesto());
                                        objetodetalle.setNegocio(negocio);
                                        objetodetalle.setPeriodoevaluar(periodoevaluar);
                                        objetodetalle.setTotalimpuesto(addtotales);
                                        ldetallepagos.add(objetodetalle);
                                    }
                                }
                                try {
                                    addpago = new verpagos();
                                    addpago.setEstapagado(estado);
                                    addpago.setNegocio(negocio);
                                    addpago.setMespagado(periodoevaluar);
                                    addpago.setMonto(total);
                                    listadepagos.add(addpago);
                                } catch (Exception e) {
                                    System.out.println("Error durante la carga inicial de pagos");
                                    System.out.println(e.toString());
                                    return false;
                                }
                                mes += 1;
                            }
                        }
                        mes = 1;
                        //Finaliza la iteración de los anios.
                    }
                    ltotales = new ArrayList<totalesimpuestosinmuebles>();
                }//Fin de la iteración de los impuestos del negocio.                
            }//Fin de la iteración de la lista de negocios.
            if (listadepagos.size() != 0) {
                int ncont = 0;
                try {
                    Iterator itvp = listadepagos.iterator();
                    while (itvp.hasNext()) {
                        //seteando el pago.
                        addpago = (verpagos) itvp.next();
                        if (addpago.getNegocio().getMntimpuestoes().size() != 0) {
                            montototal = aplicarpgadelantado(null, addpago.getNegocio(), addpago.getMonto(), addpago.getMespagado());
                            if (montototal.compareTo(addpago.getMonto()) == 1) {
                                pago = new Pagos();
                                pago.setNegocios(addpago.getNegocio());
                                pago.setMespagado(addpago.getMespagado());
                                pago.setMonto(addpago.getMonto());
                                pago.setMontopagado(montototal);
                                pago.setBoletas(valboleta);
                                pago.setNorecibo(valboleta.getRecibo());
                                pago.setFechapago(new Date());
                                pago.setDescuento(0);
                                pago.setComentario("PAGO AUTOMATICO POR PAGO ADELANTADO");
                                pago.setEstadosid(estcancel);
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getNegocios().getId() == objetodetalle.getNegocio().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            } else {
                                pago = new Pagos();
                                pago.setNegocios(addpago.getNegocio());
                                pago.setMespagado(addpago.getMespagado());
                                pago.setMonto(addpago.getMonto());
                                pago.setMontopagado(BigDecimal.ZERO);
                                pago.setDescuento(0);
                                pago.setEstadosid(addpago.getEstapagado());
                                //Guardando
                                dao.save(pago);
                                //Iterando la lista de detalles para guardar el respectivo detalle del pago.
                                Iterator dtliterador = ldetallepagos.iterator();
                                while (dtliterador.hasNext()) {
                                    objetodetalle = (listadetalledepagos) dtliterador.next();
                                    if (pago.getMespagado().equals(objetodetalle.getPeriodoevaluar()) && pago.getNegocios().getId() == objetodetalle.getNegocio().getId()) {
                                        detallepago = new Detallepagos();
                                        detallepago.setMonto_impuesto(objetodetalle.getTotalimpuesto().getTotalimpuesto());
                                        detallepago.setPagos(pago);
                                        dao.save(detallepago);
                                    }
                                }
                            }
                            ncont += 1;
                        }
                    }
                    listadepagos = new ArrayList<verpagos>();
                    ldetallepagos = new ArrayList<listadetalledepagos>();
                    if (ncont != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("Error en la inserción de pagos a la BD");
                    System.out.println(e.toString());
                    return false;
                }
            } else {
                return false;
                //cuando se convierta en booleana retorna falso en este bloque
            }
        }//Fin comprobando si la lista de negocios no esta vacia.
        else {
            System.out.println("No se encontraron negocios activos");
            return false;
        }
    }

    /*Metodo utilizado para la aplicacion de pagos adelantados de inmuebles o negocios*/
    public BigDecimal aplicarpgadelantado(Inmuebles inmueble, Negocios negocio, BigDecimal Monto, String periodo) {
        if (inmueble != null) {
            BigDecimal mntotal = BigDecimal.ZERO;
            BigDecimal tfiesta = BigDecimal.ZERO;
            Impuestos fiesta;
            Set<Formulas> forfiesta = null;
            Formulas xformula = new Formulas();
            Date fechaforini, fechaforfin, fechaperini, fechaperfin;
            //Variables para interpretar formulas
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine interprete = manager.getEngineByName("js");
            String formula;
            Object resultadoformula = new Object();
            //Variables para verificar si es posible aplicar el pago adelantado.
            List<Pagosadelantados> listapagosadelantados = null;
            Pagosadelantados pgadel = new Pagosadelantados();
            BigDecimal Saldo, tmpsaldo, res;
            //Formateando fechas correspondientes a los periodos.
            //ini
            fechaperini = codemd5.formatearfechazerohoras(periodo);
            fechaperini = codemd5.obteniendoprimerdiames(fechaperini);
            //fin
            fechaperfin = codemd5.formatearfechaultimahora(periodo);
            fechaperfin = codemd5.obteniendoultimodiames(fechaperfin);
            //Recuperando la fiesta y sus impuestos
            fiesta = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, " nombre = 'FIESTA'");
            forfiesta = fiesta.getFormulaes();
            if (forfiesta != null) {
                Iterator iteradorformua = forfiesta.iterator();
                while (iteradorformua.hasNext()) {
                    xformula = (Formulas) iteradorformua.next();
                    formula = xformula.getFormula();
                    if (xformula.getFechafin() != null) {
                        //inicio
                        fechaforini = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                        fechaforini = codemd5.obteniendoprimerdiames(fechaforini);
                        //fin
                        fechaforfin = codemd5.formatearfechaultimahora(xformula.getFechafin());
                        fechaforfin = codemd5.obteniendoultimodiames(fechaforfin);
                    } else {
                        //inicio
                        fechaforini = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                        fechaforini = codemd5.obteniendoprimerdiames(fechaforini);
                        //fin
                        fechaforfin = codemd5.formatearfechaultimahora(new Date());
                        fechaforfin = codemd5.obteniendoultimodiames(fechaforfin);
                    }
                    if (codemd5.compararangofechas(fechaperini, fechaperfin, fechaforini, fechaforfin)) {
                        //Intepretando la formula de acuerdo al monto dado
                        interprete.put("MONTO", Monto);
                        try {
                            resultadoformula = interprete.eval(formula);
                        } catch (Exception e) {
                            System.out.println("Error durante la evaluación de la formula");
                            System.out.println(e.toString());
                        }
                        tfiesta = new BigDecimal(resultadoformula.toString()).setScale(2, RoundingMode.HALF_EVEN);
                    }
                }
            }
            mntotal = mntotal.add(Monto);
            mntotal = mntotal.add(tfiesta);
            //Obteniendo y Modificando lista de pagos adelantados.
            listapagosadelantados = dao.findByWhereStatement(Pagosadelantados.class, " inmuebles_id = " + Integer.toString(inmueble.getId()) + " and saldo > 0");
            Saldo = BigDecimal.ZERO;
            tmpsaldo = BigDecimal.ZERO;
            if (listapagosadelantados.size() != 0) {
                Iterator iteradorpagosadelantados = listapagosadelantados.iterator();
                while (iteradorpagosadelantados.hasNext()) {
                    pgadel = (Pagosadelantados) iteradorpagosadelantados.next();
                    Saldo = pgadel.getSaldo();
                    Saldo = Saldo.add(tmpsaldo);
                    if (Saldo.compareTo(mntotal) == 1 || Saldo.compareTo(mntotal) == 0) {
                        Saldo = Saldo.subtract(mntotal);
                        pgadel.setSaldo(Saldo);
                        valboleta = (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "id = " + pgadel.getBoleta().getId());
                        dao.update(pgadel);
                        return mntotal;
                    } else if (!(iteradorpagosadelantados.hasNext())) {
                        tmpsaldo = tmpsaldo.add(pgadel.getSaldo());
                        pgadel.setSaldo(tmpsaldo);
                        dao.update(pgadel);
                        return Monto;

                    } else {
                        tmpsaldo = tmpsaldo.add(pgadel.getSaldo());
                        res = pgadel.getSaldo();
                        res = res.subtract(res);
                        pgadel.setSaldo(res);
                        dao.update(pgadel);
                        //dejando a cero el pago adelantado anterior.
                    }
                }
            }
            return Monto;
        }//Fin de Calculo de Monto Total para inmueble
        else if (negocio != null) {
            BigDecimal mntotal = BigDecimal.ZERO;
            BigDecimal tfiesta = BigDecimal.ZERO;
            Impuestos fiesta;
            Set<Formulas> forfiesta = null;
            Formulas xformula = new Formulas();
            Date fechaforini, fechaforfin, fechaperini, fechaperfin;
            //Variables para interpretar formulas
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine interprete = manager.getEngineByName("js");
            String formula;
            Object resultadoformula = new Object();
            //Variables para verificar si es posible aplicar el pago adelantado.
            List<Pagosadelantados> listapagosadelantados = null;
            Pagosadelantados pgadel = new Pagosadelantados();
            BigDecimal Saldo, tmpsaldo, res;
            //Formateando fechas correspondientes a los periodos.
            //ini
            fechaperini = codemd5.formatearfechazerohoras(periodo);
            fechaperini = codemd5.obteniendoprimerdiames(fechaperini);
            //fin
            fechaperfin = codemd5.formatearfechaultimahora(periodo);
            fechaperfin = codemd5.obteniendoultimodiames(fechaperfin);
            //Recuperando la fiesta y sus impuestos
            fiesta = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, " nombre = 'FIESTA'");
            forfiesta = fiesta.getFormulaes();
            if (forfiesta != null) {
                Iterator iteradorformua = forfiesta.iterator();
                while (iteradorformua.hasNext()) {
                    xformula = (Formulas) iteradorformua.next();
                    formula = xformula.getFormula();
                    if (xformula.getFechafin() != null) {
                        //inicio
                        fechaforini = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                        fechaforini = codemd5.obteniendoprimerdiames(fechaforini);
                        //fin
                        fechaforfin = codemd5.formatearfechaultimahora(xformula.getFechafin());
                        fechaforfin = codemd5.obteniendoultimodiames(fechaforfin);
                    } else {
                        //inicio
                        fechaforini = codemd5.formatearfechazerohoras(xformula.getFechainicio());
                        fechaforini = codemd5.obteniendoprimerdiames(fechaforini);
                        //fin
                        fechaforfin = codemd5.formatearfechaultimahora(new Date());
                        fechaforfin = codemd5.obteniendoultimodiames(fechaforfin);
                    }
                    if (codemd5.compararangofechas(fechaperini, fechaperfin, fechaforini, fechaforfin)) {
                        //Intepretando la formula de acuerdo al monto dado
                        interprete.put("MONTO", Monto);
                        try {
                            resultadoformula = interprete.eval(formula);
                        } catch (Exception e) {
                            System.out.println("Error durante la evaluación de la formula");
                            System.out.println(e.toString());
                        }
                        tfiesta = new BigDecimal(resultadoformula.toString()).setScale(2, RoundingMode.HALF_EVEN);
                    }
                }
            }
            mntotal = mntotal.add(Monto);
            mntotal = mntotal.add(tfiesta);
            //Obteniendo y Modificando lista de pagos adelantados.
            listapagosadelantados = dao.findByWhereStatement(Pagosadelantados.class, " negocios_id = " + Integer.toString(negocio.getId()) + " and saldo > 0");
            Saldo = BigDecimal.ZERO;
            tmpsaldo = BigDecimal.ZERO;
            if (listapagosadelantados.size() != 0) {
                Iterator iteradorpagosadelantados = listapagosadelantados.iterator();
                while (iteradorpagosadelantados.hasNext()) {
                    pgadel = (Pagosadelantados) iteradorpagosadelantados.next();
                    Saldo = pgadel.getSaldo();
                    Saldo = Saldo.add(tmpsaldo);
                    if (Saldo.compareTo(mntotal) == 1 || Saldo.compareTo(mntotal) == 0) {
                        Saldo = Saldo.subtract(mntotal);
                        pgadel.setSaldo(Saldo);
                        valboleta = (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "id = " + pgadel.getBoleta().getId());
                        dao.update(pgadel);
                        return mntotal;
                    } else if (!(iteradorpagosadelantados.hasNext())) {
                        tmpsaldo = tmpsaldo.add(pgadel.getSaldo());
                        pgadel.setSaldo(tmpsaldo);
                        dao.update(pgadel);
                        return Monto;

                    } else {
                        tmpsaldo = tmpsaldo.add(pgadel.getSaldo());
                        res = pgadel.getSaldo();
                        res = res.subtract(res);
                        pgadel.setSaldo(res);
                        dao.update(pgadel);
                        //dejando a cero el pago adelantado anterior.
                    }
                }
            }
            return Monto;
        }//Fin de Calculo de Monto Total para Negocios.
        else {
            System.out.print("No se encontraron inmuebles ni negocios");
            return Monto;
        }//Negocio e Inmuebles igual a null                        
    }
}
