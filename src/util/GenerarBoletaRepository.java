/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import controller.PgDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import peralcaldia.model.Boleta;
import peralcaldia.model.Estados;
import peralcaldia.model.Formulas;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;

/**
 *
 * @author alex
 */
public class GenerarBoletaRepository {

    PgDAO dao = new PgDAO();

    public Boleta BoletaInmueble(Inmuebles inmuebleId, int meses, boolean cobrarMora) {
        Boleta boleta = new Boleta();
        List<Pagos> pagos = dao.findByWhereStatement(Pagos.class, "inmuebles_id = " + inmuebleId.getId() + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
        Date fechaInicioDeuda = null;
        Date fechaFinDeuda = null;
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
        long tiempoMora = 0;
        double Fiesta = 0.0;
        if (pagos.size() > 0) {
            fechaInicioDeuda = codemd5.formatearfechazerohoras(pagos.get(0).getMespagado());
            fechaFinDeuda = codemd5.formatearfechaultimahora(pagos.get(pagos.size() - 1).getMespagado());
            tiempoMora = (((fechaFinDeuda.getTime() - fechaInicioDeuda.getTime()) / MILLSECS_PER_DAY) / 30) - 1;
        }
        boleta.setInmuebles(inmuebleId);
        Iterator pagosIterator = pagos.iterator();
        Double Mora = 0.0;
        int contador = 0;
        int x = 0;
        BigDecimal monto = BigDecimal.ZERO;
        Estados estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        boleta.setEstados(estado);
        boleta.setMontototal(monto);
        boleta.setMesesapagar(meses);
        dao.save(boleta);
        Pagos pago = null;
        while (pagosIterator.hasNext()) {
            if (x < meses) {
                pago = (Pagos) pagosIterator.next();
                if (cobrarMora && contador == 0) {
                    Mora = this.CalcularMora(pago.getMespagado(), tiempoMora, pago.getMonto());
                    contador++;
                }
                Fiesta += this.CalcularFiestas(pago.getMonto().doubleValue(), pago.getMespagado());
                pago.setBoletas(boleta);
                dao.update(pago);
                monto = monto.add(pago.getMonto());
                x++;
            } else {
                break;
            }
        }
        monto = monto.add(BigDecimal.valueOf(Mora));
        monto = monto.add(BigDecimal.valueOf(Fiesta));
        boleta.setMontototal(monto);
        dao.update(boleta);
        return boleta;
    }

    private double CalcularMora(String Fechapago, long NMeses, BigDecimal montoPago) {
        Double res = 0.0;
        Impuestos impuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, "nombre = 'MORA'");
        Formulas formula = null;
        Iterator formulaIterator = impuesto.getFormulaes().iterator();
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");
        String formulaText = "";
        Object resultado = new Object();
        while (formulaIterator.hasNext()) {
            formula = (Formulas) formulaIterator.next();
            //Calculo de mora solo para formula actual
            if (formula.getFechafin() == null) {
                formulaText = formula.getFormula();
                interprete.put("MONTO", montoPago.doubleValue());
                interprete.put("NM", NMeses);
                try {
                    resultado = interprete.eval(formulaText);
                    res = Double.parseDouble(resultado.toString());
                } catch (ScriptException ex) {
                    Logger.getLogger(GenerarBoletaRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;               
            } 
        }
        return res;
    }

    public Double CalcularFiestas(Double monto, String Fechapago) {
        double res = 0.0;
        Impuestos impuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, "nombre = 'FIESTA'");
        Formulas formula = null;
        Iterator formulaIterator = impuesto.getFormulaes().iterator();
        Date FechaInicio = null;
        Date FechaFin = null;
        Date FechaPago = codemd5.formatearfechazerohoras(Fechapago);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");
        String formulaText = "";
        Object resultado = new Object();
        while (formulaIterator.hasNext()) {
            formula = (Formulas) formulaIterator.next();
            FechaInicio = codemd5.formatearfechazerohoras(formula.getFechainicio());
            if (formula.getFechafin() != null) {
                FechaFin = codemd5.formatearfechazerohoras(formula.getFechafin());
                if (FechaPago.before(FechaFin) && FechaPago.after(FechaInicio)) {
                    try {
                        interprete.put("MONTO", monto.toString());
                        formulaText = formula.getFormula();
                        resultado = interprete.eval(formulaText);
                        res = Double.parseDouble(resultado.toString());
                        break;

                    } catch (ScriptException ex) {
                        Logger.getLogger(GenerarBoletaRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                formulaText = formula.getFormula();
                interprete.put("MONTO", monto.toString());
                try {
                    resultado = interprete.eval(formulaText);
                    res = Double.parseDouble(resultado.toString());
                } catch (ScriptException ex) {
                    Logger.getLogger(GenerarBoletaRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        return res;
    }

    public Boleta BoletaNegocio(Negocios negocioId, int meses, boolean cobrarMora) {
//        Negocios negocio = new Negocios();
//        negocio= (Negocios) dao.findByWhereStatementoneobj(Negocios.class , "id = "+negocioId);
        Boleta boleta = new Boleta();
        List<Pagos> pagos = dao.findByWhereStatement(Pagos.class, "negocios_id = " + negocioId.getId() + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
        Date fechaInicioDeuda = null;
        Date fechaFinDeuda = null;
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
        long tiempoMora = 0;
        double Fiesta = 0.0;
        if (pagos.size() > 0) {
            fechaInicioDeuda = codemd5.formatearfechazerohoras(pagos.get(0).getMespagado());
            fechaFinDeuda = codemd5.formatearfechaultimahora(pagos.get(pagos.size() - 1).getMespagado());
            tiempoMora = (((fechaFinDeuda.getTime() - fechaInicioDeuda.getTime()) / MILLSECS_PER_DAY) / 30) - 1;
        }
        boleta.setNegocios(negocioId);
        Iterator pagosIterator = pagos.iterator();
        Double Mora = 0.0;
        int contador = 0;
        int x = 0;
        BigDecimal monto = BigDecimal.ZERO;
        Estados estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        boleta.setEstados(estado);
        boleta.setMontototal(monto);
        boleta.setMesesapagar(meses);
        dao.save(boleta);
        Pagos pago = null;
        while (pagosIterator.hasNext()) {
            if (x < meses) {
                pago = (Pagos) pagosIterator.next();
                if (cobrarMora && contador == 0) {
                    Mora = this.CalcularMora(pago.getMespagado(), tiempoMora, pago.getMonto());
                    contador++;
                }
                Fiesta += this.CalcularFiestas(pago.getMonto().doubleValue(), pago.getMespagado());
                pago.setBoletas(boleta);
                dao.update(pago);
                monto = monto.add(pago.getMonto());
                x++;
            } else {
                break;
            }
        }
        monto = monto.add(BigDecimal.valueOf(Mora));
        monto = monto.add(BigDecimal.valueOf(Fiesta));
        boleta.setMontototal(monto);
        dao.update(boleta);
        return boleta;
    }
}
