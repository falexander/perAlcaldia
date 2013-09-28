/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Montosimpuestos;
import peralcaldia.model.Pagos;

/**
 *
 * @author alex
 */
public class GenerarBoletaRepository 
{
    AbstractDAO dao = new AbstractDAO();
    public void BoletaInmueble(int inmuebleId,int meses,boolean cobrarMora)
    {        
        Inmuebles inmueble = new Inmuebles();
        inmueble= (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class , "id = "+inmuebleId);
        Boleta boleta = new Boleta();
        List<Pagos> pagos = dao.findByWhereStatement(Pagos.class, "inmuebles_id = "+inmuebleId +" And estados_id = 6");
        int NMeses = pagos.size();        
        boleta.setInmuebles(inmueble);
        Iterator pagosIterator = pagos.iterator();
        Double Mora=0.0;        
        int x =0;
        BigDecimal monto = BigDecimal.ZERO;
        Estados estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 6");
        boleta.setEstados(estado);
        boleta.setMontototal(monto);
        boleta.setMesesapagar(meses);
        dao.save(boleta);
        Pagos pago=null;
        while(pagosIterator.hasNext())
        {
            if(x<meses)
            {               
               pago=(Pagos)pagosIterator.next();
               if(cobrarMora)
               {
                   Mora += this.CalcularMora(pago.getMespagado(), NMeses,pago.getMonto());
               }
               pago.setBoletas(boleta);
               dao.update(pago);               
               monto = monto.add(pago.getMonto());
               x++;
            }
            else {
                break;
            }
        }
        double cc= Mora; 
        boleta.setMontototal(monto);
        dao.update(boleta);               
    }
    
    private double CalcularMora(String Fechapago,int NMeses,BigDecimal montoPago) 
    {
        Double res =0.0;
        Impuestos impuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, "nombre = 'MORA'");
        Formulas formula = null;
        Iterator formulaIterator= impuesto.getFormulaes().iterator();
        Date FechaPago=codemd5.formatearfechazerohoras(Fechapago);
        Date FechaInicio = null;
        Date FechaFin=null;        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine interprete = manager.getEngineByName("js");        
        String formulaText="";
        Montosimpuestos monto =null;       
        Date FechaInicioMonto=null;
        Date FechaFinMonto=null;
        Object resultado=new Object();
        while(formulaIterator.hasNext())
        {
            formula=(Formulas) formulaIterator.next();
            FechaInicio = codemd5.formatearfechazerohoras(formula.getFechainicio());
            if(formula.getFechafin()!=null)
            {
                FechaFin = codemd5.formatearfechazerohoras(formula.getFechafin());
                if(FechaPago.before(FechaFin) && FechaPago.after(FechaInicio))
                {
                    try {
                        formulaText = formula.getFormula();
                        interprete.put("MONTO", montoPago.doubleValue());
                        interprete.put("NM", NMeses);
                        resultado= interprete.eval(formulaText);
                        JOptionPane.showMessageDialog(null, resultado.toString());
                        //res = Double.parseDouble(.toString());
                       
                    } catch (ScriptException ex) {
                        Logger.getLogger(GenerarBoletaRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }
            else
                {
                    formulaText = formula.getFormula();
                    interprete.put("MONTO", montoPago.doubleValue());
                    interprete.put("NM", NMeses);
                    try {
                        resultado= interprete.eval(formulaText);
                    } catch (ScriptException ex) {
                        Logger.getLogger(GenerarBoletaRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(null, resultado.toString());
                        //res =  Double.parseDouble(interprete.eval(formulaText).toString());                        
                }
        }
        return res;        
    }
}
