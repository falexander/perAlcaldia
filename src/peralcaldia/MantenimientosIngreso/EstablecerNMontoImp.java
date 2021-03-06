/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import controller.ImpDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Estados;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Montosimpuestos;
import util.GCMPNativos;
import util.codemd5;

/**
 *
 * @author alex
 */
public class EstablecerNMontoImp extends javax.swing.JInternalFrame {
    
    DefaultComboBoxModel ModeloImpuesto = new DefaultComboBoxModel();
    Impuestos Impuesto = new Impuestos();
    ImpDAO dao = new ImpDAO();
    Montosimpuestos MontoImpAnterior, MontoImpNuevo;
    GCMPNativos componentes = new GCMPNativos();
    /**
     * Creates new form EstablecerNMontoImp
     */
    public EstablecerNMontoImp() {
        initComponents();
        ModeloImpuesto.addElement("-");
        cmbimpuestos.setModel(ModeloImpuesto);
        LlenarComboImpuestos();
        jdappmonto.setDate(new Date());
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    public void LlenarComboImpuestos(){
        List lista = new ArrayList();
        try {
            lista = dao.find_impuestos_whereStatement(" nombre <> 'MORA' and nombre  <> 'FIESTA'");
            componentes.llenarmodelo(ModeloImpuesto, lista);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void CargarDatosMontoActual(){
        Impuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, " nombre= '" + cmbimpuestos.getSelectedItem().toString().toUpperCase() + "'");
        MontoImpAnterior = (Montosimpuestos) dao.findByWhereStatementmaximoobjeto(Montosimpuestos.class, " impuestos_id = " + Impuesto.getId() + "and uso <> 'OBSOLETO'");
        if (MontoImpAnterior != null) {
            lbmonto.setText(MontoImpAnterior.getMonto().toString());
            lbfecha.setText(MontoImpAnterior.getFechainicio().toString());
        }else{
            lbmonto.setText(MontoImpAnterior.getMonto().toString());
            lbfecha.setText(MontoImpAnterior.getFechainicio().toString());
        }
    }
    
    public void GuardarNuevoMonto(){
        Estados Activo, Inactivo;
        List<Montosimpuestos> LTmpMntImp = null;
        LTmpMntImp = dao.findByWhereStatement(Montosimpuestos.class, " fechainicio = '" + codemd5.GetFechaSinHora(jdappmonto.getDate()) + "' and impuestos_id = " + Impuesto.getId() + " and uso = 'NO OBSOLETO'");
        if (LTmpMntImp.size() <= 0 || LTmpMntImp == null) {
            if (!txtmonto.getText().isEmpty()) {
                Activo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 1");
                Inactivo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 4");
                //Inicializando el Nuevo Monto
                MontoImpNuevo = new Montosimpuestos();
                //Cambiando el Estado a Inactivo Y agregando fecha de fin del monto
                MontoImpAnterior.setEstados(Inactivo);
                MontoImpAnterior.setFechafin(codemd5.RestarUnMesAFecha(jdappmonto.getDate()));
                //Creando el Nuevo Monto.
                MontoImpNuevo.setEstados(Activo);
                MontoImpNuevo.setFechainicio(jdappmonto.getDate());
                MontoImpNuevo.setImpuestos(Impuesto);
                MontoImpNuevo.setMonto(new BigDecimal(txtmonto.getText()).setScale(2, RoundingMode.HALF_EVEN));
                MontoImpNuevo.setUso("NO OBSOLETO");
                //Actualizando en el Sistema el Nuevo Monto para la fecha especificada y Deshabilitando y Actualizando el Monto Anterior
                dao.update(MontoImpAnterior);
                dao.save(MontoImpNuevo);
                txtmonto.setText("");
                jdappmonto.setDate(new Date());
                JOptionPane.showMessageDialog(this, "Nueva Monto Asignado");
                CargarDatosMontoActual();
            }
        }else{
            JOptionPane.showMessageDialog(this, "Ya Hay un Monto Registrado Para el Periodo Indicado." + "\n En caso de error ir a Modificar el Monto Para Este Periodo");
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbimpuestos = new javax.swing.JComboBox();
        txtmonto = new componentesheredados.DecimalJTextField();
        jdappmonto = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbmonto = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbfecha = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnaplicar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jLabel1.setText("Impuesto:");

        jLabel2.setText("Monto:");

        jLabel3.setText("Fecha de Inicio:");

        cmbimpuestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbimpuestosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbimpuestos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtmonto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdappmonto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtmonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jdappmonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Datos Impuesto");

        jLabel5.setText("Monto Actual:");

        jLabel6.setText("Fecha Inicio de Aplicación:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbmonto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(30, 30, 30)
                        .addComponent(lbfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(lbmonto, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        btnaplicar.setText("Aplicar");
        btnaplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaplicarActionPerformed(evt);
            }
        });

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaplicar)
                    .addComponent(jButton1))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbimpuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbimpuestosActionPerformed
        // TODO add your handling code here:
        if (cmbimpuestos.getSelectedItem() != null) {
            if (!cmbimpuestos.getSelectedItem().toString().equals("-")) {
                CargarDatosMontoActual();
                txtmonto.setText("");
                jdappmonto.setDate(new Date());
            }else{
                lbmonto.setText("");
                lbfecha.setText("");
                txtmonto.setText("");
                jdappmonto.setDate(new Date());
            }
        }
    }//GEN-LAST:event_cmbimpuestosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:
        GuardarNuevoMonto();
    }//GEN-LAST:event_btnaplicarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JComboBox cmbimpuestos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private com.toedter.calendar.JDateChooser jdappmonto;
    private javax.swing.JLabel lbfecha;
    private javax.swing.JLabel lbmonto;
    private componentesheredados.DecimalJTextField txtmonto;
    // End of variables declaration//GEN-END:variables
}
