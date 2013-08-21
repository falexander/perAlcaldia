/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import peralcaldia.model.Pagos;
import peralcaldia.model.Usuarios;
import util.totalesimpuestosinmuebles;
import util.verpagos;
/**
 *
 * @author alex
 */
public class generarpagos extends javax.swing.JInternalFrame {
    AbstractDAO dao= new AbstractDAO();
    DefaultTableModel tablam = new DefaultTableModel();
    DefaultTableCellRenderer alinearCentro;    
    /**
     * Creates new form generarpagos
     */
    public generarpagos() {
        initComponents();
        fillComboBoxcontribuyente();
        tablam.addColumn("SELECCIONAR");
        tablam.addColumn("MES");
        tablam.addColumn("MONTO A PAGAR");
        tablam.addColumn("ESTADO");
        jtpagos.setModel(tablam); 
        centrardatos();        
    }
    
    private void fillComboBoxcontribuyente(){
        List<Usuarios> lusu=null;
        Usuarios usuobj = new Usuarios();
        lusu = dao.findByWhereStatement(Usuarios.class, "roles_id=3");
        
        Iterator it = lusu.iterator();
        cmbcontribuyente.addItem("-");
        while (it.hasNext()) {            
            usuobj = (Usuarios) it.next();
            cmbcontribuyente.addItem(usuobj.getNombres() +" "+usuobj.getApellidos());
        }        
    }    
    
    private void fillcomboboxinmueble(){
        Set<Inmuebles> linmu = null;
        Set<Contribuyentes> lctr=null;        
        Contribuyentes contobje = new Contribuyentes();
        Inmuebles inmtmp = new Inmuebles();
        Usuarios ustmp = new Usuarios();
        cmbinmuebles.removeAllItems();
        cmbinmuebles.addItem("-");
        if (cmbcontribuyente.getSelectedItem().toString()!= "-") {
            ustmp = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "(nombres || ' ' || apellidos) = '"+ cmbcontribuyente.getSelectedItem().toString() +"'");
            lctr =  ustmp.getContribuyenteses();        

            Iterator uno = lctr.iterator();
            while (uno.hasNext()) {
                contobje = (Contribuyentes) uno.next();            
            }                

            linmu = contobje.getInmuebleses();

            Iterator it = linmu.iterator();

            while (it.hasNext()) {
                inmtmp = (Inmuebles) it.next();
                cmbinmuebles.addItem(inmtmp.getDireccion());

            }            
        }        
    }    
    
    public void limpiartabla(){
        try {
            for (int i=jtpagos.getRowCount()-1; i>=0;i--)
            {
            tablam.removeRow(i);
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error limpiando tabla");
        }
        jtpagos.setModel(tablam);
    }     
    
    public void centrardatos(){
        alinearCentro = new DefaultTableCellRenderer();
        alinearCentro.setHorizontalAlignment(0);
        jtpagos.getColumnModel().getColumn(0).setHeaderRenderer(alinearCentro);        
    }    
    
    public void cargarjtable(){
        Set<Impuestosinmuebles> limpinm = null;
        Set<Pagos> lpagos = null;
        List<verpagos> lvpag = new ArrayList<verpagos>();
        List<String> lmeses = Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12");
        List<totalesimpuestosinmuebles> ltot =null;
        Impuestos tmpim = new Impuestos();
        Inmuebles tmpinm = new Inmuebles();
        Pagos tmppagos = new Pagos();
        verpagos vpg = new verpagos();
        Object[] datos = new Object[4];
        int contador,anio,actual;
        String compruebames,formato;
        Date registroinmu;
        Calendar c1;
        
        try {

            if(cmbinmuebles.getSelectedItem().toString().equals("-")){
              //no hay datos para cargar  
            }
            else{
                limpiartabla();
                tmpinm = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '"+ cmbinmuebles.getSelectedItem().toString() +"'");                
                limpinm = tmpinm.getImpuestosinmuebleses();                
                lpagos = tmpinm.getPagoses(); 
                if (limpinm.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El inmueble aun no tiene impuestos asignados");
                }
                else{
                    ltot = calcularmonotosimpuestos(limpinm);
                    registroinmu = tmpinm.getFecharegistro();
                    formato="yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
                    //anio de registro del inmueble
                    anio = Integer.parseInt(dateFormat.format(registroinmu));
                    //obteniendo anio actual
                    c1= Calendar.getInstance();
                    actual = c1.get(Calendar.YEAR);
                    //obteniendo lista de pagos
                    while (anio <= actual) {                        
                        contador=1;
                        for (int i = 0; i < 11; i++) {
                            Iterator it = lpagos.iterator();
                            while (it.hasNext()) {
                                compruebames=lmeses.get(i)+"-"+ Integer.toString(anio);
                                tmppagos = (Pagos) it.next();
                                if (tmppagos.getMespagado() == compruebames) {
                                    vpg.setEstapagado(true);
                                    vpg.setMespagado(tmppagos.getMespagado());
                                    vpg.setMonto(tmppagos.getMontopagado());
                                    lvpag.add(vpg);
                                }
                                else{
                                    
                                }
                                
                            }
                           contador+=1; 
                        }
                    }
                }
                
                
            }
                        
        } catch (Exception e) {
        }
        
    }
    
    public List calcularmonotosimpuestos(Set limpuestos){
        List<totalesimpuestosinmuebles> objects = new ArrayList<totalesimpuestosinmuebles>();
        Impuestos tmpimpuesto = new Impuestos();
        
        
        
        return objects;
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
        cmbcontribuyente = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbinmuebles = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtpagos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btngenerar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jLabel1.setText("Contribuyente:");

        cmbcontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontribuyenteActionPerformed(evt);
            }
        });

        jLabel2.setText("Inmuebles:");

        cmbinmuebles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbinmueblesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbinmuebles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cmbinmuebles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtpagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtpagos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        btngenerar.setText("Generar Pre-Recibo");

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
                .addGap(85, 85, 85)
                .addComponent(btngenerar)
                .addGap(83, 83, 83)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btngenerar)
                    .addComponent(jButton1))
                .addContainerGap())
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbcontribuyenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribuyenteActionPerformed
        // TODO add your handling code here:
        fillcomboboxinmueble();
    }//GEN-LAST:event_cmbcontribuyenteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbinmueblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinmueblesActionPerformed
        // TODO add your handling code here:
        cargarjtable();
    }//GEN-LAST:event_cmbinmueblesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btngenerar;
    private javax.swing.JComboBox cmbcontribuyente;
    private javax.swing.JComboBox cmbinmuebles;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtpagos;
    // End of variables declaration//GEN-END:variables
}
