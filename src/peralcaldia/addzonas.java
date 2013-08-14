/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import javax.swing.table.DefaultTableModel;
import controller.AbstractDAO;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import peralcaldia.model.Zonas;
/**
 *
 * @author alex
 */
public class addzonas extends javax.swing.JInternalFrame {
    DefaultTableModel tablam = new DefaultTableModel();
    DefaultTableCellRenderer alinearCentro;
    AbstractDAO dao = new AbstractDAO();
    
    /**
     * Creates new form addzonas
     */
    public addzonas() {
        initComponents();
        tablam.addColumn("ZONAS");
        jtaddzonas.setModel(tablam);        
        cargarjtable();
        centrardatos();
    }
    
    public void cargarjtable(){    
        List<Zonas> lzon=null;
        Zonas zonesobj= new Zonas();
        Object[] datos = new Object[1];
        lzon = dao.findAll(Zonas.class);
        
        Iterator it = lzon.iterator();
        
        while(it.hasNext()){
            try {
            zonesobj = (Zonas) it.next();
            datos[0]= zonesobj.getZona();
            tablam.addRow(datos);                            
            }
            catch (Exception e) 
            {
                JOptionPane.showMessageDialog(this, "Error en la carga de datos");
                e.printStackTrace();
            }
        }
        try {
            jtaddzonas.setModel(tablam);                    
        } 
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Error en la carga");
            e.printStackTrace();
        }        
    }
    
    public void centrardatos(){
        alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        jtaddzonas.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);
//        jtaddzonas.getColumnModel().getColumn(0).setCellRenderer(alinearCentro);        
    }
    
    public void limpiartabla(){
        try {
            for (int i=jtaddzonas.getRowCount()-1; i>=0;i--)
            {
            tablam.removeRow(i);
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error limpiando tabla");
        }
        jtaddzonas.setModel(tablam);
    }
    
    public void guardarzona(){
        Zonas nzone = new Zonas();
        
        nzone.setZona(txtzona.getText().toUpperCase());
        
        try {
            Zonas otz = new Zonas();
            otz = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, "zona ='" + txtzona.getText().toUpperCase() + "'");
            if (otz==null){
                dao.save(nzone);
                limpiartabla();             
                cargarjtable();
                centrardatos();
                JOptionPane.showMessageDialog(this, "Guardado Completo");                            
            }
            else{
                JOptionPane.showMessageDialog(this, "La zona ya existe");
            }                
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "Error al guardar la información");
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

        jPanel1 = new javax.swing.JPanel();
        txtzona = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaddzonas = new javax.swing.JTable();
        btngzona = new javax.swing.JButton();
        btnszona = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setAutoscrolls(true);

        jtaddzonas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtaddzonas);

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

        jLabel1.setText("Zona:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btngzona, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(btnszona, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtzona))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnszona)
                    .addComponent(btngzona))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btngzonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngzonaActionPerformed
        // TODO add your handling code here:
        guardarzona();
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtaddzonas;
    private javax.swing.JTextField txtzona;
    // End of variables declaration//GEN-END:variables
}
