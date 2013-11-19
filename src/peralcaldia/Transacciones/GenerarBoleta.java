/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.Transacciones;

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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.lob.ReaderInputStream;
import peralcaldia.SLorenzoParent;
import peralcaldia.model.Boleta;
import util.GCMPNativos;
import util.GenerarBoletaRepository;

/**
 *
 * @author alex
 */
/*Pantalla utilizada para Generar Boleta de Pagos Inmuebles o Negocios*/
public class GenerarBoleta extends javax.swing.JInternalFrame {

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
    

    /**
     * Creates new form GenerarBoleta
     */
    public GenerarBoleta() {
        initComponents();
        modelocnt.addElement("-");
        cmbcontrib.setModel(modelocnt);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        modelonmeses.addElement("-");
        cmbnmeses.setModel(modelonmeses);
        jchxmora.setSelected(true);
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }

    /*Carga de negocios de acuerdo al contribuyente seleccionado*/
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

    /*Carga de inmuebles de acuerdo al contribuyente seleccionado*/
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

    /*Carga de los pagos pendientes de Cancelacion de acuerdo al inmueble o negocio seleccionado*/
    public void llenargrid() {
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

    /*Carga del numero de meses en deuda*/
    public void llenarcombomesesapagar(int total) {
        if (total != 0) {
            List objects = new ArrayList();
            for (int i = 1; i <= total; i++) {
                objects.add(i);
            }
            componentes.llenarmodelo(modelonmeses, objects);
        } else {
            componentes.limpiarmodelo(modelonmeses);
            modelonmeses.addElement("-");
            cmbnmeses.setModel(modelonmeses);
        }
    }

    /*Generacion de la boleta de pago de acuerdo al numero de meses a pagar*/
    public void generarboletapago() {
        GenerarBoletaRepository genboletas = new GenerarBoletaRepository();
        Boleta miboleta = new Boleta();
        boletamap blrep = new boletamap();
        Map data = new HashMap();
        InputStream reportStream;
        JasperPrint jasperprint = new JasperPrint();
        if (miboleta != null) {
            if (jbtninmuebles.isSelected() && inmueble != null) {
                if (!cmbnmeses.getSelectedItem().toString().equals("-")) {
                    miboleta = genboletas.BoletaInmueble(inmueble, Integer.parseInt(cmbnmeses.getSelectedItem().toString()), jchxmora.isSelected());
                    blrep.setContribuyente(cmbcontrib.getSelectedItem().toString());
                    blrep.setFechageneracion(new Date());
                    blrep.setIdboleta(miboleta.getId());
                    blrep.setInmneg(cmbinmneg.getSelectedItem().toString());
                    blrep.setMesespg(miboleta.getMesesapagar());
                    blrep.setTotal(miboleta.getMontototal());

                    try {
                        Map<String, Object> parameters = new HashMap();
                        JRViewer reportPane;
                        data = blrep.getParameters();
                        String path = "/imagenes/san_lorenzo.png";
                        parameters.put("BoletaMap", data);
                        parameters.put("logo", SLorenzoParent.class.getResourceAsStream(path));
                        reportStream = SLorenzoParent.class.getResourceAsStream("/Reportes/ticketboleta.jasper");
                        jasperprint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource());
                        reportPane = new JRViewer(jasperprint);
                        JasperViewer.viewReport(jasperprint, false);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor ingresar el numero de meses a pagar");
                }
            } else if (jbtnnegocios.isSelected() && negocio != null) {
                if (!cmbnmeses.getSelectedItem().toString().equals("-")) {
                    miboleta = genboletas.BoletaNegocio(negocio, Integer.parseInt(cmbnmeses.getSelectedItem().toString()), jchxmora.isSelected());
                    blrep.setContribuyente(cmbcontrib.getSelectedItem().toString());
                    blrep.setFechageneracion(new Date());
                    blrep.setIdboleta(miboleta.getId());
                    blrep.setInmneg(cmbinmneg.getSelectedItem().toString());
                    blrep.setMesespg(miboleta.getMesesapagar());
                    blrep.setTotal(miboleta.getMontototal());

                    try {
                        Map<String, Object> parameters = new HashMap();
                        JRViewer reportPane;
                        data = blrep.getParameters();
                        String path = "/imagenes/san_lorenzo.png";
                        parameters.put("BoletaMap", data);
                        parameters.put("logo", SLorenzoParent.class.getResourceAsStream(path));
                        reportStream = SLorenzoParent.class.getResourceAsStream("/Reportes/ticketboleta.jasper");
                        jasperprint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource());
                        reportPane = new JRViewer(jasperprint);
                        JasperViewer.viewReport(jasperprint, false);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor ingresar el numero de meses a pagar");
                }
            }
        }
    }
    
    /*Busqueda de Contribuyentes por Nombre*/
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
    
    /*Busqueda de Contribuyente por DUI*/
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

    /*Busqueda de Contribuyente por NIT*/
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
    
    /*Limpieza de la informacion cargada en pantalla*/
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
        jPanel1 = new javax.swing.JPanel();
        jbtninmuebles = new javax.swing.JRadioButton();
        jbtnnegocios = new javax.swing.JRadioButton();
        cmbinmneg = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbcontrib = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jtablelistpg = new javax.swing.JScrollPane();
        jtpagos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbnmeses = new javax.swing.JComboBox();
        jchxmora = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnaplicar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
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

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

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
                        .addComponent(cmbinmneg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtablelistpg, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtablelistpg, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );

        jLabel7.setText("Meses a Pagar:");

        jchxmora.setText("Aplicar Mora");

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel1.setText("Datos del Cobro a Generar :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbnmeses, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jchxmora)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbnmeses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchxmora)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnaplicar.setText("Generar Boleta");
        btnaplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaplicarActionPerformed(evt);
            }
        });

        btnsalir.setText("Salir");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        jButton1.setText("Borrar Boletas No Pagadas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaplicar)
                    .addComponent(btnsalir)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtninmueblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtninmueblesActionPerformed
        // TODO add your handling code here:
        if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
            cmbinmneg.setSelectedIndex(0);
        }
        llenarcomboboxporinmuebles();
        componentes.limpiarmodelo(modelonmeses);
        modelonmeses.addElement("-");
        cmbnmeses.setModel(modelonmeses);
    }//GEN-LAST:event_jbtninmueblesActionPerformed

    private void jbtnnegociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnegociosActionPerformed
        // TODO add your handling code here:
        if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
            cmbinmneg.setSelectedIndex(0);
        }
        llenarcomboboxpornegocio();
        componentes.limpiarmodelo(modelonmeses);
        modelonmeses.addElement("-");
        cmbnmeses.setModel(modelonmeses);        
    }//GEN-LAST:event_jbtnnegociosActionPerformed

    private void cmbinmnegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinmnegActionPerformed
        // TODO add your handling code here:
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        llenargrid();
    }//GEN-LAST:event_cmbinmnegActionPerformed

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:        
        generarboletapago();
    }//GEN-LAST:event_btnaplicarActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void cmbcontribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribActionPerformed
        // TODO add your handling code here:
        buttonGroup1.clearSelection();
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
    }//GEN-LAST:event_cmbcontribActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       GenerarBoletaRepository gb = new GenerarBoletaRepository();
       int x = JOptionPane.showConfirmDialog(null, "Desea borrar todas las boletas que no han sido pagadas?");
       if(x==0)
        gb.BorrarBoletasNoPagadas();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        buttonGroup1.clearSelection();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbcontrib;
    private javax.swing.JComboBox cmbinmneg;
    private javax.swing.JComboBox cmbnmeses;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jbtndui;
    private javax.swing.JRadioButton jbtninmuebles;
    private javax.swing.JRadioButton jbtnnegocios;
    private javax.swing.JRadioButton jbtnnit;
    private javax.swing.JRadioButton jbtnnombres;
    private javax.swing.JCheckBox jchxmora;
    private javax.swing.JScrollPane jtablelistpg;
    private javax.swing.JTable jtpagos;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    // End of variables declaration//GEN-END:variables
}
