/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.PgDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;
import peralcaldia.model.Boleta;
import peralcaldia.model.Estados;
import peralcaldia.model.Formulas;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Impuestosinmuebles;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Montosimpuestos;
import peralcaldia.model.Montosimpuestosnegocios;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;

/**
 *
 * @author alex
 */
/*Clase utilizada para la generacion de los diferentes tipos de boleta manejados en el sistema*/
public class GenerarBoletaRepository {

    PgDAO dao = new PgDAO();

    /*Metodo para Generar las boletas de pago para Inmuebles*/
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
            pago = (Pagos) pagosIterator.next();
            if (pago.getBoletas() != null) {
                JOptionPane.showMessageDialog(null, "Parte de la seleccion de pagos han sido incluidos en la boleta numero: " + pago.getBoletas().getId());
                //reak;
                return null;
            }
            if (x < meses) {
                //pago = (Pagos) pagosIterator.next();
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

    /*Metodo para calcular la mora en caso de que esta aplique*/
    public double CalcularMora(String Fechapago, long NMeses, BigDecimal montoPago) {
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

    /*Metodo para calcular la fiesta en caso de que aplique*/
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

    /*Metodo para Generar las boletas de pago para Negocios*/
    public Boleta BoletaNegocio(Negocios negocioId, int meses, boolean cobrarMora) {
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
                if (pago.getBoletas() != null) {
                    JOptionPane.showMessageDialog(null, "No se pueden incluir pagos que ya han sido incluidos en otra boleta con numero: " + pago.getBoletas().getId());
                    //reak;
                    return null;
                }
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

    /*Metodo para Generar las boletas de pago adelantado para Inmuebles*/
    public Boleta BoletaPagosAdelantadosInmueble(int nPagos, Inmuebles inmueble) {
        Set<Pagos> pags = inmueble.getPagoses();
        Iterator iPags = pags.iterator();
        boolean isSolvent = true;
        Pagos p = null;
        while (iPags.hasNext()) {
            p = (Pagos) iPags.next();
            if (p.getEstadosid().getId() == 6) {
                isSolvent = false;
                break;
            }
        }
        if (isSolvent) {
            Set<Impuestosinmuebles> impuestos = inmueble.getImpuestosinmuebleses();
            Iterator impuestosIterator = impuestos.iterator();
            Formulas formula = null;
            Impuestosinmuebles impuestoInmueble = null;
            Montosimpuestos monto = null;
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine interprete = manager.getEngineByName("js");
            String formulaText = "";
            Double resultado = 0.0;
            while (impuestosIterator.hasNext()) {
                impuestoInmueble = (Impuestosinmuebles) impuestosIterator.next();
                formula = (Formulas) dao.findByWhereStatementmaximoobjeto(Formulas.class, "impuestos_id = " + impuestoInmueble.getImpuestos().getId() + " and uso <> 'OBSOLETO'");
                monto = (Montosimpuestos) dao.findByWhereStatementmaximoobjeto(Montosimpuestos.class, "impuestos_id = " + impuestoInmueble.getImpuestos().getId() + " and  uso <> 'OBSOLETO'");
                formulaText = formula.getFormula();
                if (!formulaText.equals("0")) {
                    interprete.put("MONTO", monto.getMonto().doubleValue());
                    interprete.put("MC", inmueble.getMetros_cuadrados().doubleValue());
                    interprete.put("ML", inmueble.getMetros_lineales().doubleValue());
                    try {
                        resultado += Double.parseDouble(interprete.eval(formulaText).toString());
                    } catch (ScriptException ex) {
                        Logger.getLogger(GenerarBoletaRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    resultado += monto.getMonto().doubleValue();
                }

            }
            resultado += this.CalcularFiestas(resultado, new Date().toString());
            Boleta boleta = new Boleta();
            Estados estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "Id = 6");
            boleta.setInmuebles(inmueble);
            boleta.setMontototal(BigDecimal.valueOf(resultado * 5));
            boleta.setEstados(estado);
            boleta.setMesesapagar(nPagos);
            dao.save(boleta);
            Pagos pago = new Pagos();
            pago.setBoletas(boleta);
            pago.setMespagado("VARIOS ADELANTADO");
            pago.setEstadosid(estado);
            pago.setInmuebles(inmueble);
            pago.setMonto(BigDecimal.valueOf(resultado * 5));
            dao.save(pago);
            return boleta;
        } else {
            JOptionPane.showMessageDialog(null, "El inmueble se encuentra en mora, favor cancelar pagos pendientes.");
            return null;
        }
    }

    /*Metodo para Generar las boletas de pago adelantados para Negocios*/
    public Boleta BoletaPagosAdelantadosNegocio(int nPagos, Negocios negocio) {
        Set<Pagos> pags = negocio.getPagoes();
        Iterator iPags = pags.iterator();
        boolean isSolvent = true;
        Pagos p = null;
        while (iPags.hasNext()) {
            p = (Pagos) iPags.next();
            if (p.getEstadosid().getId() == 6) {
                isSolvent = false;
                break;
            }
        }
        if(isSolvent)
        {
             Montosimpuestosnegocios monto = (Montosimpuestosnegocios) dao.findByWhereStatementmaximoobjeto(Montosimpuestosnegocios.class, "negocios_id = " + negocio.getId() + " and  uso <> 'OBSOLETO'");
            Double resultado = monto.getMonto().doubleValue() + this.CalcularFiestas(monto.getMonto().doubleValue(), new Date().toString());
            Boleta boleta = new Boleta();
            Estados estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "Id = 6");
            boleta.setNegocios(negocio);
            boleta.setMontototal(BigDecimal.valueOf(resultado * nPagos));
            boleta.setEstados(estado);
            boleta.setMesesapagar(nPagos);
            dao.save(boleta);
            Pagos pago = new Pagos();
            pago.setBoletas(boleta);
            pago.setMespagado("VARIOS ADELANTADO");
            pago.setEstadosid(estado);
            pago.setNegocios(negocio);
            pago.setMonto(BigDecimal.valueOf(resultado * nPagos));
            dao.save(pago);
            return boleta;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "El inmueble se encuentra en mora, favor cancelar pagos pendientes.");
            return null;
        }       
    }

    /*Metodo para anular una boleta en especifico*/
    public void AnularBoleta(int boletaId) {
        Boleta boleta = (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "Id = " + boletaId);
        Set<Pagos> pagos = boleta.getPagoes();
        Iterator pagosIterator = pagos.iterator();
        Estados estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "Id = 6");
        Pagos pago = null;
        while (pagosIterator.hasNext()) {
            pago = (Pagos) pagosIterator.next();
            if (pago.getMespagado().equals("VARIOS ADELANTADO")) {
                dao.delete(pago);
            } else {
                pago.setBoletas(null);
                pago.setMontopagado(null);
                pago.setFechapago(null);
                pago.setComentario(null);
                pago.setNorecibo(null);
                pago.setEstadosid(estado);
                dao.update(dao);
            }
        }
        dao.delete(boleta);
    }

    /*Metodo para borrar boletas no pagadas*/
    public void BorrarBoletasNoPagadas() {
        List<Boleta> Boletas = dao.findByWhereStatement(Boleta.class, "estados_id = 6");
        Iterator boletasIterator = Boletas.iterator();
        int x = 0;
        while (boletasIterator.hasNext()) {
            this.AnularBoleta(((Boleta) boletasIterator.next()).getId());
            x++;
        }
        JOptionPane.showMessageDialog(null, "Se eliminaron: " + x + " boletas.");
    }
}
