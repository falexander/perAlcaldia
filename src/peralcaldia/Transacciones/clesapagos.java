/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.Transacciones;

import controller.AbstractDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Pagos;
import peralcaldia.model.Usuarios;
import util.GCMPNativos;
import util.GenerarBoletaRepository;
/**
 *
 * @author alex
 */
/*Pantalla utilizada para aplicar los pagos enviados por clesa correspondientes a los inmuebles*/
public class clesapagos extends javax.swing.JInternalFrame {
    AbstractDAO dao = new AbstractDAO();
    Inmuebles inmueble = new Inmuebles();
    Pagos pago = new Pagos();
    Usuarios user = new Usuarios();
    Contribuyentes cnt = new Contribuyentes();
    Set<Inmuebles> linm = null;
    BigDecimal val1, val2,res;
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel modeloinmueble = new DefaultComboBoxModel();
    GenerarBoletaRepository bltrep = new GenerarBoletaRepository();
    Double resfies;
    /**
     * Creates new form clesapagos
     */
    public clesapagos() {
        initComponents();
        modeloinmueble.addElement("-");
        cmbcuentainm.setModel(modeloinmueble);
        btnaplicar.setEnabled(false);
        txtctacte.setEnabled(false);
        txtniss.setEnabled(false);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Buscar Inmueble por NISS*/
    public void BuscarporNiss() {
        limpiar();
        try {
            inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, " nis = '" + txtniss.getText() + "'");
            if (inmueble != null) {
                cnt = inmueble.getContribuyentes();
                user = cnt.getUsuarios();
                modeloinmueble.addElement(inmueble.getCuentacorriente());
                lbcontribuyente.setText(user.getNombres() + " " + user.getApellidos());
            }else{
                JOptionPane.showMessageDialog(this, "No hay Inmuebles registrados con el Número de Nis Ingresado");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /*Buscar Inmueble por Cuenta Corriente*/
    public void BuscarporCuentaCorriente() {
        limpiar();
        try {
            inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, " cuentacorriente = '" + txtctacte.getText() + "'");
            if (inmueble != null) {
                cnt = inmueble.getContribuyentes();
                user = cnt.getUsuarios();
                modeloinmueble.addElement(inmueble.getCuentacorriente());
                lbcontribuyente.setText(user.getNombres() + " " + user.getApellidos());
            }else{
                JOptionPane.showMessageDialog(this, "No hay Inmuebles registrados con el Número de Nis Ingresado");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }    
    
    /*Limpiar informacion cargada en pantalla*/
    public void limpiar(){
        componentes.limpiarmodelo2(modeloinmueble);
        lbcontribuyente.setText("");
        lbdirinmueble.setText("");
        lbestado.setText("");
        lbmonto.setText("");
        txtperiodo.setText("");
    }
    
    /*Carga de la direccion del inmueble*/
    public void cargarinmueble(){
        lbdirinmueble.setText(inmueble.getDireccion());
    }
    
    /*Comprobacion del periodo que se desea cancelar*/
    public void comprobarperiodo() {
        if (!txtperiodo.getText().isEmpty()) {
            pago = (Pagos) dao.findByWhereStatementoneobj(Pagos.class, "mespagado = '" + txtperiodo.getText() + "' and inmuebles_id = " + inmueble.getId());
            if (pago != null) {
                if (pago.getEstadosid().getId() == 6) {
                    btnaplicar.setEnabled(true);
                    lbestado.setText("NO CANCELADO");
                    resfies = bltrep.CalcularFiestas(pago.getMonto().doubleValue(), pago.getMespagado());
                    val1 = pago.getMonto().add(new BigDecimal(resfies));
                    lbmonto.setText(val1.setScale(2, RoundingMode.HALF_EVEN).toString());
                } else {
                    btnaplicar.setEnabled(false);
                    lbestado.setText("CANCELADO");
                    lbmonto.setText(pago.getMontopagado().setScale(2, RoundingMode.HALF_EVEN).toString());
                }
            }else{
                JOptionPane.showMessageDialog(this, "El Cobro para el periodo ingresado no ha sido generado");
            }
        }
    }
    
    /*Aplicacion del pago si esta disponible para cancelacion*/
    public void aplicarpago(){
        //Inicialización de Variables.
        Date fecha;
        fecha= new Date();
        Estados est= new Estados();
        est = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5"); 
        
        //Aplicando pagos.
        pago.setFechapago(fecha);
        pago.setComentario("Pago aplicado a través de clesa para el periodo" + " " + txtperiodo.getText());
        pago.setMespagado(txtperiodo.getText());
        pago.setMontopagado(val1.setScale(2, RoundingMode.HALF_EVEN));
        pago.setEstadosid(est);
        pago.setNorecibo("RAES-"+txtperiodo.getText());
        try {
            dao.save(pago);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un Problema en el Guardado de los Datos, por favor intente mas tarde");
            e.printStackTrace();
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
        lbcontribuyente = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbcuentainm = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jbtnniss = new javax.swing.JRadioButton();
        jbtnctate = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        txtniss = new componentesheredados.NumberJTextField();
        jLabel3 = new javax.swing.JLabel();
        txtctacte = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbdirinmueble = new javax.swing.JLabel();
        btncompro = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbmonto = new javax.swing.JLabel();
        lbestado = new javax.swing.JLabel();
        txtperiodo = new componentesheredados.PerJTextField();
        jPanel3 = new javax.swing.JPanel();
        btnaplicar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Contribuyente:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Cuentas Inmueble:");

        cmbcuentainm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcuentainmActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Criterios de Busqueda");

        buttonGroup1.add(jbtnniss);
        jbtnniss.setText("Niss");
        jbtnniss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnissActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtnctate);
        jbtnctate.setText("Cuenta Corriente");
        jbtnctate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnctateActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Niss:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Cuenta Corriente:");

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jbtnniss)
                                .addGap(109, 109, 109)
                                .addComponent(jbtnctate)
                                .addGap(119, 119, 119)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtniss, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtctacte, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbcontribuyente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbcuentainm, 0, 570, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnniss)
                    .addComponent(jbtnctate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtniss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtctacte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbcuentainm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(lbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Ejemplo de periodo a aplicar el pago:");

        jLabel6.setText(" 01-2012,  02-2012,  01-2013 etc......");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Periodo:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Dirección Inmueble:");

        btncompro.setText("Comprobar");
        btncompro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncomproActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Estado:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Monto:");

        try {
            txtperiodo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbdirinmueble, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtperiodo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btncompro, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbmonto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbestado, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lbdirinmueble, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btncompro)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lbestado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbmonto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtperiodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        btnaplicar.setText("Aplicar");
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

        btnlimpiar.setText("Limpiar");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnlimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaplicar)
                    .addComponent(btnsalir)
                    .addComponent(btnlimpiar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbcuentainmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcuentainmActionPerformed
        // TODO add your handling code here:
        if (!cmbcuentainm.getSelectedItem().toString().equals("-")) {
            cargarinmueble();
        }
    }//GEN-LAST:event_cmbcuentainmActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btncomproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncomproActionPerformed
        // TODO add your handling code here:
        comprobarperiodo();
    }//GEN-LAST:event_btncomproActionPerformed

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:
        aplicarpago();
        comprobarperiodo();
    }//GEN-LAST:event_btnaplicarActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void jbtnnissActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnissActionPerformed
        // TODO add your handling code here:
        txtniss.setEnabled(true);
        txtctacte.setEnabled(false);
        txtctacte.setText("");
        limpiar();
    }//GEN-LAST:event_jbtnnissActionPerformed

    private void jbtnctateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnctateActionPerformed
        // TODO add your handling code here:
        txtctacte.setEnabled(true);
        txtniss.setEnabled(false);
        txtniss.setText("");
        limpiar();
    }//GEN-LAST:event_jbtnctateActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        btnaplicar.setEnabled(false);
        if (jbtnniss.isSelected()) {
            BuscarporNiss();
        }else if(jbtnctate.isSelected()){
            BuscarporCuentaCorriente();
        }

    }//GEN-LAST:event_btnbuscarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btncompro;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbcuentainm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jbtnctate;
    private javax.swing.JRadioButton jbtnniss;
    private javax.swing.JLabel lbcontribuyente;
    private javax.swing.JLabel lbdirinmueble;
    private javax.swing.JLabel lbestado;
    private javax.swing.JLabel lbmonto;
    private javax.swing.JTextField txtctacte;
    private componentesheredados.NumberJTextField txtniss;
    private componentesheredados.PerJTextField txtperiodo;
    // End of variables declaration//GEN-END:variables
}
