/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.Transacciones;

import beans.Verpagosnocancelados;
import controller.InmDAO;
import controller.Controller;
import java.util.List;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Usuarios;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Pagos;
import controller.NegDAO;
import controller.PgDAO;
import controller.UserDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import componentesheredados.CheckCell;
import componentesheredados.CheckRender;
import java.awt.Dimension;
import java.awt.Toolkit;
import util.GCMPNativos;
import util.Value;
import util.verpagos;

/**
 *
 * @author alex
 */
/*Pantalla utilizada para la aplicacion de pagos manuales correspondientes a la carga inicial*/
public class applicarpagomanual extends javax.swing.JInternalFrame {

    InmDAO dao = new InmDAO();
    NegDAO daoneg = new NegDAO();
    PgDAO daopago = new PgDAO();
    UserDAO daouser = new UserDAO();
    Inmuebles inmueble = null;
    Negocios negocio = null;
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel modelocnt = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloinmneg = new DefaultComboBoxModel();
    DefaultTableModel tablam = new DefaultTableModel();
    Contribuyentes contribuyente = new Contribuyentes();
    Usuarios users = new Usuarios();

    /**
     * Creates new form applicarpagomanual
     */
    public applicarpagomanual() {
        initComponents();
        modelocnt.addElement("-");
        cmbcontrib.setModel(modelocnt);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        jdfecha.setDate(new Date());
        txtnombres.setEnabled(false);
        txtnit.setEnabled(false);
        txtdui.setEnabled(false);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Busqueda de Contribuyentes por Nombres*/
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
    
    /*Busqueda de Contribuyentes por DUI*/
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

    /*Carga de negocios de acuerdo al contribuyente seleccionado*/
    public void llenarcomboboxpornegocio() {
        List lista = null;
        Set<Contribuyentes> lcont = null;
        lista = new ArrayList();
        if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
            try {
                if (contribuyente == null && jbtnnombres.isSelected()) {
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

    /*Carga de Inmuebles de acuerdo al contribuyente seleccionado*/
    public void llenarcomboboxporinmuebles() {
        List lista = null;
        lista = new ArrayList();
        if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
            try {
                if (contribuyente == null && jbtnnombres.isSelected()) {
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

    /*Carga de los pagos no cancelados de acuerdo al inmueble o negocio seleccionado*/
    public void llenargrid() {
        List<Verpagosnocancelados> lista = null;
        Verpagosnocancelados vpn;
        List otls;
        Object[] unamas;
        int cont;

        if (cmbinmneg.getModel().getSelectedItem() != null) {
            if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
                if (jbtninmuebles.isSelected()) {
                    negocio = null;
                    inmueble = new Inmuebles();
                    inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_all_pagos_whereStatement("inmuebles_id = " + Integer.toString(inmueble.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();

                    if (!lista.isEmpty() || lista.size() > 0) {
                        unamas = new Object[4];
                        Iterator it = lista.iterator();
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Verpagosnocancelados();
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setSELECCIONAR(new Boolean(object.toString()));
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setPERIODO(object.toString());
                                    cont++;
                                } else if (cont == 2) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Verpagosnocancelados();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Verpagosnocancelados.class));
                        jtpagos.getColumnModel().getColumn(0).setCellEditor(new CheckCell());
                        jtpagos.getColumnModel().getColumn(0).setCellRenderer(new CheckRender());
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnasdesde2(jtpagos, 3);                        
                    }
                } else if (jbtnnegocios.isSelected()) {
                    inmueble = null;
                    negocio = new Negocios();
                    negocio = (Negocios) daoneg.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_all_pagos_whereStatement("negocios_id = " + Integer.toString(negocio.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();

                    if (!lista.isEmpty() || lista.size() > 0 || lista != null) {
                        unamas = new Object[4];
                        Iterator it = lista.iterator();
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Verpagosnocancelados();
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setSELECCIONAR(new Boolean(object.toString()));
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setPERIODO(object.toString());
                                    cont++;
                                } else if (cont == 2) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Verpagosnocancelados();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Verpagosnocancelados.class));
                        jtpagos.getColumnModel().getColumn(0).setCellEditor(new CheckCell());
                        jtpagos.getColumnModel().getColumn(0).setCellRenderer(new CheckRender());
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnasdesde2(jtpagos, 3);
                    }
                    
                } else {
                    System.out.println("Error en la carga");
                }
            }
        }
    }
    
    /*Aplicacion de los pagos seleccionados*/
    public void aplicarpagos(){
        List<verpagos> seleccion = new ArrayList<verpagos>();
        verpagos vp;
        Pagos pago = new Pagos();
        TableModel tverificador;
        Boolean valor;
        Estados estcancel = new Estados();
        estcancel = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");
        //Scan de la Tabla para recuperar todos las lineas chequeadas.
        if (inmueble != null) {
            tverificador = jtpagos.getModel();
            for (int i = 0; i < tverificador.getRowCount(); i++) {
                valor = (Boolean) tverificador.getValueAt(i, 0);
                if (valor) {
                    vp = new verpagos();
                    vp.setMespagado(tverificador.getValueAt(i, 1).toString());
                    vp.setInmueble(inmueble);
                    seleccion.add(vp);
                }                
            }            
        }
        else if (negocio != null){
            tverificador = jtpagos.getModel();
            for (int i = 0; i < tverificador.getRowCount(); i++) {
                valor = (Boolean) tverificador.getValueAt(i, 0);
                if (valor) {
                    vp = new verpagos();
                    vp.setMespagado(tverificador.getValueAt(i, 1).toString());
                    vp.setNegocio(negocio);
                    seleccion.add(vp);
                }                
            }                        
        }
        //Fin del Scan.
        if (!seleccion.isEmpty() || seleccion.size()  > 0) {
            Iterator itseleccion = seleccion.iterator();
            while (itseleccion.hasNext()) {
                vp = (verpagos) itseleccion.next();
                if (vp.getInmueble() != null) {
                    pago = (Pagos) daopago.findByWhereStatementoneobj(Pagos.class, " mespagado = '" + vp.getMespagado() + "' and inmuebles_id = " + vp.getInmueble().getId());
                }
                else if (vp.getNegocio() != null){
                    pago = (Pagos) daopago.findByWhereStatementoneobj(Pagos.class, " mespagado = '" + vp.getMespagado() + "' and negocios_id = " + vp.getNegocio().getId());
                }
                if (pago != null) {
                    if (!txtrecibo.getText().isEmpty()) {                        
                        if (!txtmnt.getText().isEmpty()) {
                            pago.setMontopagado(new BigDecimal(txtmnt.getText()).divide(new BigDecimal(seleccion.size())));
                            pago.setFechapago(jdfecha.getDate());
                            if (!txtcomentario.getText().isEmpty()) {
                                pago.setComentario(txtcomentario.getText().toUpperCase());
                            }
                            pago.setNorecibo(txtrecibo.getText());
                            pago.setEstadosid(estcancel);
                            daopago.update(pago);
                        } else {
                            JOptionPane.showMessageDialog(this, "Favor ingresar el Monto Total de el/los Pagos a Cancelar");
                        }                        
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Favor ingresar el número de recibo destinado a el/los pagos");
                    }                    
                }                
            }//Fin de la iteración de la lista de pagos.
            tablam = componentes.limpiartabla(tablam);
            jtpagos.setModel(tablam);
            llenargrid();
            limpiar();
        }//Fin de la lista de pagos                
    }
    
    /*Limpieza de los datos ingresados del pago*/
    public void limpiar(){
        txtcomentario.setText("");
        txtmnt.setText("");
        txtrecibo.setText("");
        jdfecha.setDate(new Date());
    }
    
    /*Limpieza de la informacion cargada en pantalla*/
    public void LimpiarPantalla(){
        txtcomentario.setText("");
        txtmnt.setText("");
        txtrecibo.setText("");
        jdfecha.setDate(new Date());
        modelocnt.removeAllElements();
        modelocnt.addElement("-");
        componentes.limpiarmodelo(modeloinmneg);
        modeloinmneg.addElement("-");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
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
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jdfecha = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtcomentario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtrecibo = new javax.swing.JTextField();
        txtmnt = new componentesheredados.DecimalJTextField();
        jPanel4 = new javax.swing.JPanel();
        btnaplicar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
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

        jLabel1.setText("jLabel1");

        setClosable(true);
        setIconifiable(true);

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(cmbinmneg, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(37, 37, 37)
                        .addComponent(cmbcontrib, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtablelistpg)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtablelistpg, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setText("Monto Total:");

        jLabel5.setText("Fecha de Aplicación de el/los Pagos:");

        jLabel6.setText("Comentario:");

        jLabel7.setText("Recibo:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtrecibo, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(txtmnt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdfecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtcomentario)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdfecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5)
                        .addComponent(txtmnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtcomentario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtrecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnaplicar.setText("Aplicar Pagos");
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaplicar)
                    .addComponent(btnsalir))
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
                                .addGap(82, 82, 82)
                                .addComponent(jbtndui)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnnit)
                                .addGap(61, 61, 61)
                                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:
        aplicarpagos();
    }//GEN-LAST:event_btnaplicarActionPerformed

    private void jbtninmueblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtninmueblesActionPerformed
        // TODO add your handling code here:
        if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
            cmbinmneg.setSelectedIndex(0);
        }
        llenarcomboboxporinmuebles();
    }//GEN-LAST:event_jbtninmueblesActionPerformed

    private void jbtnnegociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnegociosActionPerformed
        // TODO add your handling code here:
        if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
            cmbinmneg.setSelectedIndex(0);
        }
        llenarcomboboxpornegocio();
    }//GEN-LAST:event_jbtnnegociosActionPerformed

    private void cmbinmnegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinmnegActionPerformed
        // TODO add your handling code here:
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        llenargrid();
    }//GEN-LAST:event_cmbinmnegActionPerformed

    private void cmbcontribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribActionPerformed
        // TODO add your handling code here:
        buttonGroup1.clearSelection();
        componentes.limpiarmodelo(modeloinmneg);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        inmueble = null;
        negocio = null;        
        if (cmbcontrib.getSelectedItem() != null) {
            if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                }
            }
            cmbcontrib.setToolTipText(cmbcontrib.getSelectedItem().toString());
        }        
    }//GEN-LAST:event_cmbcontribActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbcontrib;
    private javax.swing.JComboBox cmbinmneg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private com.toedter.calendar.JDateChooser jdfecha;
    private javax.swing.JScrollPane jtablelistpg;
    private javax.swing.JTable jtpagos;
    private javax.swing.JTextField txtcomentario;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.DecimalJTextField txtmnt;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    private javax.swing.JTextField txtrecibo;
    // End of variables declaration//GEN-END:variables
}
