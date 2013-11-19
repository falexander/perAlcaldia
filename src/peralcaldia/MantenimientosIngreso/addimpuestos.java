/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import beans.VerImpuestos;
import javax.swing.table.DefaultTableModel;
import controller.AbstractDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.processing.RoundEnvironment;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import peralcaldia.model.Estados;
import peralcaldia.model.Formulas;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Montosimpuestos;
import util.GCMPNativos;
import util.codemd5;
/**
 *
 * @author alex
 */
/*Pantalla Utilizada para Ingresar Nuevos Impuestos al Sistema Junto con sus Caracteristicas Respectivas*/
public class addimpuestos extends javax.swing.JInternalFrame {
    AbstractDAO dao = new AbstractDAO();
    DefaultTableModel tablam = new DefaultTableModel();
    GCMPNativos componentes = new GCMPNativos();
    /**
     * Creates new form addimpuestos
     */
    public addimpuestos() {
        initComponents();
        cargarjtable();
        jdiniformula.setDate(new Date());
        jdmntimp.setDate(new Date());
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }

    /*Metodo utilizado para cargar la información referente a los Impuestos en el sistema junto con la formulas y 
     * Montos Actuales*/
    public void cargarjtable(){    
        List object = new ArrayList();
        List<Impuestos> limpuestos = null;
        VerImpuestos obj = new VerImpuestos();
        Impuestos impuesto = new Impuestos();
        Formulas formula = new Formulas();
        Montosimpuestos mntimpuesto = new Montosimpuestos();
        
        try {
            limpuestos = dao.findAll(Impuestos.class);
            if (limpuestos.size() > 0 || !limpuestos.isEmpty()) {
                Iterator impiterador = limpuestos.iterator();
                while (impiterador.hasNext()) {
                    obj = new VerImpuestos();
                    impuesto = (Impuestos) impiterador.next();
                    mntimpuesto = (Montosimpuestos) dao.findByWhereStatementmaximoobjeto(Montosimpuestos.class, " impuestos_id = " + impuesto.getId() + " and uso <> 'OBSOLETO'");
                    formula = (Formulas) dao.findByWhereStatementmaximoobjeto(Formulas.class, " impuestos_id = " + impuesto.getId() + " and uso <> 'OBSOLETO'");
                    if (impuesto.getNombre().equals("MORA") || impuesto.getNombre().equals("FIESTA")) {
                        obj.setMONTO_ACTUAL(BigDecimal.ZERO);
                    } else{
                        obj.setMONTO_ACTUAL(mntimpuesto.getMonto());
                    }
                    obj.setFORMULA(formula.getFormula());
                    obj.setNOMBRE_IMPUESTO(impuesto.getNombre());
                    object.add(obj);
                }
            }
            jtaddimpuestos.setModel(componentes.llenartabla(tablam, object, VerImpuestos.class));
            componentes.centrarcabeceras(jtaddimpuestos);
            componentes.centrarcolumnasdesde2(jtaddimpuestos, 2);
        } catch (Exception e) {
            System.out.println(e.toString());
        }        
    }
    
    /*Metodo utilizado para comprobar la formula ingresada por el usuario*/
    public void comprobarformula() {
        if (!txtformula.getText().equals("") || !txtformula.getText().isEmpty()) {
            if (codemd5.evaluarformula(txtformula.getText())) {
                JOptionPane.showMessageDialog(this, "Formato sin Errores, Formula Valida");
            } else {
                JOptionPane.showMessageDialog(this, "Formato de la Formula con Errores, Revisar antes de Proceder a Guardar");
            }
        }
    }
    
