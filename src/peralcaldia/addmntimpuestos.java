/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;
import controller.AbstractDAO;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Estados;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Montosimpuestos;
import sun.text.normalizer.UCharacter;
import util.codemd5;
/**
 *
 * @author alex
 */
public class addmntimpuestos extends javax.swing.JInternalFrame {
    DefaultTableModel tablam = new DefaultTableModel();
    DefaultTableCellRenderer alinearCentro;    
    AbstractDAO dao = new AbstractDAO();
    /**
     * Creates new form addmntimpuestos
     */
    public addmntimpuestos() {
        initComponents();
        fillcomboboximpuestos();
        tablam.addColumn("IMPUESTO");
        tablam.addColumn("MONTO");
        tablam.addColumn("FECHA INICIO");        
        jtmntimp.setModel(tablam);        
        cargarjtable();
        centrardatos();                
    }

    private void fillcomboboximpuestos(){
        Set<Montosimpuestos> check = null;
        List<Impuestos> limp = null;
        Impuestos impobj = new Impuestos();
        
        limp= dao.findAll(Impuestos.class);        
        Iterator it = limp.iterator();
            while (it.hasNext()) {
            impobj = (Impuestos) it.next();
            check = impobj.getMontosimpuestoses();
                if (check.isEmpty()) {
                       cmbimpuestomnt.addItem(impobj.getNombre());                    
                }                        
            }        
    }
    
    public void cargarjtable(){    
        List<Impuestos> limpt=null;
        Set<Montosimpuestos> lmnt = null;        
        Impuestos impuestosobj= new Impuestos();
        Montosimpuestos mntimobj = new Montosimpuestos();
        Object[] datos = new Object[3];
        
        limpt = dao.findAll(Impuestos.class);
        
        Iterator it = limpt.iterator();
        
        while(it.hasNext()){
            try {
            impuestosobj = (Impuestos) it.next();
            lmnt = impuestosobj.getMontosimpuestoses();
                if (lmnt.isEmpty()) {
                                  
                }
                else{
                    Iterator it1 = lmnt.iterator();
                    datos[0]= impuestosobj.getNombre();
                    while (it1.hasNext()) {
                        mntimobj = (Montosimpuestos) it1.next();
                        datos[1]= "$" + mntimobj.getMonto().toString();
                        datos[2]= mntimobj.getFechainicio().toString();
                    }                    
                    tablam.addRow(datos);                      
                }

                           
            }
            catch (Exception e) 
            {
                JOptionPane.showMessageDialog(this, "Error en la carga de datos");
                e.printStackTrace();
            }
        }
        try {
            jtmntimp.setModel(tablam);                    
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
        jtmntimp.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);
        jtmntimp.getColumnModel().getColumn(1).setHeaderRenderer(alinearCentro);
        jtmntimp.getColumnModel().getColumn(2).setHeaderRenderer(alinearCentro);
        jtmntimp.getColumnModel().getColumn(1).setCellRenderer(alinearCentro);
        jtmntimp.getColumnModel().getColumn(2).setCellRenderer(alinearCentro);
//        jtaddzonas.getColumnModel().getColumn(0).setCellRenderer(alinearCentro);        
    }
    
    public void limpiartabla(){
        try {
            for (int i=jtmntimp.getRowCount()-1; i>=0;i--)
            {
            tablam.removeRow(i);
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error limpiando tabla");
        }
        jtmntimp.setModel(tablam);
    }         
    
    public void guardarmntimpuestos(){
        Montosimpuestos nmntimp = new Montosimpuestos();
        Impuestos impid = new Impuestos();
        Estados stid = new Estados();
        codemd5 isn = new codemd5();
        
        impid = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, "nombre = '"+ cmbimpuestomnt.getSelectedItem().toString() +"'");
        stid = (Estados) dao.findByWhereStatementoneobj(Estados.class, "estado = 'Activo'");
        nmntimp.setEstados(stid);
        nmntimp.setImpuestos(impid);
        nmntimp.setFechainicio(jdfini.getDate());

        
        try {
            if (isn.esnumero(txtmonto.getText())) {
                nmntimp.setMonto(new BigDecimal(txtmonto.getText()));                
                dao.save(nmntimp);
                limpiartabla();
                cmbimpuestomnt.removeAllItems();
                fillcomboboximpuestos();
                cargarjtable();
                centrardatos(); 
                limpiarpantalla();
                JOptionPane.showMessageDialog(this, "Guardado");                

            }
            else{
                JOptionPane.showMessageDialog(this, "Debe de ser un numero con 2 decimales");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al guardar los datos, por favor intente mas tarde");
            e.printStackTrace();                        
        }
    }   
    
    public void limpiarpantalla(){
        txtmonto.setText("");
        jdfini.setDate(null);
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
        cmbimpuestomnt = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtmonto = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jdfini = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtmntimp = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btngrd = new javax.swing.JButton();
        btnsl = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jLabel1.setText("Impuesto:");

        jLabel4.setText("Monto :");

        txtmonto.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbimpuestomnt, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtmonto, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbimpuestomnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtmonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel3.setText("Fecha Inicio de Aplicación del Impuesto:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdfini, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdfini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtmntimp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtmntimp);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117))
        );

        btngrd.setText("Guardar");
        btngrd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngrdActionPerformed(evt);
            }
        });

        btnsl.setText("Salir");
        btnsl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnslActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(btngrd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnsl, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btngrd)
                    .addComponent(btnsl))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnslActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnslActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnslActionPerformed

    private void btngrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngrdActionPerformed
        // TODO add your handling code here:
        guardarmntimpuestos();
    }//GEN-LAST:event_btngrdActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btngrd;
    private javax.swing.JButton btnsl;
    private javax.swing.JComboBox cmbimpuestomnt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdfini;
    private javax.swing.JTable jtmntimp;
    private javax.swing.JTextField txtmonto;
    // End of variables declaration//GEN-END:variables
}
