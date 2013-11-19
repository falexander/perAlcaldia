/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.PgDAO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import peralcaldia.SLorenzoParent;

/**
 *
 * @author alex
 */
/*Clase utilizada para generar los diferentes tipos de solvencias generados por el sistema*/
public class SolvenciasRepository {

    PgDAO dao = new PgDAO();
    JasperPrint jasperprint = new JasperPrint();
    InputStream reportStream;

    /*Metodo para determinar si un contribuyente esta solvente con la municipalidad*/
    public void IsSolvent(Contribuyentes contribuyente) {
        Map data = new HashMap();
        boolean res = true;
        Set<Inmuebles> inmuebles = contribuyente.getInmuebleses();
        Set<Negocios> negocios = contribuyente.getNegocioses();
        Iterator inmueblesIterator = inmuebles.iterator();
        Iterator negociosIterator = negocios.iterator();
        Inmuebles inmueble = null;
        Negocios negocio = null;
        List<Pagos> pagos = null;
        Map<String, String> inmueblesMora = new HashMap<String, String>();
        Map<String, String> negociosMora = new HashMap<String, String>();
        while (inmueblesIterator.hasNext()) {
            inmueble = (Inmuebles) inmueblesIterator.next();
            pagos = dao.fin_all_pagos_whereStatement("inmuebles_id = " + inmueble.getId() + " And estados_id = 6");
            if (pagos.size() > 0) {
                inmueblesMora.put("Inmueble: " + inmueble.getDireccion(), "debe: " + pagos.size());
                res = false;
            }
        }
        while (negociosIterator.hasNext()) {
            negocio = (Negocios) negociosIterator.next();
            pagos = dao.fin_all_pagos_whereStatement("negocios_id = " + negocio.getId() + " And estados_id = 6");
            if (pagos.size() > 0) {
                negociosMora.put("Negocio: " + negocio.getNombreempresa(), "debe: " + pagos.size());
                res = false;
            }
        }
        if (!res) {
            JOptionPane.showMessageDialog(null, "Contribuyente en mora, " + inmueblesMora.size() + " inmuebles en mora, " + negociosMora.size() + " negocios en mora.");
        } else {
            try {
                String reporteLocation = "src/Reportes/report11c.jrxml";
                Map<String, Object> parameters = new HashMap();
                JRViewer reportPane;
                //reportStream = SLorenzoParent.class.getResourceAsStream("/Reportes/report11c.jasper");

                parameters.put("nombre", contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                parameters.put("inmueble", " ");
                parameters.put("negocio", " ");
                parameters.put("1", "X");
                parameters.put("2", " ");
                parameters.put("3", " ");
                parameters.put("4", " ");
                JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
                jasperprint = JasperFillManager.fillReport(reporte, parameters, new JREmptyDataSource());
                JasperViewer.viewReport(jasperprint, false);
            } catch (JRException ex) {
                Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*Metodo para determinar si un inmueble esta solvente con la municipalidad*/
    public void IsSolvent(Inmuebles inmueble) {
        boolean res = true ;
        List<Pagos> pagos = null;
        Map<String, String> inmueblesMora = new HashMap<String, String>();
        pagos = dao.fin_all_pagos_whereStatement("inmuebles_id = " + inmueble.getId() + " And estados_id = 6");
        if (pagos.size() > 0) {
            inmueblesMora.put("Inmueble: " + inmueble.getDireccion(), "debe: " + pagos.size());
            res = false;
        }
        if (!res) {
            JOptionPane.showMessageDialog(null, "Este inmueble tiene una mora de: " + pagos.size());
        } else {
            try {
                String reporteLocation = "src/Reportes/report11c.jrxml";
                Map<String, Object> parameters = new HashMap();
                JRViewer reportPane;
                //reportStream = SLorenzoParent.class.getResourceAsStream("/Reportes/report11c.jasper");

                parameters.put("nombre", " ");
                parameters.put("inmueble", inmueble.getDireccion());
                parameters.put("negocio", " ");
                parameters.put("1", " ");
                parameters.put("2", " ");
                parameters.put("3", "X");
                parameters.put("4", " ");
                JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
                jasperprint = JasperFillManager.fillReport(reporte, parameters, new JREmptyDataSource());
                JasperViewer.viewReport(jasperprint, false);
            } catch (JRException ex) {
                Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*Metodo para determinar si un negocio esta solvente con la municipalidad*/
    public void IsSolvent(Negocios negocio) {
        boolean res = true;
        List<Pagos> pagos = null;
        Map<String, String> negociosMora = new HashMap<String, String>();
        pagos = dao.fin_all_pagos_whereStatement("negocios_id = " + negocio.getId() + " And estados_id = 6");
        if (pagos.size() > 0) {
            negociosMora.put("Inmueble: " + negocio.getDireccion(), "debe: " + pagos.size());
            res = false;
        }
        if (!res) {
            JOptionPane.showMessageDialog(null, "Este negocio tiene una mora de: " + pagos.size());
        } else {
            try {
                String reporteLocation = "src/Reportes/report11c.jrxml";
                Map<String, Object> parameters = new HashMap();
                JRViewer reportPane;
                //reportStream = SLorenzoParent.class.getResourceAsStream("/Reportes/report11c.jasper");

                parameters.put("nombre", " ");
                parameters.put("inmueble", " ");
                parameters.put("negocio", negocio.getNombreempresa());
                parameters.put("1", " ");
                parameters.put("2", " ");
                parameters.put("3", " ");
                parameters.put("4", "X");
                JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
                jasperprint = JasperFillManager.fillReport(reporte, parameters, new JREmptyDataSource());
                JasperViewer.viewReport(jasperprint, false);
            } catch (JRException ex) {
                Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