    /*Metodo Utilizado para Verificar, Inicializar, Setear las propiedades y Guardar el Nuevo impuesto
     * en caso de cumplir todos los requisitos*/
    public void guardarimpuestos(){
        Impuestos nimpuesto = new Impuestos();
        Montosimpuestos mntimp = new Montosimpuestos();
        Formulas fr = new Formulas();
        Estados est = new Estados();
        est = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 1");
        
        if (!txtnimpuesto.getText().isEmpty() && !txtmontoimpuestos.getText().isEmpty() && !txtformula.getText().isEmpty()) {
            nimpuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, " nombre = '" + txtnimpuesto.getText().toUpperCase() + "'");
            if (nimpuesto == null) {
                nimpuesto = new Impuestos();
                nimpuesto.setNombre(txtnimpuesto.getText().toUpperCase());
            }else{
                JOptionPane.showMessageDialog(this, "El Nombre del Nuevo Impuesto ya se Encuentra en Uso, Favor Verificar ");
                return;
            }
            
            if (!txtformula.getText().equals("0") && !txtformula.getText().isEmpty()) {
                if (!codemd5.evaluarformula(txtformula.getText())) {
                    JOptionPane.showMessageDialog(this, "Formato de la Formula con Errores, Revisar Antes de Proceder a Guardar");
                    return;
                }else{
                    fr = new Formulas();
                    fr.setEstados(est);
                    fr.setFechainicio(codemd5.getmesanio(jdiniformula.getDate()));
                    fr.setFormula(txtformula.getText());
                    fr.setImpuestos(nimpuesto);
                    fr.setUso("NO OBSOLETO");
                }
            }else{
                fr = new Formulas();
                fr.setEstados(est);
                fr.setFechainicio(codemd5.getmesanio(jdiniformula.getDate()));
                fr.setFormula("0");
                fr.setImpuestos(nimpuesto);
                fr.setUso("NO OBSOLETO");
            }            
            
            if (!txtmontoimpuestos.getText().equals("0")) {
                Date fechamonto;
                fechamonto = codemd5.obteniendoprimerdiames(jdmntimp.getDate());
                mntimp = new Montosimpuestos();
                mntimp.setEstados(est);
                mntimp.setFechainicio(fechamonto);
                mntimp.setImpuestos(nimpuesto);
                mntimp.setMonto(new BigDecimal(txtmontoimpuestos.getText()));
                mntimp.setUso("NO OBSOLETO");
            }else{
                JOptionPane.showMessageDialog(this, "El Monto del Impuesto debe de ser Diferente de 0");
                return;
            }

            dao.save(nimpuesto);
            dao.save(fr);
            dao.save(mntimp);
            limpiarpantalla();
            cargarjtable();
            
        }
        
    }
    
    /*Metodo para limpiar la informacion en pantalla*/
    public void limpiarpantalla(){
        txtnimpuesto.setText("");
        txtformula.setText("");
        txtmontoimpuestos.setText("");
        jdiniformula.setDate(new Date());
        jdmntimp.setDate(new Date());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaddimpuestos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtformula = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnformula = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jdiniformula = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jdmntimp = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        txtmontoimpuestos = new componentesheredados.DecimalJTextField();
        jPanel4 = new javax.swing.JPanel();
        btnguardar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtnimpuesto = new componentesheredados.LettersJTextField();

        jLabel3.setText("jLabel3");

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        jtaddimpuestos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtaddimpuestos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Codigos Para Fórmulas:");

        jLabel4.setText("Metros Lineales = ML");

        jLabel5.setText("Metros Cuadrados = MC");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("* Formula:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Ejemplo Formula Aseo: ");

        jLabel9.setText(" (MC * MONTO) / 8.75");

        jLabel10.setText("El valor asignado del impuesto = MONTO (Impuestos  Basicos)");

        jLabel6.setText("En el caso de MORA y FIESTA  No Ingresar Valor de Monto en");

        jLabel11.setText("este caso el Monto es calculado por el sistema.");

        jLabel12.setText("MONTO :");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("NOTA: Si el Impuesto Tendra un Monto Fijo (Sin Formula) colocar 0 en Formula");

        btnformula.setText("Comprobar Formula");
        btnformula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnformulaActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("* Fecha Inicio de Aplicacion de la Formula :");

        jLabel17.setText("NM = Numero de Meses (SOLO PARA FORMULAS DE MORA)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(263, 263, 263))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtformula, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jdiniformula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(0, 56, Short.MAX_VALUE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(239, 239, 239)
                                .addComponent(btnformula, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(4, 4, 4)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel16))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtformula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdiniformula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnformula)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("* Monto Inicial Impuestos");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("* Fecha Inicio de Aplicacion del Monto:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(txtmontoimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdmntimp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(0, 77, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdmntimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtmontoimpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnguardar.setText("Guardar");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
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
                .addGap(138, 138, 138)
                .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(146, 146, 146))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnsalir))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jLabel1.setText("* Impuesto:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtnimpuesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtnimpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        // TODO add your handling code here:
        guardarimpuestos();        
    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnformulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnformulaActionPerformed
        // TODO add your handling code here:
        if (!txtformula.getText().equals("0")) {
            comprobarformula();
        }
    }//GEN-LAST:event_btnformulaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnformula;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdiniformula;
    private com.toedter.calendar.JDateChooser jdmntimp;
    private javax.swing.JTable jtaddimpuestos;
    private javax.swing.JTextField txtformula;
    private componentesheredados.DecimalJTextField txtmontoimpuestos;
    private componentesheredados.LettersJTextField txtnimpuesto;
    // End of variables declaration//GEN-END:variables
}
