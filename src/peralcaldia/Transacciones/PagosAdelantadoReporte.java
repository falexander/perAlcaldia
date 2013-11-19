/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

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
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import peralcaldia.model.Boleta;
import util.GCMPNativos;
import util.GenerarBoletaRepository;

/**
 *
 * @author alex
 */
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
    }
    
        public void buscarcontribuyente() {
        List luser = null;
        try {
            luser = daouser.find_usuarios_whereStatement(" nombres || ' '||apellidos like '%" + txtcontribuyente.getText().toUpperCase() + "%' and roles_id= 3");
            if (!luser.isEmpty()) {
                componentes.llenarmodelo(modelocnt, luser);
                cmbcontrib.setModel(modelocnt);
            } else {
                JOptionPane.showMessageDialog(this, "No se encuentran usuarios registrados con el valor ingresado");
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

                users = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, " (nombres || ' ' || apellidos) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                lcont = users.getContribuyenteses();
                cont = (Contribuyentes) lcont.iterator().next();
                lista = daoneg.findnameswhereStatement(" contribuyentes_id = " + cont.getId() + " and estados_id = 1 ");
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
        Contribuyentes cont = new Contribuyentes();
        lista = new ArrayList();
        if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
            try {

                users = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, " (nombres || ' ' || apellidos) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                lcont = users.getContribuyenteses();
                cont = (Contribuyentes) lcont.iterator().next();
                lista = dao.find_direcciones_whereStatement(" contribuyentes_id = " + cont.getId() + " and estados_id = 1 ");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if (lista.size() != 0 || lista != null) {
            componentes.llenarmodelo(modeloinmneg, lista);
            cmbinmneg.setModel(modeloinmneg);
        }

    }

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
            cmbnmeses.setModel(modelonmeses);            
        }
    }
    
    public void generarboletapago() {
        GenerarBoletaRepository genboletas = new GenerarBoletaRepository();
        Boleta miboleta = new Boleta();
        boletamap blrep = new boletamap();
        Map data = new HashMap();
        InputStream reportStream;
        JasperPrint jasperprint = new JasperPrint();

        if (jbtninmuebles.isSelected() && inmueble != null) {
            miboleta = genboletas.BoletaInmueble(inmueble, Integer.parseInt(cmbnmeses.getSelectedItem().toString()), jchxmora.isSelected());
            blrep.setContribuyente(cmbcontrib.getSelectedItem().toString());
            blrep.setFechageneracion(new Date());
            blrep.setIdboleta(miboleta.getId());
            blrep.setInmneg(cmbinmneg.getSelectedItem().toString());
            blrep.setMesespg(miboleta.getMesesapagar());
            blrep.setTotal(miboleta.getMontototal());
            data = blrep.getParameters();
            reportStream = SLorenzoParent.class.getResourceAsStream("/Reportes/ticketboleta.jasper");
            
        } else if (jbtnnegocios.isSelected() && negocio != null) {
            miboleta = genboletas.BoletaNegocio(negocio, Integer.parseInt(cmbnmeses.getSelectedItem().toString()), jchxmora.isSelected());
            blrep.setContribuyente(cmbcontrib.getSelectedItem().toString());
            blrep.setFechageneracion(new Date());
            blrep.setIdboleta(miboleta.getId());
            blrep.setInmneg(cmbinmneg.getSelectedItem().toString());
            blrep.setMesespg(miboleta.getMesesapagar());
            blrep.setTotal(miboleta.getMontototal());
            
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtcontribuyente = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
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

        jLabel2.setText("Contribuyente:");

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbcontrib, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar))
                .addGap(18, 18, 18)
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

        jchxmora.setText("Aplicar / Dispensar Mora");

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel1.setText("Datos del Cobro a Generar :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(btnsalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
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

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modelocnt);
        componentes.limpiarmodelo(modeloinmneg);
        modelocnt.addElement("-");
        cmbcontrib.setModel(modelocnt);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        buscarcontribuyente();
    }//GEN-LAST:event_btnbuscarActionPerformed

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

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_btnaplicarActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void cmbcontribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modeloinmneg);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        inmueble = null;
        negocio = null;
    }//GEN-LAST:event_cmbcontribActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbcontrib;
    private javax.swing.JComboBox cmbinmneg;
    private javax.swing.JComboBox cmbnmeses;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jbtninmuebles;
    private javax.swing.JRadioButton jbtnnegocios;
    private javax.swing.JCheckBox jchxmora;
    private javax.swing.JScrollPane jtablelistpg;
    private javax.swing.JTable jtpagos;
    private javax.swing.JTextField txtcontribuyente;
    // End of variables declaration//GEN-END:variables
}
