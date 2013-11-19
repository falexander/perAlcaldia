/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.formularios;

import beans.Verpagosnocancelados;
import beans.Vpgnocansincheckbox;
import beans.boletamap;
import controller.AbstractDAO;
import controller.InmDAO;
import controller.NegDAO;
import controller.PgDAO;
import controller.UserDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Usuarios;
import componentesheredados.CheckCell;
import componentesheredados.CheckRender;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import peralcaldia.SLorenzoParent;
import peralcaldia.model.Boleta;
import util.GCMPNativos;
import util.GenerarBoletaRepository;
import util.codemd5;

/**
 *
 * @author alex
 */
public class FUATM13i extends javax.swing.JInternalFrame {
    
    InmDAO dao = new InmDAO();
    NegDAO daoneg = new NegDAO();
    PgDAO daopago = new PgDAO();
    UserDAO daouser = new UserDAO();
    Inmuebles inmueble = null;
    Negocios negocio = null;
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel modelocnt = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloinmneg = new DefaultComboBoxModel();
    DefaultComboBoxModel modelonmeses = new DefaultComboBoxModel();
    DefaultTableModel tablam = new DefaultTableModel();
    Contribuyentes contribuyente = new Contribuyentes();
    private String noti1;
    private String noti2;
    private String noti3;
    /**
     * Creates new form GenerarBoleta
     */
    public FUATM13i() {
        initComponents();
        modelocnt.addElement("-");
        cmbcontrib.setModel(modelocnt);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        modelonmeses.addElement("-");
    }
    
  
        
