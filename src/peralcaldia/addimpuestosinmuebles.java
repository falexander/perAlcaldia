/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Impuestosinmuebles;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Usuarios;

/**
 *
 * @author alex
 */
public class addimpuestosinmuebles extends javax.swing.JInternalFrame {
    AbstractDAO dao = new AbstractDAO();
    DefaultTableModel tablam = new DefaultTableModel();
    DefaultTableCellRenderer alinearCentro;
    /**
     * Creates new form addimpuestosinmuebles
     */
    public addimpuestosinmuebles() {
        initComponents();
        fillComboBoxcontribuyente();
        tablam.addColumn("IMPUESTOS ASOCIADOS");
        jtiminm.setModel(tablam); 
        centrardatos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    private void fillComboBoxcontribuyente(){
        List<Usuarios> lusu=null;
        Usuarios usuobj = new Usuarios();
        lusu = dao.findByWhereStatement(Usuarios.class, "roles_id=3");
        
        Iterator it = lusu.iterator();
        cmbcontiminm.addItem("-");
        while (it.hasNext()) {            
            usuobj = (Usuarios) it.next();
            cmbcontiminm.addItem(usuobj.getNombres() +" "+usuobj.getApellidos());
        }        
    }
    
    private void fillcomboboxinmueble(){
        Set<Inmuebles> linmu = null;
        Set<Contribuyentes> lctr=null;        
        Contribuyentes contobje = new Contribuyentes();
        Inmuebles inmtmp = new Inmuebles();
        Usuarios ustmp = new Usuarios();
        cmbimueblesiminm.removeAllItems();
        cmbimueblesiminm.addItem("-");
        if (cmbcontiminm.getSelectedItem().toString()!= "-") {
            ustmp = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "(nombres || ' ' || apellidos) = '"+ cmbcontiminm.getSelectedItem().toString() +"'");
            lctr =  ustmp.getContribuyenteses();        

            Iterator uno = lctr.iterator();
            while (uno.hasNext()) {
                contobje = (Contribuyentes) uno.next();            
            }                

            linmu = contobje.getInmuebleses();

            Iterator it = linmu.iterator();

            while (it.hasNext()) {
                inmtmp = (Inmuebles) it.next();
                cmbimueblesiminm.addItem(inmtmp.getDireccion());

            }            
        }        
    }
    
    private void fillcomboboximpuestos(){
        Set<Impuestosinmuebles> limpinm = null;
        List<Impuestos> limp = null;
        Impuestosinmuebles impinm = new Impuestosinmuebles();
        Impuestos tmpim = new Impuestos();
        Impuestos otrim = new Impuestos();
        Inmuebles tmpinm = new Inmuebles();

        try {
            //Cargando los impuestos posibles de aplicación
            limp = dao.findAll(Impuestos.class);
            //verificando y cargando impuestos posibles de aplicación  
            limpiartabla();
            cmbimpuestosiminm.removeAllItems();
            cmbimpuestosiminm.addItem("-");
            if (cmbimueblesiminm.getSelectedItem().toString().equals("-")) {                
            }
            else{
                tmpinm = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '"+ cmbimueblesiminm.getSelectedItem().toString() +"'");
                limpinm = tmpinm.getImpuestosinmuebleses();
                if (limpinm.isEmpty()) {                                        
                    Iterator it = limp.iterator();
                    while (it.hasNext()) {
                        tmpim = (Impuestos) it.next();
                        cmbimpuestosiminm.addItem(tmpim.getNombre());
                    }
                }
                else{
                    Iterator it = limp.iterator();
                    while(it.hasNext()){
                        otrim=(Impuestos) it.next();
                        impinm = (Impuestosinmuebles) dao.findByWhereStatementoneobj(Impuestosinmuebles.class, "inmuebles_id =" + tmpinm.getId() + " " +"and" +" "+ "impuestos_id ="+ otrim.getId());
                        try {
                            if (impinm != null) {
                                Object[] datos = new Object[1];                    
                                datos[0]=otrim.getNombre();
                                tablam.addRow(datos);
                            }
                            else{
                                cmbimpuestosiminm.addItem(otrim.getNombre());
                            }                            
                        } catch (Exception e) {
                        
                        }
                    }                    
                }                
            }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la carga de datos");
            e.printStackTrace();            
        }        
    }
    
    public void limpiartabla(){
        try {
            for (int i=jtiminm.getRowCount()-1; i>=0;i--)
            {
            tablam.removeRow(i);
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error limpiando tabla");
        }
        jtiminm.setModel(tablam);
    }     
    
    public void centrardatos(){
        alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        jtiminm.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);
    }    
    
    public void aplicar(){
        Impuestosinmuebles addimpunm = new Impuestosinmuebles();
        Impuestos impuesto = new Impuestos();
        Inmuebles inmueble = new Inmuebles();
        
        try {
            impuesto = (Impuestos) dao.findByWhereStatementoneobj(Impuestos.class, "nombre = '"+ cmbimpuestosiminm.getSelectedItem().toString() +"'");
            inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '"+ cmbimueblesiminm.getSelectedItem().toString() +"'");
            addimpunm.setImpuestos(impuesto);
            addimpunm.setInmuebles(inmueble);
            dao.save(addimpunm);
            JOptionPane.showMessageDialog(this, "Impuesto Asignado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la información");
            e.printStackTrace();
        }
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbcontiminm = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbimueblesiminm = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbimpuestosiminm = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtiminm = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnasignar = new javax.swing.JButton();
        btnsiminm = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        jLabel1.setText("Contribuyente:");

        cmbcontiminm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontiminmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbcontiminm, 0, 197, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbcontiminm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Inmueble:");

        cmbimueblesiminm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbimueblesiminmActionPerformed(evt);
            }
        });

        jLabel3.setText("Impuestos:");

        cmbimpuestosiminm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbimpuestosiminmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbimueblesiminm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbimpuestosiminm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbimueblesiminm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbimpuestosiminm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtiminm.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtiminm);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnasignar.setText("Asignar Impuesto");
        btnasignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnasignarActionPerformed(evt);
            }
        });

        btnsiminm.setText("Salir");
        btnsiminm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsiminmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnasignar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnsiminm, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnasignar)
                    .addComponent(btnsiminm))
                .addContainerGap())
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
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsiminmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsiminmActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsiminmActionPerformed

    private void cmbcontiminmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontiminmActionPerformed
        // TODO add your handling code here:
        if (cmbcontiminm.getSelectedItem().toString()!= "-") {            
            fillcomboboxinmueble();
        }
    }//GEN-LAST:event_cmbcontiminmActionPerformed

    private void cmbimueblesiminmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbimueblesiminmActionPerformed
        // TODO add your handling code here:
        if (cmbimueblesiminm.getSelectedItem().toString()!="-") {
            fillcomboboximpuestos();   
        }
    }//GEN-LAST:event_cmbimueblesiminmActionPerformed

    private void btnasignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnasignarActionPerformed
        // TODO add your handling code here:        
        aplicar();
        fillcomboboximpuestos();
    }//GEN-LAST:event_btnasignarActionPerformed

    private void cmbimpuestosiminmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbimpuestosiminmActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbimpuestosiminmActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnasignar;
    private javax.swing.JButton btnsiminm;
    private javax.swing.JComboBox cmbcontiminm;
    private javax.swing.JComboBox cmbimpuestosiminm;
    private javax.swing.JComboBox cmbimueblesiminm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtiminm;
    // End of variables declaration//GEN-END:variables
}
