/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.AbstractDAO;
import controller.PgDAO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;

/**
 *
 * @author alex
 */
/*Clase utilizada para la generacion de los reportes estadisticos para el departamento de 
 * cuentas corrientes y para el consejo de la alcaldia*/
public class ReportsRepository {

    JasperPrint jasperprint = new JasperPrint();
    GenericRepository gr = new GenericRepository();

    /*Metodo para generar el reporte de los contribuyentes en Mora con la municipalidad*/
    public void ContribuyentesMoraGeneral() {
        try {
            PgDAO p = new PgDAO();
            String reporteLocation = "src/Reportes/ContribuyentesMora.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("totalContribuyentes", String.valueOf(p.GetContribuyentesMora()));
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para generar el reporte de los Inmuebles en Mora con la municipalidad*/
    public void InmueblesMoraDetail() {
        try {
            PgDAO p = new PgDAO();
            String reporteLocation = "src/Reportes/InmeublesMoraDetallado.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            // parameters.put("totalContribuyentes", String.valueOf(p.GetContribuyentesMora()));
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para generar el reporte de los Negocios en Mora con la Municipalidad*/
    public void NegociosMoraDetail() {
        try {
            PgDAO p = new PgDAO();
            String reporteLocation = "src/Reportes/NegociosMoraDetallado.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            // parameters.put("totalContribuyentes", String.valueOf(p.GetContribuyentesMora()));
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para generar el reporte de Recaudacion Mensual*/
    public void RecaudacionMensual(String BeginDate, String EndDate) {
        try {
            PgDAO p = new PgDAO();
            String reporteLocation = "src/Reportes/RecaudacionMensual.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("BeginDate", BeginDate);
            parameters.put("EndDate", EndDate);
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para generar el reporte de Recaudacion Anual*/
    public void RecaudacionAnual(Integer BeginDate, Integer EndDate) {
        try {
            PgDAO p = new PgDAO();
            String reporteLocation = "src/Reportes/RecaudacionAnual.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("BeginYear", BeginDate);
            parameters.put("EndYear", EndDate);
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para generar el reporte de Resumen de Estado de los Inmuebles y contribuyentes*/
    public void ResumenEstado() {
        try {
            PgDAO p = new PgDAO();
            String reporteLocation = "src/Reportes/ResumenEstados.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("TotalDeuda", this.GetMoraTotal());
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para obtener la mora total*/
    public Double GetMoraTotal() {
        PgDAO pdao = new PgDAO();
        List<Pagos> p = pdao.findByWhereStatement(Pagos.class, "estados_id = 6");
        Iterator pIt = p.iterator();
        double res = 0;
        while (pIt.hasNext()) {
            res += ((Pagos) (pIt.next())).getMonto().doubleValue();
        }
        return res;
    }

    public void reportTes() {
        AbstractDAO ad = new AbstractDAO();
        try {
            List<Estados> estados = ad.findAll(Estados.class);
            JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(estados);
            String reporteLocation = "src/Reportes/testr.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            //parameters.put("totalContribuyentes", "");
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, beanCollectionDataSource);
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JRException ex) {
            Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Metodo para generar el reporte del detalle de Ingresos por Impuestos*/
    public void IngresosImpuestos(String fechaInicio, String fechaFin) {
        try {
            String reporteLocation = "src/Reportes/IngresosPorImpuestos.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("fechaInicio", fechaInicio);
            parameters.put("fechaFin", fechaFin);
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void PagosAdelantadosInmueble(int inmuebleId) {
        try {
            String reporteLocation = "src/Reportes/PagosAdelantados.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("inmueble_id", inmuebleId);            
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void PagosAdelantadosNegocio(int negocioId) {
        try {
            String reporteLocation = "src/Reportes/PagosAdelantadosNegocios.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("negocio_id", negocioId);            
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void GenerarEstadosContribuyente(Contribuyentes contribuyente,String beginDate, String endDate) {
        try {
            String reporteLocation = "src/Reportes/EstadoCuentaContrib.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("beginDate", beginDate);            
            parameters.put("endDate", endDate);            
            parameters.put("contribName", contribuyente.getUsuarios().getNombres() +" "+contribuyente.getUsuarios().getApellidos()+" Dui: "+contribuyente.getDui());            
            parameters.put("ContribuyenteId", contribuyente.getId());            
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void GenerarEstadosInmueble(Inmuebles inmueble,String beginDate, String endDate) {
        try {
            String reporteLocation = "src/Reportes/EstadoCuentaInmueble.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("beginDate", beginDate);            
            parameters.put("endDate", endDate);            
            parameters.put("Inmueble", inmueble.getDireccion());            
            parameters.put("InmuebleId", inmueble.getId());
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void GenerarEstadosNegocio(Negocios negocio,String beginDate, String endDate) {
        try {
            String reporteLocation = "src/Reportes/EstadoCuentaNegocio.jrxml";
            Map<String, Object> parameters = new HashMap();
            JRViewer reportPane;
            parameters.put("beginDate", beginDate);            
            parameters.put("endDate", endDate);            
            parameters.put("Nombre", negocio.getNombreempresa());            
            parameters.put("NegocioId", negocio.getId());
            JasperReport reporte = JasperCompileManager.compileReport(reporteLocation);
            jasperprint = JasperFillManager.fillReport(reporte, parameters, gr.GetConnection());
            JasperViewer.viewReport(jasperprint, false);
            try {
                gr.GetConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (JRException ex) {
            Logger.getLogger(SolvenciasRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