    public void BuscarContribuyenteNombres() {
        List lusuarios = null;
        try {
            /*Buscando Usuarios que coincidan con el nombre o porcion indicada en el Textbox*/
            if (!txtnombres.getText().isEmpty()) {
                lusuarios = daouser.find_usuarios_whereStatement(" a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui like '%" + txtnombres.getText().toUpperCase() + "%' and a.roles = 3");
                if (lusuarios != null && lusuarios.size() > 0) {
                    componentes.llenarmodelo(modelocnt, lusuarios);
                    contribuyente = null;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }
    
    public void BuscarContribuyentesDui() {
        List lusuarios = null;
        try {
            if (!txtdui.getText().isEmpty() && !txtdui.getText().equals("-")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "dui = '" + this.txtdui.getText() + "'");
                if (contribuyente != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelocnt, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el DUI ingresado");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }

    }

    public void BuscarContribuyenteNit() {
        List lusuarios = null;
        try {
            if (!txtnit.getText().isEmpty() && !txtnit.getText().equals("----")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "nit = '" + this.txtnit.getText() + "'");
                if (contribuyente != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelocnt, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el NIT ingresado");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }     

    public void llenarcomboboxpornegocio() {
        List lista = null;
        Usuarios users = new Usuarios();
        Set<Contribuyentes> lcont = null;
        Contribuyentes cont = new Contribuyentes();
        lista = new ArrayList();
        if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
            try {
                if (contribuyente == null) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                }
                lista = daoneg.findnameswhereStatement(" contribuyentes_id = " + contribuyente.getId() + " and estados_id = 1 ");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if (lista.size() != 0 || lista != null) {
            componentes.llenarmodelo(modeloinmneg, lista);
            cmbinmneg.setModel(modeloinmneg);
        }
    }

    public void llenarcomboboxporinmuebles() {
        List lista = null;
        Usuarios users = new Usuarios();
        Set<Contribuyentes> lcont = null;
        lista = new ArrayList();
        if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
            try {
                if (contribuyente == null) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                }
                lista = dao.find_direcciones_whereStatement(" contribuyentes_id = " + contribuyente.getId() + " and estados_id = 1 ");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if (lista.size() != 0 || lista != null) {
            componentes.llenarmodelo(modeloinmneg, lista);
            cmbinmneg.setModel(modeloinmneg);
        }

    }
    
    public void llenarinfo() {
        List<Vpgnocansincheckbox> lista = null;
        Vpgnocansincheckbox vpn;
        List otls;
        Object[] unamas;
        int cont;

        if (cmbinmneg.getModel().getSelectedItem() != null) {
            if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
                if (jbtninmuebles.isSelected()) {
                    negocio = null;
                    inmueble = new Inmuebles();
                    inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_pagos_whereStatement("inmuebles_id = " + Integer.toString(inmueble.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();
                    

                    if (!lista.isEmpty() || lista.size() > 0) {
                        unamas = new Object[3];
                        Iterator it = lista.iterator();
                        
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Vpgnocansincheckbox();
                            
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setPERIODO(object.toString());
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Vpgnocansincheckbox();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Vpgnocansincheckbox.class));
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnas(jtpagos, 2);
                        llenarcombomesesapagar(lista.size());
                        
                    }
                    if (lista.isEmpty() || lista.size() <= 0) {
                        llenarcombomesesapagar(0);
                    }                    
                } else if (jbtnnegocios.isSelected()) {
                    inmueble = null;
                    negocio = new Negocios();
                    negocio = (Negocios) daoneg.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_pagos_whereStatement("negocios_id = " + Integer.toString(negocio.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();

                    if (!lista.isEmpty() || lista.size() > 0 || lista != null) {
                        unamas = new Object[3];
                        Iterator it = lista.iterator();
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Vpgnocansincheckbox();
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setPERIODO(object.toString());
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Vpgnocansincheckbox();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Vpgnocansincheckbox.class));
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnas(jtpagos, 2);
                        llenarcombomesesapagar(lista.size());
                    }
                    if (lista.isEmpty() || lista.size() <= 0) {
                        llenarcombomesesapagar(0);
                    }                    
                } else {
                    System.out.println("Error en la carga");
                }
            }
        }
    
    }

     public void llenargrid() {
        List<Vpgnocansincheckbox> lista = null;
        Vpgnocansincheckbox vpn;
        List otls;
        Object[] unamas;
        int cont;
        
        GenerarBoletaRepository mora = new GenerarBoletaRepository();
        double valormora=0;
        BigDecimal monto= BigDecimal.ZERO;
        BigDecimal montomes= BigDecimal.ZERO;
        BigDecimal fiesta = BigDecimal.valueOf(0.05);
        BigDecimal total= BigDecimal.ZERO;
        boolean primer=true;
        boolean prmonto=true;
        String desde ="";
        String hasta ="";

        if (cmbinmneg.getModel().getSelectedItem() != null) {
            if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
                if (jbtninmuebles.isSelected()) {
                    negocio = null;
                    inmueble = new Inmuebles();
                    inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_pagos_whereStatement("inmuebles_id = " + Integer.toString(inmueble.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();
                    List imps = daopago.GetPagosDetails(inmueble.getId());
                    double otros = 0;
                    Iterator impsIte = imps.iterator();
                    while(impsIte.hasNext())
                    {
                        Object[] tmp= (Object[])impsIte.next();
                        if(tmp[1].toString().equals("ALCANTARILLADO"))
                        {
                            this.jTextAlcantarillado.setText(String.valueOf((double)Math.round(Double.parseDouble(tmp[0].toString())*100)/100));
                        }
                        else if(tmp[1].toString().equals("ALUMBRADO"))
                        {
                            this.jTextAlumbrado.setText(String.valueOf((double)Math.round(Double.parseDouble(tmp[0].toString())*100)/100));
                        }
                        else if(tmp[1].toString().equals("ASEO"))
                        {
                            this.jTextAseo.setText(String.valueOf((double)Math.round(Double.parseDouble(tmp[0].toString())*100)/100));
                        }
                        else if(tmp[1].toString().equals("PAVIMENTO"))
                        {
                            this.jTextPavimento.setText(String.valueOf((double)Math.round(Double.parseDouble(tmp[0].toString())*100)/100));
                        }                        
                        else
                        {
                            otros += Double.parseDouble(tmp[0].toString());
                        }
                    }
                    this.jTextAgua.setText(String.valueOf((double)Math.round(otros*100)/100));
                    if (!lista.isEmpty() || lista.size() > 0) {
                        unamas = new Object[3];
                        Iterator it = lista.iterator();
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Vpgnocansincheckbox();
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setPERIODO(object.toString());
                                    if(primer == true)
                                    { desde= object.toString();
                                      primer=false;
                                      
                                    }hasta=object.toString();
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    monto = monto.add((new BigDecimal(object.toString())).setScale(2, RoundingMode.HALF_EVEN));
                                    if (prmonto==true){
                                        montomes = montomes.add((new BigDecimal(object.toString())).setScale(2, RoundingMode.HALF_EVEN));
                                        
                                    prmonto=false;
                                    }
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Vpgnocansincheckbox();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Vpgnocansincheckbox.class));
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnas(jtpagos, 2);
                        llenarcombomesesapagar(lista.size());
                        fiesta = fiesta.multiply(monto).setScale(2, RoundingMode.HALF_EVEN);
                        jTextNombre.setText(inmueble.getContribuyentes().getUsuarios().getNombres()+ " "+ inmueble.getContribuyentes().getUsuarios().getApellidos()); 
                        jTextDireccion.setText(inmueble.getDireccion());
                        jTextMonto.setText(monto.toString());
                        jTextDesde.setText(desde);
                        jTextHasta.setText(hasta);
                        jTextFiestas.setText(fiesta.toString());
                        Date from = codemd5.formatearfechazerohoras(desde);
                        Date to = codemd5.formatearfechaultimahora(hasta);
                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();
                        int mini, meses, mt, anioant, anioact;
                        c1.setTime(from);
                        anioant = c1.get(Calendar.YEAR);
                        mini = c1.get(Calendar.MONTH);
                        c2.setTime(to);
                        anioact = c2.get(Calendar.YEAR);
                        meses = c2.get(Calendar.MONTH) + 1;
                        if (anioact - anioant == 0) { 
                            mt = meses - mini - 2;
                        } else {
                            mini = ((anioact - anioant) * 12) - (c1.get(Calendar.MONTH) );
                            mt = (mini + meses)-2;
                        }
                       
                       valormora = mora.CalcularMora(desde, mt, montomes);
                        DecimalFormat df = new DecimalFormat("#.##");
                            jTextInteres.setText(df.format(valormora).toString());
                        total=total.add(monto).add(fiesta).add(new BigDecimal(valormora)).setScale(2, RoundingMode.HALF_EVEN);
                            jTextTotal.setText(total.toString());
                        ajustarFormulario();
                    }
                    if (lista.isEmpty() || lista.size() <= 0) {
                        llenarcombomesesapagar(0);
                    }
                } else if (jbtnnegocios.isSelected()) {
                    limpiar();
                    inmueble = null;
                    negocio = new Negocios();
                    negocio = (Negocios) daoneg.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_pagos_whereStatement("negocios_id = " + Integer.toString(negocio.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();

                    if (!lista.isEmpty() || lista.size() > 0 || lista != null) {
                        unamas = new Object[3];
                        Iterator it = lista.iterator();
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Vpgnocansincheckbox();
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setPERIODO(object.toString());
                                    if(primer == true)
                                    { desde= object.toString();
                                      primer=false;
                                      
                                    }hasta=object.toString();
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                     vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    monto = monto.add((new BigDecimal(object.toString())).setScale(2, RoundingMode.HALF_EVEN));
                                    if (prmonto==true){
                                        montomes = montomes.add((new BigDecimal(object.toString())).setScale(2, RoundingMode.HALF_EVEN));
                                        
                                    prmonto=false;
                                    }
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Vpgnocansincheckbox();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Vpgnocansincheckbox.class));
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnas(jtpagos, 2);
                        llenarcombomesesapagar(lista.size());
                        jTextDireccion.setText(negocio.getDireccion());
                        jTextNombre.setText(negocio.getContribuyentes().getUsuarios().getNombres()+ " "+ negocio.getContribuyentes().getUsuarios().getApellidos()); 
                        fiesta = fiesta.multiply(monto).setScale(2, RoundingMode.HALF_EVEN);
                        jTextAlumbrado.setText(monto.toString());
                        jTextMonto.setText(monto.toString());
                        jTextDesde.setText(desde);
                        jTextHasta.setText(hasta);
                        jTextAseo.setText(fiesta.toString());
                        //jTextTotal.setText(montomes.toString());
                        Date from = codemd5.formatearfechazerohoras(desde);
                        Date to = codemd5.formatearfechaultimahora(hasta);
                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();
                        int mini, meses, mt, anioant, anioact;
                        c1.setTime(from);
                        anioant = c1.get(Calendar.YEAR);
                        mini = c1.get(Calendar.MONTH);
                        c2.setTime(to);
                        anioact = c2.get(Calendar.YEAR);
                        meses = c2.get(Calendar.MONTH) + 1;
                        if (anioact - anioant == 0) { 
                            mt = meses - mini - 2;
                        } else {
                            mini = ((anioact - anioant) * 12) - (c1.get(Calendar.MONTH) );
                            mt = (mini + meses)-2;
                        }
                       valormora = mora.CalcularMora(desde, mt, montomes);
                        DecimalFormat df = new DecimalFormat("#.##");
                            jTextPavimento.setText(df.format(valormora).toString());
                        total=total.add(monto).add(fiesta).add(new BigDecimal(valormora)).setScale(2, RoundingMode.HALF_EVEN);
                            jTextAgua.setText(total.toString());
                    }
                    if (lista.isEmpty() || lista.size() <= 0) {
                        llenarcombomesesapagar(0);
                    }
                } else {
                    System.out.println("Error en la carga");
                }
            }
        }
    }
    
    public void llenarcombomesesapagar(int total) {
        if (total != 0) {
            List objects = new ArrayList();
            for (int i = 1; i <= total; i++) {
                objects.add(i);
            }
            componentes.llenarmodelo(modelonmeses, objects);
        }
        else{
            componentes.limpiarmodelo(modelonmeses);
            modelonmeses.addElement("-");           
        }
    }
    
    private void ajustarFormulario()
    {
        if (jbtninmuebles.isSelected()){
            //primero labels
            jLabelAlumbrado.setText("Alumbrado:");
            jLabelAseo.setText("Aseo:");
            jLabelPavimento.setText("Pavimento:");
            jLabelAgua.setText("Agua:");
            //habilitando textfiels
            jTextAlcantarillado.setVisible(true);
            jTextFiestas.setVisible(true);
            jTextInteres.setVisible(true);
            jTextTotal.setVisible(true);
            //habilitando labels
            jLabelAlcantarillado.setVisible(true);
            jLabelFiestas.setVisible(true);
            jLabelInteres.setVisible(true);
            jLabelTotal.setVisible(true);
        }else
        if (jbtnnegocios.isSelected()){
            //jlabel nuevas etiquetas
            jLabelAlumbrado.setText("Comercio:");
            jLabelAseo.setText("Fiestas:");
            jLabelPavimento.setText("Mora:");
            jLabelAgua.setText("Total:");
            
            //deshabilitando textfiels
            jTextAlcantarillado.setVisible(false);
            jTextFiestas.setVisible(false);
            jTextInteres.setVisible(false);
            jTextTotal.setVisible(false);
            //deshabilitando labels
            jLabelAlcantarillado.setVisible(false);
            jLabelFiestas.setVisible(false);
            jLabelInteres.setVisible(false);
            jLabelTotal.setVisible(false);
        }
    }

    private void limpiar(){
    jTextNombre.setText("");
    jTextAgua.setText("");
    jTextAlcantarillado.setText("");
    jTextAlumbrado.setText("");
    jTextAseo.setText("");
    jTextDesde.setText("");
    jTextDireccion.setText("");
    jTextFiestas.setText("");
    jTextHasta.setText("");
    jTextInteres.setText("");
    jTextMonto.setText("");
    jTextPavimento.setText("");
    jTextTotal.setText("");
    }
    
        public  void evaluar () {
        if (cmbAviso.getSelectedItem().equals("1")) {
            noti1 = "X";
        }else{
        noti1="";}
        if (cmbAviso.getSelectedItem().equals("2")) {
            noti2 = "X";
        }else{
        noti2="";}
        if (cmbAviso.getSelectedItem().equals("3")) {
            noti3 = "X";
        }else{
        noti3="";}
    }
        
         public void LimpiarPantalla(){
        if (cmbcontrib.getSelectedItem() != null && cmbinmneg.getSelectedItem() != null) {
            try {
                cmbcontrib.removeAllItems();
                modelocnt.addElement("-");
                componentes.limpiarmodelo(modeloinmneg);
                modeloinmneg.addElement("-");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }        
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        cmbAviso = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jLabelDireccion = new javax.swing.JLabel();
        jTextNombre = new javax.swing.JTextField();
        jLabelMonto = new javax.swing.JLabel();
        jLabelDesde = new javax.swing.JLabel();
        jLabelAlumbrado = new javax.swing.JLabel();
        jLabelAseo = new javax.swing.JLabel();
        jLabelPavimento = new javax.swing.JLabel();
        jLabelAgua = new javax.swing.JLabel();
        jLabelAlcantarillado = new javax.swing.JLabel();
        jLabelFiestas = new javax.swing.JLabel();
        jLabelInteres = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabelHasta = new javax.swing.JLabel();
        btnsalir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextDireccion = new javax.swing.JTextField();
        jTextMonto = new javax.swing.JTextField();
        jTextDesde = new javax.swing.JTextField();
        jTextHasta = new javax.swing.JTextField();
        jTextAlumbrado = new javax.swing.JTextField();
        jTextAseo = new javax.swing.JTextField();
        jTextPavimento = new javax.swing.JTextField();
        jTextAgua = new javax.swing.JTextField();
        jTextAlcantarillado = new javax.swing.JTextField();
        jTextFiestas = new javax.swing.JTextField();
        jTextInteres = new javax.swing.JTextField();
        jTextTotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtnombres = new componentesheredados.LettersJTextField();
        jLabel18 = new javax.swing.JLabel();
        txtdui = new componentesheredados.DUITextField();
        jLabel19 = new javax.swing.JLabel();
        txtnit = new componentesheredados.NitJTextField();
        btnbuscar = new javax.swing.JButton();
        jbtnnombres = new javax.swing.JRadioButton();
        jbtndui = new javax.swing.JRadioButton();
        jbtnnit = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jbtninmuebles = new javax.swing.JRadioButton();
        jbtnnegocios = new javax.swing.JRadioButton();
        cmbinmneg = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbcontrib = new javax.swing.JComboBox();
        jtablelistpg = new javax.swing.JScrollPane();
        jtpagos = new javax.swing.JTable();

        setClosable(true);

        cmbAviso.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", " " }));
        cmbAviso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAvisoActionPerformed(evt);
            }
        });

        jLabel3.setText("selecione el aviso");

        jLabelNombre.setText("Nombre:");

        jLabelDireccion.setText("Direccion:");

        jTextNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNombreActionPerformed(evt);
            }
        });

        jLabelMonto.setText("Monto:");

        jLabelDesde.setText("Desde:");

        jLabelAlumbrado.setText("Alumbrado:");

        jLabelAseo.setText("Aseo:");

        jLabelPavimento.setText("Pavimento:");

        jLabelAgua.setText("Agua:");

        jLabelAlcantarillado.setText("Alcantarillado:");

        jLabelFiestas.setText("Fiestas:");

        jLabelInteres.setText("Interes:");

        jLabelTotal.setText("Total:");

        jLabelHasta.setText("Hasta:");

        btnsalir.setText("Salir");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        jButton1.setText("Generar Aviso");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDireccionActionPerformed(evt);
            }
        });

        jTextMonto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMontoActionPerformed(evt);
            }
        });

        jTextDesde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDesdeActionPerformed(evt);
            }
        });

        jTextHasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextHastaActionPerformed(evt);
            }
        });

        jTextAlumbrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAlumbradoActionPerformed(evt);
            }
        });

        jTextAseo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAseoActionPerformed(evt);
            }
        });

        jTextPavimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPavimentoActionPerformed(evt);
            }
        });

        jTextAgua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAguaActionPerformed(evt);
            }
        });

        jTextAlcantarillado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAlcantarilladoActionPerformed(evt);
            }
        });

        jTextFiestas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFiestasActionPerformed(evt);
            }
        });

        jTextInteres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextInteresActionPerformed(evt);
            }
        });

        jTextTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAlcantarillado)
                            .addComponent(jLabelNombre)
                            .addComponent(jLabelDireccion)
                            .addComponent(jLabelMonto)
                            .addComponent(jLabelDesde)
                            .addComponent(jLabelHasta)
                            .addComponent(jLabelAlumbrado)
                            .addComponent(jLabelAseo)
                            .addComponent(jLabelPavimento)
                            .addComponent(jLabelAgua)
                            .addComponent(jLabelFiestas)
                            .addComponent(jLabelInteres)
                            .addComponent(jLabelTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAlumbrado, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAseo, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextPavimento, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAgua, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAlcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFiestas, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextInteres, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAviso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDireccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMonto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDesde))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHasta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextAlumbrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAlumbrado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextAseo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAseo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextPavimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPavimento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextAgua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAgua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextAlcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAlcantarillado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFiestas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFiestas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextInteres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInteres))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTotal))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnsalir))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Criterios de Busqueda");

        jLabel17.setText("Nombres:");

        jLabel18.setText("DUI:");

        try {
            txtdui.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel19.setText("NIT:");

        try {
            txtnit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-######-###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtnnombres);
        jbtnnombres.setText("Nombres y Apellidos");
        jbtnnombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombresActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtndui);
        jbtndui.setText("DUI");
        jbtndui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnduiActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtnnit);
        jbtnnit.setText("NIT");
        jbtnnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(txtnombres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(42, 42, 42)
                        .addComponent(txtdui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jbtnnombres)
                                .addGap(72, 72, 72)
                                .addComponent(jbtndui)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                                .addComponent(jbtnnit)
                                .addGap(61, 61, 61)
                                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbuscar)
                    .addComponent(jbtnnombres)
                    .addComponent(jbtndui)
                    .addComponent(jbtnnit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        buttonGroup1.add(jbtninmuebles);
        jbtninmuebles.setText("Inmuebles");
        jbtninmuebles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtninmueblesActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtnnegocios);
        jbtnnegocios.setText("Negocios");
        jbtnnegocios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnegociosActionPerformed(evt);
            }
        });

        cmbinmneg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbinmnegActionPerformed(evt);
            }
        });

        jLabel4.setText("Contribuyente:");

        cmbcontrib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontribActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbtninmuebles)
                        .addGap(33, 33, 33)
                        .addComponent(jbtnnegocios)
                        .addGap(18, 18, 18)
                        .addComponent(cmbinmneg, 0, 413, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(45, 45, 45)
                        .addComponent(cmbcontrib, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbcontrib, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtninmuebles)
                        .addComponent(jbtnnegocios))
                    .addComponent(cmbinmneg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtpagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtablelistpg.setViewportView(jtpagos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtablelistpg, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtablelistpg, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void cmbAvisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAvisoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAvisoActionPerformed

    private void jTextNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNombreActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if (jbtninmuebles.isSelected()){
        evaluar();
        try{
            Map<String, Object> params = new HashMap<String, Object>();
            String reporteLocation ="src/formularios/report13i.jrxml";
            params.put("noti1",noti1);
            params.put("noti2",noti2);
            params.put("noti3",noti3);
            params.put("monto",jTextMonto.getText());
            params.put("nombre",jTextNombre.getText());
            params.put("direccion",jTextDireccion.getText());
            params.put("desde",jTextDesde.getText());
            params.put("hasta",jTextHasta.getText());
            params.put("alumbrado",jTextAlumbrado.getText());
            params.put("aseo",jTextAseo.getText());
            params.put("pavimento",jTextPavimento.getText());
            params.put("agua",jTextAgua.getText());
            params.put("alcantarillado",jTextAlcantarillado.getText());
            params.put("fiestas",jTextFiestas.getText());
            params.put("interes",jTextInteres.getText());
            params.put("total",jTextTotal.getText());

            //params.put("numero","22");
            JasperReport reporte=   JasperCompileManager.compileReport(reporteLocation);
            //JasperPrint print = JasperFillManager.fillReport(reporte,null,coneccionSQL());
            JasperPrint print = JasperFillManager.fillReport(reporte,params,new JREmptyDataSource());
            JasperViewer.viewReport(print,false);
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(false);
            add(viewer);
            //JasperExportManager.exportReportToPdfFile(print, "Informefuatm1.pdf");
        }catch(Exception e   ){
            System.out.print(e.getMessage());
        }
        }
        if(jbtnnegocios.isSelected()){
        evaluar();
        try{
            Map<String, Object> params = new HashMap<String, Object>();
                String reporteLocation ="src/formularios/report13c.jrxml";
                params.put("noti1",noti1);
                params.put("noti2",noti2);
                params.put("noti3",noti3);
                params.put("monto",jTextMonto.getText());
                params.put("nombre",jTextNombre.getText());
                params.put("direccion",jTextDireccion.getText());
                params.put("desde",jTextDesde.getText());
                params.put("hasta",jTextHasta.getText());
                params.put("comercio",jTextAlumbrado.getText());
                params.put("fiestas",jTextAseo.getText());
                params.put("mora",jTextPavimento.getText());
                params.put("total",jTextAgua.getText());
  
              


                
                //params.put("numero","22");
                JasperReport reporte=   JasperCompileManager.compileReport(reporteLocation);
                //JasperPrint print = JasperFillManager.fillReport(reporte,null,coneccionSQL());
                JasperPrint print = JasperFillManager.fillReport(reporte,params,new JREmptyDataSource());
                JasperViewer.viewReport(print,false);
             //   JRViewer viewer = new JRViewer(print);
            //viewer.setOpaque(true);
           // viewer.setVisible(true);
            //    add(viewer);
             //JasperExportManager.exportReportToPdfFile(print, "Informefuatm1.pdf");   
        }catch(Exception e   ){
        System.out.print(e.getMessage());
        }
            
        
        }

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDireccionActionPerformed

    private void jTextMontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextMontoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextMontoActionPerformed

    private void jTextDesdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDesdeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDesdeActionPerformed

    private void jTextHastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextHastaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextHastaActionPerformed

    private void jTextAlumbradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAlumbradoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAlumbradoActionPerformed

    private void jTextAseoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAseoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAseoActionPerformed

    private void jTextPavimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPavimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPavimentoActionPerformed

    private void jTextAguaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAguaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAguaActionPerformed

    private void jTextAlcantarilladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAlcantarilladoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAlcantarilladoActionPerformed

    private void jTextFiestasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFiestasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFiestasActionPerformed

    private void jTextInteresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextInteresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextInteresActionPerformed

    private void jTextTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTotalActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modelocnt);
        modelocnt.addElement("-");
        LimpiarPantalla();
        if (jbtnnombres.isSelected()) {
            BuscarContribuyenteNombres();
        }else if(jbtndui.isSelected()){
            BuscarContribuyentesDui();
        }else if(jbtnnit.isSelected()){
            BuscarContribuyenteNit();
        }else{
            JOptionPane.showMessageDialog(this, "Elija un criterio de Busqueda e Ingrese el valor a buscar");
        }
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void jbtnnombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnombresActionPerformed
        // TODO add your handling code here:
        txtnombres.setEnabled(true);
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtdui.setText("");
        txtnit.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnnombresActionPerformed

    private void jbtnduiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnduiActionPerformed
        // TODO add your handling code here:
        txtdui.setEnabled(true);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        txtnit.setText("");
        txtnombres.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnduiActionPerformed

    private void jbtnnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnitActionPerformed
        // TODO add your handling code here:
        txtnit.setEnabled(true);
        txtnombres.setEnabled(false);
        txtdui.setEnabled(false);
        txtnombres.setText("");
        txtdui.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnnitActionPerformed

    private void jbtninmueblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtninmueblesActionPerformed
        // TODO add your handling code here:
        if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
            cmbinmneg.setSelectedIndex(0);
        }
        llenarcomboboxporinmuebles();
        componentes.limpiarmodelo(modelonmeses);
        modelonmeses.addElement("-");
        limpiar();
        ajustarFormulario();
    }//GEN-LAST:event_jbtninmueblesActionPerformed

    private void jbtnnegociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnegociosActionPerformed
        // TODO add your handling code here:
        if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
            cmbinmneg.setSelectedIndex(0);
        }
        llenarcomboboxpornegocio();
        componentes.limpiarmodelo(modelonmeses);
        modelonmeses.addElement("-");
        limpiar();
        ajustarFormulario();
    }//GEN-LAST:event_jbtnnegociosActionPerformed

    private void cmbinmnegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinmnegActionPerformed
        // TODO add your handling code here:
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        llenargrid();
    }//GEN-LAST:event_cmbinmnegActionPerformed

    private void cmbcontribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribActionPerformed
        // TODO add your handling code here:
        if (cmbcontrib.getSelectedItem() != null) {
            if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                    cmbcontrib.setToolTipText(cmbcontrib.getSelectedItem().toString());
                }
            }
        }
        componentes.limpiarmodelo(modeloinmneg);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        inmueble = null;
        negocio = null;
        limpiar();
    }//GEN-LAST:event_cmbcontribActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbAviso;
    private javax.swing.JComboBox cmbcontrib;
    private javax.swing.JComboBox cmbinmneg;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelAgua;
    private javax.swing.JLabel jLabelAlcantarillado;
    private javax.swing.JLabel jLabelAlumbrado;
    private javax.swing.JLabel jLabelAseo;
    private javax.swing.JLabel jLabelDesde;
    private javax.swing.JLabel jLabelDireccion;
    private javax.swing.JLabel jLabelFiestas;
    private javax.swing.JLabel jLabelHasta;
    private javax.swing.JLabel jLabelInteres;
    private javax.swing.JLabel jLabelMonto;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPavimento;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextAgua;
    private javax.swing.JTextField jTextAlcantarillado;
    private javax.swing.JTextField jTextAlumbrado;
    private javax.swing.JTextField jTextAseo;
    private javax.swing.JTextField jTextDesde;
    private javax.swing.JTextField jTextDireccion;
    private javax.swing.JTextField jTextFiestas;
    private javax.swing.JTextField jTextHasta;
    private javax.swing.JTextField jTextInteres;
    private javax.swing.JTextField jTextMonto;
    private javax.swing.JTextField jTextNombre;
    private javax.swing.JTextField jTextPavimento;
    private javax.swing.JTextField jTextTotal;
    private javax.swing.JRadioButton jbtndui;
    private javax.swing.JRadioButton jbtninmuebles;
    private javax.swing.JRadioButton jbtnnegocios;
    private javax.swing.JRadioButton jbtnnit;
    private javax.swing.JRadioButton jbtnnombres;
    private javax.swing.JScrollPane jtablelistpg;
    private javax.swing.JTable jtpagos;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    // End of variables declaration//GEN-END:variables
}
