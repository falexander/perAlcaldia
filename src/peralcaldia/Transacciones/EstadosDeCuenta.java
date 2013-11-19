/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.Transacciones;

import controller.InmDAO;
import controller.NegDAO;
import controller.PgDAO;
import controller.UserDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import util.GCMPNativos;
import util.ReportsRepository;
import util.codemd5;

/**
 *
 * @author alex
 */
public class EstadosDeCuenta extends javax.swing.JInternalFrame {

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

    /**
     * Creates new form EstadosDeCuenta
     */
    public EstadosDeCuenta() {
        initComponents();
        modelocnt.addElement("-");
        cmbcontrib.setModel(modelocnt);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        jDateChooser1.setDate(new Date());
        jDateChooser2.setDate(new Date());
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }

    /*Buscar Contribuyentes por Nombres*/
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

    /*Buscar Contribuyentes por DUI*/
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

    /*Buscar contribuyente por NIT*/
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

    /*Limpiar la informacion cargada en pantalla*/
    public void LimpiarPantalla() {
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

    /*Carga de Negocios de acuerdo al contribuyente seleccionado*/
    public void llenarcomboboxpornegocio() {
        List lista = null;
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

    /*Carga de Inmuebles de acuerdo al contribuyente seleccionado*/
    public void llenarcomboboxporinmuebles() {
        List lista = null;
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

    public void RecuperarInmNego() {
        if (jbtninmuebles.isSelected()) {
            negocio = null;
            inmueble = new Inmuebles();
            inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '" + cmbinmneg.getSelectedItem().toString() + "'");
        } else if (jbtnnegocios.isSelected()) {
            inmueble = null;
            negocio = new Negocios();
            negocio = (Negocios) daoneg.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbinmneg.getSelectedItem().toString() + "'");
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
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtnombres = new componentesheredados.LettersJTextField();
        jLabel18 = new javax.swing.JLabel();
        txtdui = new componentesheredados.DUITextField();
        jLabel19 = new javax.swing.JLabel();
        txtnit = new componentesheredados.NitJTextField();
        btnbuscar1 = new javax.swing.JButton();
        jbtnnombres = new javax.swing.JRadioButton();
        jbtndui = new javax.swing.JRadioButton();
        jbtnnit = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jbtninmuebles = new javax.swing.JRadioButton();
        jbtnnegocios = new javax.swing.JRadioButton();
        cmbinmneg = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbcontrib = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        btngnestado = new javax.swing.JButton();
        btngnconglomerado = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();

        setClosable(true);
        setIconifiable(true);

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

        btnbuscar1.setText("Buscar");
        btnbuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtnnombres);
        jbtnnombres.setText("Nombres y Apellidos");
        jbtnnombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombresActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtndui);
        jbtndui.setText("DUI");
        jbtndui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnduiActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtnnit);
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
                        .addComponent(txtdui, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jbtnnombres)
                                .addGap(85, 85, 85)
                                .addComponent(jbtndui)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnnit)
                                .addGap(59, 59, 59)
                                .addComponent(btnbuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(btnbuscar1)
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

        buttonGroup2.add(jbtninmuebles);
        jbtninmuebles.setText("Inmuebles");
        jbtninmuebles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtninmueblesActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtnnegocios);
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
                        .addGap(18, 18, 18)
                        .addComponent(jbtnnegocios)
                        .addGap(18, 18, 18)
                        .addComponent(cmbinmneg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
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

        btngnestado.setText("Estado de Cuenta Contribuyente");
        btngnestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngnestadoActionPerformed(evt);
            }
        });

        btngnconglomerado.setText("Estado de cuenta inmueble/negocio");
        btngnconglomerado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngnconglomeradoActionPerformed(evt);
            }
        });

        btnsalir.setText("Salir");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btngnestado, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btngnconglomerado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnsalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnsalir)
                        .addComponent(btngnconglomerado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btngnestado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("De: ");

        jLabel2.setText("Hasta:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar1ActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modelocnt);
        modelocnt.addElement("-");
        LimpiarPantalla();
        if (jbtnnombres.isSelected()) {
            BuscarContribuyenteNombres();
        } else if (jbtndui.isSelected()) {
            BuscarContribuyentesDui();
        } else if (jbtnnit.isSelected()) {
            BuscarContribuyenteNit();
        } else {
            JOptionPane.showMessageDialog(this, "Elija un criterio de Busqueda e Ingrese el valor a buscar");
        }
    }//GEN-LAST:event_btnbuscar1ActionPerformed

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
        if (cmbinmneg.getSelectedItem() != null) {
            RecuperarInmNego();
            cmbinmneg.setToolTipText(cmbinmneg.getSelectedItem().toString());
        }
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
        inmueble = null;
        negocio = null;
    }//GEN-LAST:event_cmbcontribActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btngnestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngnestadoActionPerformed
        if (cmbcontrib.getSelectedItem() != null && !cmbcontrib.getSelectedItem().toString().equals("-")) {
            ReportsRepository rr = new ReportsRepository();
            rr.GenerarEstadosContribuyente(contribuyente, codemd5.GetFechaSinHora(this.jDateChooser1.getDate()), codemd5.GetFechaSinHora(this.jDateChooser2.getDate()));
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese Todos los Criterios de Busqueda");
        }
    }//GEN-LAST:event_btngnestadoActionPerformed

    private void btngnconglomeradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngnconglomeradoActionPerformed
        if (cmbcontrib.getSelectedItem() != null && cmbinmneg.getSelectedItem() != null && !cmbcontrib.getSelectedItem().toString().equals("-") && !cmbinmneg.getSelectedItem().toString().equals("-")) {
            ReportsRepository rr = new ReportsRepository();
            if (this.jbtninmuebles.isSelected() && this.inmueble != null) {
                rr.GenerarEstadosInmueble(inmueble, codemd5.GetFechaSinHora(this.jDateChooser1.getDate()), codemd5.GetFechaSinHora(this.jDateChooser2.getDate()));
            }
            if (this.jbtnnegocios.isSelected() && this.negocio != null) {
                rr.GenerarEstadosNegocio(negocio, codemd5.GetFechaSinHora(this.jDateChooser1.getDate()), codemd5.GetFechaSinHora(this.jDateChooser2.getDate()));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese Todos los Criterios de Busqueda");
        }
    }//GEN-LAST:event_btngnconglomeradoActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar1;
    private javax.swing.JButton btngnconglomerado;
    private javax.swing.JButton btngnestado;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbcontrib;
    private javax.swing.JComboBox cmbinmneg;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jbtndui;
    private javax.swing.JRadioButton jbtninmuebles;
    private javax.swing.JRadioButton jbtnnegocios;
    private javax.swing.JRadioButton jbtnnit;
    private javax.swing.JRadioButton jbtnnombres;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    // End of variables declaration//GEN-END:variables
}
