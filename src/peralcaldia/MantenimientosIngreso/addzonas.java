/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import beans.VerZonas;
import javax.swing.table.DefaultTableModel;
import controller.AbstractDAO;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.StyledEditorKit;
import peralcaldia.model.Zonas;
import util.GCMPNativos;
/**
 *
 * @author alex
 */
/*Pantalla destinada a la creación de zonas en el sistema*/
public class addzonas extends javax.swing.JInternalFrame {
    DefaultTableModel tablam = new DefaultTableModel();
    DefaultTableCellRenderer alinearCentro, alinearcentroceldas;    
    AbstractDAO dao = new AbstractDAO();
    GCMPNativos componentes = new GCMPNativos();
    
    /**
     * Creates new form addzonas
     */
    public addzonas() {
        initComponents();
        jtaddzonas.setModel(tablam);        
        cargarjtable();
        centrardatos();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Metodo encargado de la carga de las zonas y sus caracteristicas en pantalla por medio del JTable*/
    public void cargarjtable(){    
        List<Zonas> lzon=null;
        Zonas zonesobj= new Zonas();
        List otls = new ArrayList();
        VerZonas vzn = new VerZonas();
        lzon = dao.findAll(Zonas.class);
        
        Iterator it = lzon.iterator();
        
        while(it.hasNext()){
            vzn = new VerZonas();
            try {
            zonesobj = (Zonas) it.next();
            vzn.setCODIGO_ZONA(zonesobj.getZonecode());
            vzn.setDESCRIPCIÓN(zonesobj.getZona());
            vzn.setNUMERO_DE_INMUEBLES_ACTUALES(zonesobj.getCorrelativozona());
            otls.add(vzn);                            
            }
            catch (Exception e) 
            {
                JOptionPane.showMessageDialog(this, "Error en la carga de datos");
                e.printStackTrace();
            }
        }
        tablam = componentes.limpiartabla(tablam);
        tablam = componentes.llenartabla(tablam, otls, VerZonas.class);
        jtaddzonas.setModel(tablam);        
    }
    
    /*Metodo para Centrar los Datos del Jtable*/
    public void centrardatos(){
        alinearCentro = new DefaultTableCellRenderer();        
        alinearcentroceldas = new DefaultTableCellRenderer();                
        Font negrilla = new Font( "Tahoma",Font.BOLD,18 );        
        alinearCentro.setHorizontalAlignment(0);          
        alinearCentro.setFont(negrilla);
        alinearcentroceldas.setHorizontalAlignment(SwingConstants.CENTER);               
        jtaddzonas.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);
        jtaddzonas.getColumnModel().getColumn(1).setHeaderRenderer(alinearCentro);
        jtaddzonas.getColumnModel().getColumn(2).setHeaderRenderer(alinearCentro);
        jtaddzonas.getColumnModel().getColumn(0).setCellRenderer(alinearcentroceldas);
        jtaddzonas.getColumnModel().getColumn(2).setCellRenderer(alinearcentroceldas);

    }
    
    /*Metodo utlizado para la verificacion, inicializacion, seteo de las propiedades y 
     * Guardado de las zonas una vez estas cumplan con los requisitos para ello*/
    public void guardarzona(){
        Zonas nzone = new Zonas();
        
        nzone.setZonecode(txtcodez.getText().toUpperCase());
        nzone.setZona(txtzona.getText().toUpperCase());
        nzone.setCorrelativozona(1);
        
        try {
            Zonas otz = new Zonas();
            otz = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, "zona ='" + txtzona.getText().toUpperCase() + "'");            
            if (otz==null){
                otz = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, "zone_code ='"+ txtcodez.getText().toUpperCase() +"'");
                if (otz==null) {
                    dao.save(nzone);
                    limpiarpantalla();
//                    tablam = componentes.limpiartabla(tablam);             
                    cargarjtable();
                    centrardatos();
                    JOptionPane.showMessageDialog(this, "Guardado Completo");                                               
                }
                else {
                    JOptionPane.showMessageDialog(this, "El codigo ya ha sido asignado a otra zona, ingresar un codigo valido");
                }
                
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
    
    /*Metodo para Limpiar la informacion en pantalla*/
    public void limpiarpantalla(){
        txtzona.setText("");
        txtcodez.setText("");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaddzonas = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtzona = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtcodez = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btngzona = new javax.swing.JButton();
        btnszona = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Nombre Zona:");

        jLabel2.setText("Codigo:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtzona)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtcodez)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtcodez, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtaddzonas;
    private javax.swing.JTextField txtcodez;
    private javax.swing.JTextField txtzona;
    // End of variables declaration//GEN-END:variables
}
