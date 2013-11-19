/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import controller.ImpDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Estados;
import peralcaldia.model.Formulas;
import peralcaldia.model.Impuestos;
import util.GCMPNativos;
import util.codemd5;

/**
 *
 * @author alex
 */
/*Pantalla Destinada al Establecimiento de Nuevas Formulas para los Impuestos en nuevos periodos*/
public class EstablecerNForm extends javax.swing.JInternalFrame {
    
    ImpDAO dao = new ImpDAO();
    Impuestos impuesto = new Impuestos();
    GCMPNativos componentes = new GCMPNativos();
    Formulas forant, foract;
    DefaultComboBoxModel modeloimpuesto = new DefaultComboBoxModel();
    /**
     * Creates new form EstablecerNForm
     */
    public EstablecerNForm() {
        initComponents();
        cmbimpuestos.setModel(modeloimpuesto);
        FillComboImpuestos();
        jdfechaapp.setDate(new Date());
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Metodo para realizar el llenado de los Impuestos Existentes en el sistema*/
    public void FillComboImpuestos(){
        List lista = new ArrayList();
        try {
            lista = dao.find_all_impuestos();
            componentes.llenarmodelo(modeloimpuesto, lista);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /*Metodo para Realizar la Carga de los Datos de la Formula que se Encuentra en Uso*/
    public void CargarDatosFormula(){
        impuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, " nombre= '" + cmbimpuestos.getSelectedItem().toString().toUpperCase() + "'");
        forant = (Formulas) dao.findByWhereStatementmaximoobjeto(Formulas.class, "impuestos_id = " + impuesto.getId() + " and uso <> 'OBSOLETO'");
        if (forant != null) {
            lbformula.setText(forant.getFormula());
            lbfecha.setText(forant.getFechainicio());
        }else{
            JOptionPane.showMessageDialog(this, "El Impuesto Seleccionado no Posee Información de Formulas Relacionadas al Mismo en la fecha actual " + "\n Realizar Verificación");
        }
    }
    
    /*Metodo para realizar la comprobacion de la formula ingresada por el usuario.*/
    public void ComprobarFormula() {
        if (!txtnewformula.getText().equals("") || !txtnewformula.getText().isEmpty()) {
            if (codemd5.evaluarformula(txtnewformula.getText())) {
                JOptionPane.showMessageDialog(this, "Formato sin Errores, Formula Valida");
            } else {
                JOptionPane.showMessageDialog(this, "Formato de la Formula con Errores, Revisar antes de Proceder a Guardar");
            }
        }
    }
    
    /*Metodo utilizado para el guardado de la nueva formula en caso de cumplir con todos los requisitos*/
    public void GuardarNuevaFormula() {
        Estados activo, inactivo;
        List<Formulas> tmplform = null;
        tmplform = dao.findByWhereStatement(Formulas.class, " fechainicio = '" + codemd5.getmesanio(jdfechaapp.getDate()) + "' and impuestos_id = " + impuesto.getId() + " and uso = 'NO OBSOLETO'");
        if (tmplform.size() <= 0) {
            if (!txtnewformula.getText().isEmpty()) {
                if (codemd5.evaluarformula(txtnewformula.getText())) {
                    activo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 1");
                    inactivo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 4");
                    foract = new Formulas();
                    //Cambiando el estado a inactivo y agregando fecha de fin de validez
                    forant.setEstados(inactivo);
                    forant.setFechafin(codemd5.GetMesAnteriorAnio(jdfechaapp.getDate()));
                    //Creando la nueva formula.
                    foract.setEstados(activo);
                    foract.setFechainicio(codemd5.getmesanio(jdfechaapp.getDate()));
                    foract.setFormula(txtnewformula.getText());
                    foract.setImpuestos(impuesto);
                    foract.setUso("NO OBSOLETO");

                    //Actualizando el impuesto a la nueva formula en uso.
                    dao.update(forant);
                    dao.save(foract);
                    txtnewformula.setText("");
                    jdfechaapp.setDate(new Date());
                    JOptionPane.showMessageDialog(this, "Nueva Formula asignada con exito");
                    CargarDatosFormula();
                } else {
                    JOptionPane.showMessageDialog(this, "Error en el Formato de la Formula Favor Comprobar antes de Guardar");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ya hay una Formula registrada para el periodo." + "\n En caso de error ir a Modificar Formula");
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
        txtnewformula = new javax.swing.JTextField();
        jdfechaapp = new com.toedter.calendar.JDateChooser();
        jpdatosimpuesto = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbformula = new javax.swing.JLabel();
        lbfecha = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnaplicar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        btnprobar = new javax.swing.JButton();

        jLabel1.setText("Impuesto:");

        jLabel2.setText("Formula:");

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jdfechaapp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnewformula, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtnewformula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jdfechaapp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Datos Impuesto");

        jLabel5.setText("Formula Actual :");

        jLabel6.setText("Fecha Inicio de Aplicación:");

        javax.swing.GroupLayout jpdatosimpuestoLayout = new javax.swing.GroupLayout(jpdatosimpuesto);
        jpdatosimpuesto.setLayout(jpdatosimpuestoLayout);
        jpdatosimpuestoLayout.setHorizontalGroup(
            jpdatosimpuestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpdatosimpuestoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpdatosimpuestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpdatosimpuestoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(lbformula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpdatosimpuestoLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpdatosimpuestoLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(lbfecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpdatosimpuestoLayout.setVerticalGroup(
            jpdatosimpuestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpdatosimpuestoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpdatosimpuestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(lbformula, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpdatosimpuestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpdatosimpuestoLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 7, Short.MAX_VALUE))
                    .addComponent(lbfecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        btnprobar.setText("Probar Formula");
        btnprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprobarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnprobar)
                .addGap(26, 26, 26)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaplicar)
                    .addComponent(btnsalir)
                    .addComponent(btnprobar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpdatosimpuesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpdatosimpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbimpuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbimpuestosActionPerformed
        // TODO add your handling code here:
        if (cmbimpuestos.getSelectedItem() != null) {
            if (!cmbimpuestos.getSelectedItem().toString().equals("-")) {
                CargarDatosFormula();
                txtnewformula.setText("");
                jdfechaapp.setDate(new Date());
            }else{
                lbformula.setText("");
                lbfecha.setText("");
                txtnewformula.setText("");
                jdfechaapp.setDate(new Date());
            }
        }
    }//GEN-LAST:event_cmbimpuestosActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btnprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprobarActionPerformed
        // TODO add your handling code here:
        ComprobarFormula();
    }//GEN-LAST:event_btnprobarActionPerformed

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:
        GuardarNuevaFormula();
    }//GEN-LAST:event_btnaplicarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JButton btnprobar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JComboBox cmbimpuestos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.toedter.calendar.JDateChooser jdfechaapp;
    private javax.swing.JPanel jpdatosimpuesto;
    private javax.swing.JLabel lbfecha;
    private javax.swing.JLabel lbformula;
    private javax.swing.JTextField txtnewformula;
    // End of variables declaration//GEN-END:variables
}
