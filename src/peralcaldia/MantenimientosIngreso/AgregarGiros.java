/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import beans.VerGiros;
import controller.GrDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Estados;
import peralcaldia.model.Giros;
import util.GCMPNativos;

/**
 *
 * @author alex
 */
/*Pantalla Destinado al Guardado de Nuevos Giros Comerciales*/
public class AgregarGiros extends javax.swing.JInternalFrame {

    DefaultTableModel tablam = new DefaultTableModel();
    GCMPNativos componentes = new GCMPNativos();
    Giros giro = new Giros();
    List otls = new ArrayList();
    GrDAO dao = new GrDAO();
    /**
     * Creates new form AgregarGiros
     */
    public AgregarGiros() {
        initComponents();
        LlenarTabla();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Metodo Utilizado para la Carga de Informacion en el Jtable Correspondiente*/
    public void LlenarTabla(){
        List<Giros> lgiros = null;
        Giros temporalgiros = new Giros();
        VerGiros vg = new VerGiros();
        try {
            lgiros = dao.findAll(Giros.class);
            if (lgiros.size() > 0 && !lgiros.isEmpty()) {
                Iterator it = lgiros.iterator();
                otls = new ArrayList();
                while (it.hasNext()) {
                    temporalgiros = (Giros) it.next();
                    vg = new VerGiros();
                    vg.setNOMBRE_GIRO(temporalgiros.getDescripcion());
                    vg.setESTADO(temporalgiros.getEstados().getEstado());
                    otls.add(vg);
                }
            }
            tablam = componentes.limpiartabla(tablam);
            tablam = componentes.llenartabla(tablam, otls, VerGiros.class);
            jtaddgiros.setModel(tablam);
            componentes.centrarcabeceras(jtaddgiros);
        } catch (Exception e) {
            System.out.println(e.toString());
        }        
    }

    /*Metodo encargado de verificar las restriccion y realizar la creacion y guardado de la nueva 
     * instancia*/
    public void GuardarNuevoGiro(){
        Giros tmpgiro = new Giros();
        Estados estado = new Estados();
        if (!txtgiro.getText().isEmpty()) {
            tmpgiro = (Giros) dao.findByWhereStatementoneobj(Giros.class, " descripcion = '" + txtgiro.getText().toUpperCase() + "'");
            estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 1");
            if (tmpgiro == null) {
                giro = new Giros();
                giro.setDescripcion(txtgiro.getText().toUpperCase());
                giro.setEstados(estado);
                dao.save(giro);
                JOptionPane.showMessageDialog(this, "Guardado Completo");
                txtgiro.setText("");
            }else{
                JOptionPane.showMessageDialog(this, "El Nombre Ingresado ya se encuentra en Uso \n Favor Verificar Antes de Guardar");
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtgiro = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaddgiros = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btngzona = new javax.swing.JButton();
        btnszona = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre N. Giro:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(txtgiro, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtgiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 189, 491, -1));

        jPanel1.setAutoscrolls(true);

        jtaddgiros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtaddgiros);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btngzona.setText("Guardar");
        btngzona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngzonaActionPerformed(evt);
            }
        });

        btnszona.setText("Salir");
        btnszona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnszonaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(btngzona, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96)
                .addComponent(btnszona, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btngzona)
                    .addComponent(btnszona))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 237, 491, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btngzonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngzonaActionPerformed
        // TODO add your handling code here:
        if (!txtgiro.getText().isEmpty()) {
            GuardarNuevoGiro();
            LlenarTabla();
        }else{
            JOptionPane.showMessageDialog(this, "Ingresar el Nombre del Nuevo Giro Antes de Guardar");
        }
        
    }//GEN-LAST:event_btngzonaActionPerformed

    private void btnszonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnszonaActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnszonaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btngzona;
    private javax.swing.JButton btnszona;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtaddgiros;
    private javax.swing.JTextField txtgiro;
    // End of variables declaration//GEN-END:variables
}
