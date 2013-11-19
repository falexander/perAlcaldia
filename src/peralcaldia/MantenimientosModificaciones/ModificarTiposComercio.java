/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosModificaciones;

import controller.TpComDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Estados;
import peralcaldia.model.Tiposcomercio;
import util.GCMPNativos;

/**
 *
 * @author alex
 */
/*Pantalla Utilizada para la Modificación de los diferentes tipos de comercio ingresados en la aplicacion*/
public class ModificarTiposComercio extends javax.swing.JInternalFrame {
    DefaultComboBoxModel modeloactividad = new DefaultComboBoxModel();
    DefaultComboBoxModel modelogiro = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloestado = new DefaultComboBoxModel();
    TpComDAO dao = new TpComDAO();
    GCMPNativos componentes = new GCMPNativos();
    Tiposcomercio tipocom = new Tiposcomercio();
    /**
     * Creates new form ModificarTiposComercio
     */
    public ModificarTiposComercio() {
        initComponents();
        modeloactividad.addElement("-");
        modelogiro.addElement("-");
        modeloestado.addElement("-");
        cmbactividad.setModel(modeloactividad);
        cmbestado.setModel(modeloestado);
        cmbgiro.setModel(modelogiro);
        cmbgiro.setEnabled(false);
        LlenarGiros();
        LlenarEstado();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Carga de los Giros Ingresados en el sistema y que se encuentran activos*/
    public void LlenarGiros() {
        List lgr = null;
        /*Buscando Giros*/
        lgr = dao.findByWhereStatement("select descripcion from Giros ");
        if (lgr.size() > 0 && !lgr.isEmpty()) {
            componentes.limpiarmodelo2(modelogiro);
            componentes.llenarmodelo(modelogiro, lgr);
        }
    }
    
    /*Carga de los estados validos para los tipos de comercio*/
    public void LlenarEstado(){
        List lista = null;
        try {
            lista = dao.findByWhereStatement("Select estado from Estados where id = 1 or id = 4");
            if (lista.size() > 0 && !lista.isEmpty()) {
                componentes.llenarmodelo(modeloestado, lista);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /*Busqueda de los tipos de comercio por nombre*/
    public void BuscarActividad(){
        List lista = null;
        try {
            /*Buscando Los Tipos de Comercio*/
            if (!txtactecon.getText().isEmpty()) {
                lista = dao.find_tiposcomercio_whereStatement(" actividadeconomica like '%" + txtactecon.getText().toUpperCase() + "%'");
                if (lista.size() > 0 && !lista.isEmpty()) {
                    componentes.limpiarmodelo2(modeloactividad);
                    componentes.llenarmodelo(modeloactividad, lista);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Ingresen el Criterio de Busqueda");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /*Carga de los Datos de la seleccion*/
    public void CargarDatos(){
        tipocom = new Tiposcomercio();
        try {
            tipocom = (Tiposcomercio) dao.findByWhereStatementoneobj(Tiposcomercio.class, " actividadeconomica = '" + cmbactividad.getSelectedItem().toString() + "'");
            if (tipocom != null) {
                txtdescripcion.setText(tipocom.getActividadeconomica());
                cmbgiro.setSelectedItem(tipocom.getGiros().getDescripcion());
                cmbestado.setSelectedItem(tipocom.getEstados().getEstado());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /*Modificacion de los tipos si cumplen con todos los requisitos*/
    public void ModificarTipo() {
        Tiposcomercio temporal = new Tiposcomercio();
        Estados estado = new Estados();
        try {
            if (tipocom != null) {
                if (!txtdescripcion.getText().isEmpty() && !cmbestado.getSelectedItem().toString().equals("-")) {
                    /*Recuperando el estado seleccionado*/
                    estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, " estado = '" + cmbestado.getSelectedItem().toString() + "'");
                    if (!txtdescripcion.getText().toUpperCase().equals(tipocom.getActividadeconomica())) {
                        temporal = (Tiposcomercio) dao.findByWhereStatementoneobj(Tiposcomercio.class, " actividadeconomica = '" + txtdescripcion.getText().toUpperCase() + "'");
                        if (temporal == null) {
                            tipocom.setActividadeconomica(txtdescripcion.getText().toUpperCase());
                        } else {
                            JOptionPane.showMessageDialog(this, "El Valor Ingresado ya se Encuentra Asignado \n Verificar Antes de Modificar");
                            return;
                        }
                    }
                    tipocom.setEstados(estado);
                    /*Actualizando la Instancia*/
                    dao.update(tipocom);
                    txtactecon.setText(txtdescripcion.getText().toUpperCase());
                    txtdescripcion.setText("");
                    BuscarActividad();
                    cmbactividad.setSelectedItem(txtactecon.getText());
                    JOptionPane.showMessageDialog(this, "Modificacion Completada");
                } else {
                    JOptionPane.showMessageDialog(this, "Antes de Modificar Debe de Seleccionarse todos los Campos Obligatorios");
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtactecon = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbactividad = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbgiro = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtdescripcion = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnmodificar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Actividad Economica:");

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
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtactecon, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtactecon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 437, -1));

        jLabel2.setText("Actividad:");

        cmbactividad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbactividadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(cmbactividad, 0, 351, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbactividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 51, 437, -1));

        jLabel3.setText("Giro:");

        jLabel4.setText("Estado:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Datos Actuales Tipos de Comercio");

        jLabel6.setText("Descripción Actividad:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(cmbgiro, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(cmbestado, 0, 157, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtdescripcion)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbgiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtdescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 99, 437, -1));

        btnmodificar.setText("Modificar");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
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
                .addGap(72, 72, 72)
                .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnmodificar)
                    .addComponent(btnsalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        BuscarActividad();
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void cmbactividadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbactividadActionPerformed
        // TODO add your handling code here:
        if (cmbactividad.getSelectedItem() != null) {
            CargarDatos();
        }
    }//GEN-LAST:event_cmbactividadActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        // TODO add your handling code here:
        ModificarTipo();
        CargarDatos();
    }//GEN-LAST:event_btnmodificarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JComboBox cmbactividad;
    private javax.swing.JComboBox cmbestado;
    private javax.swing.JComboBox cmbgiro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField txtactecon;
    private javax.swing.JTextField txtdescripcion;
    // End of variables declaration//GEN-END:variables
}
