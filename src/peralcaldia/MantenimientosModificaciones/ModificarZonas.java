/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosModificaciones;

import controller.ZnDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Zonas;
import util.GCMPNativos;

/**
 *
 * @author alex
 */
/*Panta utilizada para modificar las zonas ingresadas en el sistema*/
public class ModificarZonas extends javax.swing.JInternalFrame {
    DefaultComboBoxModel modelozonas = new DefaultComboBoxModel();
    Zonas zona = new Zonas();
    GCMPNativos componentes = new GCMPNativos();
    ZnDAO dao = new ZnDAO();
    
    /**
     * Creates new form ModificarZonas
     */
    public ModificarZonas() {
        initComponents();
        modelozonas.addElement("-");
        cmbzonas.setModel(modelozonas);
        txtninmuebles.setEnabled(false);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Buscar Zonas por Descripcion*/
    public void BuscarporDescripcion(){
        List lista = null;
        try {
            if (!txtabuscar.getText().isEmpty()) {
                lista = dao.find_zonas_whereStatement(" zona like '%" + txtabuscar.getText().toUpperCase() + "%'");
                if (lista.size() > 0 && !lista.isEmpty()) {
                    componentes.llenarmodelo(modelozonas, lista);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Ingresar el criterio de Busqueda");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /*Buscar Zonas por Codigo*/
    public void BuscarporCodigo(){
        List lista = null;
        try {
            if (!txtabuscar.getText().isEmpty()) {
                lista = dao.find_zonas_whereStatement(" zone_code like '%" + txtabuscar.getText().toUpperCase() + "%'");
                if (lista.size() > 0 && !lista.isEmpty()) {
                    componentes.llenarmodelo(modelozonas, lista);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Ingresar el criterio de Busqueda");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }
    
    /*Limpiar Pantalla*/
    public void LimpiarPantalla(){
        txtcodigozona.setText("");
        txtdesczona.setText("");
        txtninmuebles.setText("");
        componentes.limpiarmodelo2(modelozonas);
        if (cmbzonas.getSelectedItem() != null) {
            cmbzonas.setSelectedIndex(0);
        }
    }
    
    /*Carga de Datos de la Zona Seleccionada*/
    public void CargarDatosZona(){
        try {
            zona = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, " zona = '" + cmbzonas.getSelectedItem().toString() + "'");
            if (zona != null) {
                txtcodigozona.setText(zona.getZonecode());
                txtdesczona.setText(zona.getZona());
                txtninmuebles.setText(Integer.toString(zona.getCorrelativozona()));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /*Aplicar la Modificacion a la Zona Seleccionada*/
    public void ModificarZona() {
        Zonas tmpzone = new Zonas();
        try {
            if (zona != null) {
                /*Comprobando si hay Cambio en la Descripción de la Zona*/
                if (!txtdesczona.getText().toString().equals(zona.getZona())) {
                    tmpzone = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, " zona = '" + txtdesczona.getText().toUpperCase() + "'");
                    if (tmpzone == null) {
                        zona.setZona(txtdesczona.getText().toUpperCase());
                    } else {
                        JOptionPane.showMessageDialog(this, "El nombre de la Zona ya se encuentra en uso \n Favor Revisar Antes de Guardar");
                        return;
                    }
                }
                /*Comprobando si hay Cambio en el Código de la Zona*/
                if (!txtcodigozona.getText().toString().equals(zona.getZonecode())) {
                    tmpzone = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, " zone_code = '" + txtcodigozona.getText().toUpperCase() + "'");
                    if (tmpzone == null) {
                        zona.setZonecode(txtcodigozona.getText().toUpperCase());
                    } else {
                        JOptionPane.showMessageDialog(this, "El Codigo Ingresado ya se encuentra en uso \n Favor Revisar Antes de Guardar");
                        return;
                    }
                }
                /*Actualizando la Zona*/
                dao.update(zona);
                JOptionPane.showMessageDialog(this, "Actualización Completada");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jbtnnombre = new javax.swing.JRadioButton();
        jbtncodigo = new javax.swing.JRadioButton();
        txtabuscar = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbzonas = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtdesczona = new javax.swing.JTextField();
        txtcodigozona = new javax.swing.JTextField();
        txtninmuebles = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnmodificar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        buttonGroup1.add(jbtnnombre);
        jbtnnombre.setText("Nombre");
        jbtnnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombreActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtncodigo);
        jbtncodigo.setText("Codigo");
        jbtncodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtncodigoActionPerformed(evt);
            }
        });

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
                    .addComponent(txtabuscar)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbtnnombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtncodigo)
                        .addGap(18, 18, 18)
                        .addComponent(btnbuscar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnnombre)
                    .addComponent(jbtncodigo)
                    .addComponent(btnbuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtabuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Zona:");

        cmbzonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbzonasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(cmbzonas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbzonas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Datos de la Zona:");

        jLabel3.setText("Descripcion:");

        jLabel4.setText("Codigo:");

        jLabel5.setText("N° de Inmuebles:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtdesczona)
                            .addComponent(txtcodigozona)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(26, 26, 26)
                        .addComponent(txtninmuebles)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtdesczona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtcodigozona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtninmuebles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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
                .addGap(41, 41, 41)
                .addComponent(btnmodificar)
                .addGap(31, 31, 31)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnmodificar)
                    .addComponent(btnsalir))
                .addContainerGap(15, Short.MAX_VALUE))
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
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnombreActionPerformed
        // TODO add your handling code here:
        txtabuscar.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnnombreActionPerformed

    private void jbtncodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtncodigoActionPerformed
        // TODO add your handling code here:
        txtabuscar.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtncodigoActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        LimpiarPantalla();
        if (jbtnnombre.isSelected()) {
            BuscarporDescripcion();
        }else if(jbtncodigo.isSelected()){
            BuscarporCodigo();
        }
        txtcodigozona.setText("");
        txtdesczona.setText("");
        txtninmuebles.setText("");
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void cmbzonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbzonasActionPerformed
        // TODO add your handling code here:
        if (cmbzonas.getSelectedItem() != null) {
            if (!cmbzonas.getSelectedItem().toString().equals("-")) {
                CargarDatosZona();
            }else{
                txtcodigozona.setText("");
                txtdesczona.setText("");
                txtninmuebles.setText("");
            }
        }
    }//GEN-LAST:event_cmbzonasActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        // TODO add your handling code here:
        if (zona != null && !txtcodigozona.getText().isEmpty() && !txtdesczona.getText().isEmpty()) {
            if (jbtnnombre.isSelected()) {
                ModificarZona();
                txtabuscar.setText(txtdesczona.getText().toUpperCase());
                componentes.limpiarmodelo2(modelozonas);
                BuscarporDescripcion();
                cmbzonas.setSelectedItem(txtabuscar.getText());
                CargarDatosZona();
            }else if(jbtncodigo.isSelected()){
                String cad;
                ModificarZona();
                txtabuscar.setText(txtcodigozona.getText().toUpperCase());
                cad = txtdesczona.getText().toUpperCase();
                componentes.limpiarmodelo2(modelozonas);
                BuscarporCodigo();                
                cmbzonas.setSelectedItem(cad);
                CargarDatosZona();
            }
            
        }else{
            JOptionPane.showMessageDialog(this, "Elija la Zona a Modificar");
        }        
    }//GEN-LAST:event_btnmodificarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbzonas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jbtncodigo;
    private javax.swing.JRadioButton jbtnnombre;
    private javax.swing.JTextField txtabuscar;
    private javax.swing.JTextField txtcodigozona;
    private javax.swing.JTextField txtdesczona;
    private javax.swing.JTextField txtninmuebles;
    // End of variables declaration//GEN-END:variables
}
